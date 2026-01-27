mvn clean package -DskipTests

mvn spring-boot:run

java -jar target/app-basic-java-0.0.1-SNAPSHOT.jar

docker build -t app-basic-java:1.0 .
docker images | grep app-basic-java
docker run -p 8080:8080 --name app-basic-java app-basic-java:1.0
docker stop app-basic-java

http://localhost:8080/clientes
http://localhost:8080/healthCheck