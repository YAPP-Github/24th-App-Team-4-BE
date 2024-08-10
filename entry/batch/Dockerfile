FROM amazoncorretto:17

ARG JAR_FILE=entry/batch/build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
