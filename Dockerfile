FROM azul/zulu-openjdk:11

ENV APP_DIR /app
ENV PORT 8080
ENV HTTP_AUTH_TOKEN None
ENV COMMAND_TIMEOUT 1200

RUN mkdir $APP_DIR
WORKDIR $APP_DIR

COPY target/agent-4.1.0-SNAPSHOT-exec.jar $APP_DIR
COPY https $APP_DIR/https

ENTRYPOINT ["java", "-jar", "/app/agent-4.1.0-SNAPSHOT-exec.jar"]
