# 1. Abrir Terminal en VS Code (Ctrl+`)
# 2. Navegar a la raíz del proyecto

# 3. Limpiar y compilar
mvn clean compile

# 4. Si hay errores, resolver dependencias:
mvn dependency:resolve

# 5. Ejecutar tests (opcional)
mvn test

# 6. Ejecutar aplicación
mvn exec:java -Dexec.mainClass="com.huawei.functiongraph.obs.LocalRunner"
mvn clean package -DskipTests

# 7. O ejecutar directamente con Java
mvn clean package -DskipTests
java -cp "target/huawei-fg-obs-minimal-1.0.0.jar;target/dependency/*" com.huawei.functiongraph.obs.LocalRunner

mvn exec:java '-Dexec.mainClass=com.huawei.functiongraph.obs.LocalRunner'

