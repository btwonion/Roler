ARG BUILD_DIR="/usr/src/Roler/"

FROM gradle:7.5.1-jdk AS builder

ARG BUILD_DIR

WORKDIR $BUILD_DIR
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew installDist --no-daemon

FROM openjdk:17-slim

ARG BUILD_DIR

WORKDIR /app/
COPY --from=builder $BUILD_DIR/build/install/Roler/ .

CMD ["./bin/Roler"]