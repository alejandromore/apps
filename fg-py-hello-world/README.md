# Huawei FunctionGraph - Hello World (Python)

# Preparar ambiente
python -m venv venv
venv\Scripts\activate
pip install fastapi uvicorn requests
pip install huaweicloudsdkfunctiongraph huaweicloudsdkcore


# Ejecutar localmente 
uvicorn app_local:local_handler --reload --port 8000

# Prueba Codigo local golpeando al servicio que escucha
python .\test_local.py
python .\test_remote.py




## Empaquetar artefacto para rubirlo
.\package.ps1

## Setup
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
pip install huaweicloudsdkfunctiongraph huaweicloudsdkcore



python test_remotev2.py

## Test
pytest

{
    "queryString": {
        "name": "Alejandro"
    }
}


pip install huaweicloudsdkfunctiongraph huaweicloudsdkcore
