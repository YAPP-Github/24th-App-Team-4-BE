FROM amazoncorretto:17

ARG JAR_FILE=entry/batch/build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENV SERVER_PORT=8081

ENTRYPOINT ["java", "-Dserver.port=${SERVER_PORT}", "-jar", "/app.jar"]
