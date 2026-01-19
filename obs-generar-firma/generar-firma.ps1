# Generate_OBS_For_Bruno.ps1
$AK = "8ENLOAE2QCECKCRKANEU"
$SK = "vddTKjKuG8hcNGOb1cYv3jZ03RLlkOFEEhEHphl8"
$Bucket = "obs-test-alejandro"
$VirtualHost = "$Bucket.obs.ap-southeast-1.myhuaweicloud.com"

# Generar fecha actual
$Date = (Get-Date).ToUniversalTime().ToString("ddd, dd MMM yyyy HH:mm:ss 'GMT'")

# String to sign CORRECTO según el error mostrado
# El error muestra: GET\n\n\n[Date]\n\n/bucket/
$StringToSign = "GET`n`n`n$Date`n`n/$Bucket/"

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   GENERADOR PARA BRUNO (SIN LICENCIA)" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "String to sign:" -ForegroundColor Yellow
Write-Host "GET\\n\\n\\n$Date\\n\\n/$Bucket/" -ForegroundColor Gray
Write-Host ""

# Mostrar bytes para verificar
$Bytes = [System.Text.Encoding]::UTF8.GetBytes($StringToSign)
Write-Host "Longitud del string: $($Bytes.Length) bytes" -ForegroundColor Yellow

# Generar firma HMAC-SHA1
$hmac = New-Object System.Security.Cryptography.HMACSHA1
$hmac.Key = [System.Text.Encoding]::UTF8.GetBytes($SK)
$hash = $hmac.ComputeHash([System.Text.Encoding]::UTF8.GetBytes($StringToSign))
$Signature = [Convert]::ToBase64String($hash)

$AuthHeader = "OBS $AK`:$Signature"

Write-Host ""
Write-Host "=== PASOS PARA BRUNO ===" -ForegroundColor Green
Write-Host ""

Write-Host "1. Abre Bruno" -ForegroundColor Yellow
Write-Host "2. Crea un nuevo request" -ForegroundColor Yellow
Write-Host "3. Configura así:" -ForegroundColor Yellow
Write-Host ""

Write-Host "   URL: https://$VirtualHost/" -ForegroundColor Green
Write-Host "   Método: GET" -ForegroundColor Green
Write-Host ""

Write-Host "4. Agrega estos Headers:" -ForegroundColor Yellow
Write-Host ""

Write-Host "   Header 1:" -ForegroundColor Cyan
Write-Host "   Key: Host" -ForegroundColor White
Write-Host "   Value: $VirtualHost" -ForegroundColor White
Write-Host ""

Write-Host "   Header 2:" -ForegroundColor Cyan
Write-Host "   Key: Date" -ForegroundColor White
Write-Host "   Value: $Date" -ForegroundColor White
Write-Host ""

Write-Host "   Header 3:" -ForegroundColor Cyan
Write-Host "   Key: Authorization" -ForegroundColor White
Write-Host "   Value: $AuthHeader" -ForegroundColor White
Write-Host ""

Write-Host "5. Haz clic en SEND" -ForegroundColor Yellow
Write-Host ""

# Generar archivo de configuración para copiar/pegar
$BrunoConfig = @"
==================================================
CONFIGURACION PARA BRUNO - OBS Huawei Cloud
==================================================

URL: 
https://$VirtualHost/

METODO:
GET

HEADERS (agregar uno por uno en Bruno):

1. Host
   Key: Host
   Value: $VirtualHost

2. Date
   Key: Date
   Value: $Date

3. Authorization
   Key: Authorization
   Value: $AuthHeader

==================================================
INFORMACION TECNICA:
- Bucket: $Bucket
- Access Key: $AK
- Fecha generada: $Date
- Firma: $Signature
- String to sign: GET\\n\\n\\n$Date\\n\\n/$Bucket/
==================================================
"@

$BrunoConfig | Out-File -FilePath "Bruno_OBS_Config.txt" -Encoding UTF8
Write-Host "✅ Configuración guardada en: Bruno_OBS_Config.txt" -ForegroundColor Green
Write-Host "   (Abre el archivo y copia/pega en Bruno)" -ForegroundColor Gray

Write-Host ""
Write-Host "Presiona Enter para abrir el archivo de configuración..." -ForegroundColor Cyan
Read-Host

# Abrir el archivo generado
Start-Process "Bruno_OBS_Config.txt"