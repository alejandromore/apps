mvn install:install-file `
  -Dfile=libs/RunTime-2.0.5.jar `
  -DgroupId=com.huawei `
  -DartifactId=runtime `
  -Dversion=2.0.5 `
  -Dpackaging=jar `
  -DgeneratePom=true

mvn clean compile
mvn compile -DskipTests
mvn package

.\build.bat

"https://functiongraph.la-south-2.myhuaweicloud.com/v2/0371a9a7f90b493fadebbf130f6fcd2c/fgs/functions/urn:fss:la-south-2:0371a9a7f90b493fadebbf130f6fcd2c:function:default:fgs-http-java:latest/invocations"