# Limpieza
Remove-Item -Recurse -Force build -ErrorAction SilentlyContinue
Remove-Item fg-py-hello-world.zip -ErrorAction SilentlyContinue

# Crear carpeta build
New-Item -ItemType Directory -Name build | Out-Null

# Copiar handler
Copy-Item index.py build\

# Instalar dependencias runtime (si las hubiera)
pip install -r requirements.txt -t build --no-deps

# Crear ZIP
Compress-Archive -Path build\* -DestinationPath fg-py-hello-world.zip

Write-Host "ZIP generado: fg-py-hello-world.zip"

