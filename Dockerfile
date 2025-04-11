FROM registry.access.redhat.com/ubi9/openjdk-21:1.22-1.1743606703 as base

FROM base as builder

WORKDIR /home/default/app

COPY --chown=default:0 . .

ARG GIT_USERNAME
ARG GIT_TOKEN

RUN ./gradlew bootJar

FROM base

COPY --from=builder /home/default/app/build/libs/midi-guitar-server.jar .

ENTRYPOINT ["java", "-jar", "midi-guitar-server.jar"]
