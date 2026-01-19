param(
    [string]$Environment = "dev",
    [switch]$Help
)

if ($Help) {
    Write-Host "Uso: .\run-project.ps1 [-Environment dev|prod|test] [-Help]"
    exit 0
}

Write-Host "=== Huawei FunctionGraph Runner ===" -ForegroundColor Cyan
Write-Host "Entorno: $Environment" -ForegroundColor Yellow

# Configurar según entorno
switch ($Environment) {
    "dev" {
        $env:HUAWEI_AK = "8ENLOAE2QCECKCRKANEU"
        $env:HUAWEI_SK = "vddTKjKuG8hcNGOb1cYv3jZ03RLlkOFEEhEHphl8"
        $env:OBS_ENDPOINT = "obs.la-south-2.myhuaweicloud.com"
        $env:OBS_BUCKET = "obs-test-alejandro"
        $env:LOG_LEVEL = "DEBUG"
    }
}

# Ejecutar
Write-Host "`n🚀 Ejecutando proyecto..." -ForegroundColor Cyan
mvn clean compile exec:java '-Dexec.mainClass=com.huawei.functiongraph.obs.LocalRunner'