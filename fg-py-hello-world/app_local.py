from index import handler as huawei_handler
import json
from urllib.parse import parse_qs

async def local_handler(scope, receive, send):
    """
    Handler ASGI para desarrollo local
    """
    if scope['type'] != 'http':
        return
    
    # 1. Parsear query string
    query_string = scope.get('query_string', b'').decode()
    query_params = parse_qs(query_string)
    
    # Convertir listas a valores únicos
    query_params_simple = {}
    for key, value in query_params.items():
        query_params_simple[key] = value[0] if len(value) == 1 else value
    
    # 2. Simular evento de Huawei
    event = {
        "httpMethod": scope['method'],
        "path": scope['path'],
        "queryString": query_params_simple,
        "headers": {k.decode(): v.decode() for k, v in scope['headers']}
    }
    
    # 3. Simular contexto de Huawei
    context = {
        "function_name": "local-dev",
        "memory_limit": "256MB",
        "request_id": "local-123"
    }
    
    # 4. Ejecutar handler de Huawei
    result = huawei_handler(event, context)
    
    # 5. Responder
    await send({
        'type': 'http.response.start',
        'status': result['statusCode'],
        'headers': [(b'content-type', b'application/json')]
    })
    
    await send({
        'type': 'http.response.body',
        'body': result['body'].encode('utf-8')
    })

# Para ejecutar: uvicorn app_local:local_handler --reload