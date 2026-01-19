FROM eclipse-temurin:17-jdk-focal
ENV SPRING_PROFILES_ACTIVE=default \
    JAVA_OPTS="" \
    TZ=UTC
WORKDIR /app
COPY target/demo*.jar app.jar
EXPOSE 8080

# Variables de entorno para Huawei Cloud
ENV DEW_ENABLED=true
ENV DEW_REGION=eu-west-0
ENV DEW_SECRET_NAME=cliente-crud-db-secrets

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]