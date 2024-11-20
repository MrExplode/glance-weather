FROM gradle:jdk21-alpine AS builder

WORKDIR /app

COPY . .
RUN gradle build --no-daemon

FROM gcr.io/distroless/java21-debian12:nonroot AS runner

WORKDIR /app
COPY --from=builder /app/build/libs/weather-extension.jar .

CMD [ "weather-extension.jar" ]