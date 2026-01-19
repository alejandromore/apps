#!/usr/bin/env python3
"""
Crear paquete de despliegue para FunctionGraph
"""

import zipfile
import os
import sys

def create_zip_package():
    """Crear archivo ZIP con todas las dependencias"""
    
    # Archivos a incluir
    files_to_include = [
        'index.py'
    ]
    
    # Nombre del archivo ZIP
    zip_filename = 'fg-basic-py-event.zip'
    
    # Crear archivo ZIP
    with zipfile.ZipFile(zip_filename, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for file in files_to_include:
            if os.path.exists(file):
                zipf.write(file)
                print(f"✅ Añadido: {file}")
            else:
                print(f"❌ No encontrado: {file}")
    
    # Calcular tamaño
    size_bytes = os.path.getsize(zip_filename)
    size_mb = size_bytes / (1024 * 1024)
    
    print(f"\n📦 Paquete creado: {zip_filename}")
    print(f"📏 Tamaño: {size_bytes:,} bytes ({size_mb:.2f} MB)")
    
    # Listar contenido
    print(f"\n📁 Contenido del ZIP:")
    with zipfile.ZipFile(zip_filename, 'r') as zipf:
        for file_info in zipf.infolist():
            print(f"  - {file_info.filename}")

if __name__ == "__main__":
    create_zip_package()