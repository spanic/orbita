# syntax=docker/dockerfile:1

# --- Build stage: build the payments-service jar (and its shared dependency) ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
# -pl <module> -am builds the module plus the modules it depends on (shared).
RUN ./mvnw -B -ntp -pl payments-service -am -DskipTests package

# --- Runtime stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/payments-service/target/payments-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
