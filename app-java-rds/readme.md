mvn clean package -DskipTests

mvn spring-boot:run

java -jar target/app-basic-java-0.0.1-SNAPSHOT.jar

docker build -t app-basic-java:1.0 .
docker images | grep app-basic-java
docker run -p 8080:8080 --name app-basic-java app-basic-java:1.0
docker stop app-basic-java

http://localhost:8080/clientes
http://localhost:8080/healthCheck

----------------
# Publicarlo en artifactory
mvn clean package -DskipTests

mvn -s settings.xml deploy:deploy-file `
-Durl="http://admin:ingE2004#@localhost:8082/artifactory/maven-alejandro" `
-DrepositoryId="maven-alejandro" `
-Dfile="target/app-basic-java-0.0.1-SNAPSHOT.jar" `
-DgroupId="com.alejandro" `
-DartifactId="app-basic-java" `
-Dversion="0.0.1-SNAPSHOT" `
-Dpackaging="jar" `
-DgeneratePom="true"


mvn deploy:deploy-file `
-Durl=http://localhost:8082/artifactory/maven-alejandro `
-DrepositoryId=maven-alejandro `
-Dfile="target/app-basic-java-0.0.1-SNAPSHOT.jar" `
-DpomFile=pom.xml

curl.exe -u admin:ingE2004# `
 -T target\app-basic-java-0.0.1-SNAPSHOT.jar `
 "http://localhost:8082/artifactory/maven-alejandro/alejandro/0.0.1-SNAPSHOT/app-basic-java-0.0.1-SNAPSHOT.jar"