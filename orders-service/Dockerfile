# syntax=docker/dockerfile:1

# --- Build stage: build the orders-service jar (and its shared dependency) ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
# -pl <module> -am builds the module plus the modules it depends on (shared).
RUN ./mvnw -B -ntp -pl orders-service -am -DskipTests package

# --- Runtime stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/orders-service/target/orders-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
