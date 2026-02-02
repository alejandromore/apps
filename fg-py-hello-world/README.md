# Huawei FunctionGraph - Hello World (Python)

# Ejecutar localmente
venv\Scripts\activate
uvicorn local_app:app --reload --port 8000

# Test unitario
pytest

## Requisitos
- Python 3.9
- virtualenv

## Setup
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
pip install huaweicloudsdkfunctiongraph huaweicloudsdkcore

## Ejecutar local
uvicorn local_app:app --reload
uvicorn local_server:app --reload --port 8000

python test_handler_remote.py

## Test
pytest

## Empaquetar
.\package.ps1

## Deploy
- Runtime: Python 3.9
- Handler: handler.handler
- Subir function.zip
- Crear HTTP Trigger (GET)

{
    "queryString": {
        "name": "Alejandro"
    }
}


pip install huaweicloudsdkfunctiongraph huaweicloudsdkcore
