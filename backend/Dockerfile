

# Etapa 1: Build
#FROM gradle:8.5-jdk17 AS builder
#WORKDIR /app
#COPY . .
#RUN gradle clean build -x test

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
#COPY --from=builder /app/build/libs/backend-oportuniaapp-0.0.1-SNAPSHOT.jar  app.jar
# use the next commando to update de jar before add to docker .\gradlew clean build -x test
ARG JAR_FILE=/build/libs/backend-oportuniaapp-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
CMD ["java", "-jar", "app.jar"]

EXPOSE 8080