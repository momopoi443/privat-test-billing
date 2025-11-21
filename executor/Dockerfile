FROM gradle:9.2.0-jdk25-alpine AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./

COPY src ./src

RUN gradle installDist --no-daemon

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/install/executor ./

ENTRYPOINT ["./bin/executor"]