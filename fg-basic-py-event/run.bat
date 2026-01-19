@echo off
echo 🔧 Preparando entorno...
echo.

REM Crear entorno virtual (opcional)
py -m venv venv 2>nul
if exist venv (
    call venv\Scripts\activate
    echo Entorno virtual activado
)

REM Instalar dependencias
echo 📦 Instalando dependencias...
pip install -r requirements.txt --quiet >nul 2>&1

echo.
echo 🧪 EJECUTANDO TESTS LOCALES
echo.

REM Ejecutar todos los tests por defecto
py test_local.py --test all

REM Desactivar entorno virtual si estaba activo
if exist venv (
    deactivate
    echo Entorno virtual desactivado
)

pause