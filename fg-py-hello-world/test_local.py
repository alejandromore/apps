# test_local.py - CLIENTE SOLO
import requests
import json

url = "http://127.0.0.1:8000/"

# Opción 1: POST con body JSON (como Bruno)
payload = {"queryString": {"name": "Alejandro"}}

# Opción 2: GET con query string (alternativa)
#url = "http://127.0.0.1:8000/?name=Alejandro"

try:
    # Prueba ambos métodos
    response = requests.post(url, json=payload, timeout=5)
    #response = requests.get(url, timeout=5)  # Para GET
    
    print(f"Status: {response.status_code}")
    
    if response.status_code == 200:
        try:
            data = response.json()
            print("Éxito:", json.dumps(data, indent=2))
        except:
            print("Respuesta:", response.text[:500])
    else:
        print(f"Error {response.status_code}: {response.text}")

except requests.exceptions.ConnectionError:
    print("ERROR: Servidor no está corriendo")
    print("Ejecuta en otra terminal: uvicorn asgi_app:app --reload --port 8000")
except Exception as e:
    print(f"Error: {e}")