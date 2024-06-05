FROM gradle:jdk21-alpine AS builder

WORKDIR /app

COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:21-alpine as runner

WORKDIR /app
COPY --from=builder /app/build/libs/weather-extension.jar .

CMD [ "java", "-jar", "weather-extension.jar" ]