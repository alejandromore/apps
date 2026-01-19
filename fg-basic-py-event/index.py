#!/usr/bin/env python3
import json

def handler(event, context):
    # Obtener método HTTP
    http_method = event.get('httpMethod', 'GET')
    
    # Solo responder a GET
    if http_method.upper() != 'GET':
        return {
            'statusCode': 405,
            'headers': {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            'body': json.dumps({
                'error': 'Método no permitido',
                'allowed': ['GET']
            })
        }
    
    # Obtener parámetros de query
    query_params = event.get('queryStringParameters', {}) or {}
    name = query_params.get('name')
    
    # Respuesta Hello World
    response_data = {
        'message': f'Hello world {name}!',
        'service': 'Huawei Cloud FunctionGraph',
        'method': 'GET',
        'timestamp': '2024-01-15T10:00:00Z'  # En producción usar datetime
    }
    
    return {
        'statusCode': 200,
        'headers': {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        'body': json.dumps(response_data)
    }