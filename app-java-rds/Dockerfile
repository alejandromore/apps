FROM eclipse-temurin:17-jdk-focal
ENV TZ=UTC-5
WORKDIR /app
COPY target/app-basic-java-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]

