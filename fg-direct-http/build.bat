@echo off
REM Crear estructura para ZIP manualmente
echo Creating ZIP structure...
mkdir target\zip-content 2>nul
copy target\fgs-direct-http-1.0.0.jar target\zip-content\ 1>nul
copy bootstrap target\zip-content\ 1>nul

REM Crear ZIP
echo Creating ZIP file...
powershell -Command "Compress-Archive -Path 'target\zip-content\*' -DestinationPath 'target\fgs-direct-http.zip' -Force"

REM Limpiar
rmdir /s /q target\zip-content 2>nul