FROM eclipse-temurin:17-jre-jammy

ENV TZ=UTC-5 \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

WORKDIR /app
COPY target/app-basic-java-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]