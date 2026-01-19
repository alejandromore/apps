mvn clean package -DskipTests

dir target\*.jar

mvn exec:java
mvn exec:java '-Dexec.mainClass=com.example.Function'
mvn clean compile exec:java


Request de ejemplo
{
    "queryStringParameters": {
        "query01": "valor1"
    },
    "pathParameters": {
        "id": "123"
    },
    "headers": {
        "x-version": "1.0"
    },
    "body": "{\"nombres\": \"Miguel\", \"apellidos\": \"Timana\"}"
}
{"nombres": "Miguel", "apellidos": "Timana"}

Invoke-RestMethod -Uri http://localhost:8080/test -Method Post -Headers @{"Content-Type"="application/json"} -Body (Get-Content test_request.json -Raw)

Invoke-RestMethod -Uri "https://3606a742f89644148f83092cf970121d.apic.la-south-2.huaweicloudapis.com/fg-basic-java-event/123?query01=valor1" `
    -Method Post `
    -Headers @{
        "Content-Type" = "application/json"
        "x-version" = "1.0"
    } `
    -Body (Get-Content huawei_request.json -Raw)

