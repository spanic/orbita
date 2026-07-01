# Orbita

Multi-module Spring Boot 4 project. Two independently deployable services backed by
their own PostgreSQL database and a shared Kafka broker, plus a lightweight shared
utilities module.

## Modules

| Module             | Type           | Description                                          |
| ------------------ | -------------- | ---------------------------------------------------- |
| `shared`           | library jar    | Cross-service utilities. Framework-light on purpose. |
| `orders-service`   | executable jar | REST + JPA/PostgreSQL + Kafka. HTTP on `:8081`.      |
| `payments-service` | executable jar | REST + JPA/PostgreSQL + Kafka. HTTP on `:8082`.      |

Each service exposes a `GET /hello` endpoint (returns a greeting built via the
`shared` module) and Actuator health/info at `/actuator/health`, `/actuator/info`.

## Tech stack

- Java 21, Spring Boot 4.1.0, Maven (multi-module reactor)
- Spring Web MVC, Spring Data JPA (PostgreSQL), Spring for Apache Kafka
- Lombok (annotation processing wired in the parent POM)
- Testing: JUnit 5, Mockito, AssertJ, Spring Boot test slices
  (`@WebMvcTest`, `@DataJpaTest`, Kafka test support) and **Testcontainers**
  (PostgreSQL + Kafka) wired via Spring Boot `@ServiceConnection`

## Build & test

```bash
./mvnw clean verify          # full build; integration tests need Docker (see below)
./mvnw -DskipTests package   # just build the jars
```

Run a single service jar:

```bash
java -jar orders-service/target/orders-service-0.0.1-SNAPSHOT.jar
```

### Tests and Docker

- **Unit / web-slice tests** (`GreetingsTest`, `HelloControllerTest`) need **no Docker**:

  ```bash
  ./mvnw test -Dtest='!*ApplicationTests' -DfailIfNoTests=false
  ```

- **Integration tests** (`*ApplicationTests`, `@SpringBootTest`) start real PostgreSQL
  and Kafka via Testcontainers and therefore require a **running Docker daemon**. With
  Docker available, a plain `./mvnw test` runs everything.

### Run a service locally with throwaway infrastructure

`Test<Service>Application` (in `src/test`) boots the app with PostgreSQL and Kafka
provided by Testcontainers — no manual setup. Run it from your IDE, or:

```bash
./mvnw -pl orders-service spring-boot:test-run
```

## Containers

Each service has a multi-stage `Dockerfile` (build context is the repo root so the
`shared` module is available). Bring up the full stack with:

```bash
docker compose up --build
# orders   -> http://localhost:8081/hello
# payments -> http://localhost:8082/hello
```

## Configuration

Settings live in each service's `src/main/resources/application.yaml` and are
overridable via environment variables (defaults target `localhost`):

| Variable                                      | Default                                 |
| --------------------------------------------- | --------------------------------------- |
| `ORDERS_DB_URL` / `PAYMENTS_DB_URL`           | `jdbc:postgresql://localhost:5432/<db>` |
| `ORDERS_DB_USER` / `PAYMENTS_DB_USER`         | `<service>`                             |
| `ORDERS_DB_PASSWORD` / `PAYMENTS_DB_PASSWORD` | `<service>`                             |
| `KAFKA_BOOTSTRAP_SERVERS`                     | `localhost:9092`                        |
| `SERVER_PORT`                                 | `8081` (orders) / `8082` (payments)     |

> `spring.jpa.hibernate.ddl-auto` is set to `update` for boilerplate convenience.
> Switch to a migration tool (Flyway/Liquibase) before production.

## How the POMs are organized

- The **root POM** (`packaging: pom`) inherits the Spring Boot BOM for version
  management, declares the only two truly universal dependencies (Lombok + the core
  test stack), and centralizes the Lombok/compiler and Spring Boot plugin config.
- **`shared`** stays minimal — no web/JPA/Kafka — so it remains a clean utility library.
- Each **service** declares its own web/JPA/Kafka/Testcontainers stack, so the two can
  evolve independently. Versions still come from the parent BOM, so dependency blocks
  carry no version numbers.
