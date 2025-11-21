FROM gradle:9.2.0-jdk25-alpine AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle

RUN gradle dependencies --no-daemon || true

COPY src ./src

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]