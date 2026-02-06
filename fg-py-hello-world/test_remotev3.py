# coding: utf-8

import os
import json
from huaweicloudsdkcore.auth.credentials import BasicCredentials
from huaweicloudsdkfunctiongraph.v2.region.functiongraph_region import FunctionGraphRegion
from huaweicloudsdkcore.exceptions import exceptions
from huaweicloudsdkfunctiongraph.v2 import *

if __name__ == "__main__":
    ak = "8ENLOAE2QCECKCRKANEU"
    sk = "vddTKjKuG8hcNGOb1cYv3jZ03RLlkOFEEhEHphl8"

    credentials = BasicCredentials(ak, sk)

    client = FunctionGraphClient.new_builder() \
        .with_credentials(credentials) \
        .with_region(FunctionGraphRegion.value_of("la-south-2")) \
        .build()

    try:
        request = InvokeFunctionRequest()
        request.function_urn = "urn:fss:la-south-2:0371a9a7f90b493fadebbf130f6fcd2c:function:default:fg-py-hello-world:latest"
        listInvokeFunctionRequestBodybody = {
            "queryString": "{\"name\":\"Alejandro\"}"
        }
        request.body = listInvokeFunctionRequestBodybody
        response = client.invoke_function(request)
        
        # 1. Obtener toda la información de la respuesta (método principal)
        print("=== RESPONSE COMPLETA ===")
        print(response)
        
        # 2. Acceder al resultado específico de la función (esto es lo que necesitas)
        print("\n=== RESULTADO DE LA FUNCIÓN ===")
        if hasattr(response, 'result'):
            print(f"Result: {response.result}")
        
        # 3. Obtener los headers de la respuesta
        print("\n=== HEADERS ===")
        # En v2, los headers se acceden a través de http_info
        if hasattr(response, 'http_info'):
            http_info = response.http_info
            if hasattr(http_info, 'header_params'):
                headers = http_info.header_params
                for header, value in headers.items():
                    print(f"{header}: {value}")
        
        # 4. Obtener el cuerpo completo de la respuesta HTTP (raw)
        print("\n=== BODY COMPLETO (RAW) ===")
        if hasattr(response, 'http_info'):
            http_info = response.http_info
            if hasattr(http_info, 'body'):
                body_content = http_info.body
                try:
                    # Intentar parsear como JSON si es posible
                    if isinstance(body_content, (str, bytes)):
                        parsed_json = json.loads(body_content if isinstance(body_content, str) else body_content.decode('utf-8'))
                        print(json.dumps(parsed_json, indent=2, ensure_ascii=False))
                    else:
                        print(body_content)
                except (json.JSONDecodeError, UnicodeDecodeError):
                    print(body_content)
        
        # 5. Método alternativo: convertir respuesta a diccionario
        print("\n=== CONVERSIÓN A DICCIONARIO ===")
        try:
            # Algunas respuestas tienen método to_dict()
            if hasattr(response, 'to_dict'):
                response_dict = response.to_dict()
                print(json.dumps(response_dict, indent=2, ensure_ascii=False))
            else:
                # Intentar con __dict__
                response_dict = response.__dict__
                # Filtrar para mostrar solo lo importante
                filtered_dict = {k: v for k, v in response_dict.items() if not k.startswith('_')}
                print(json.dumps(filtered_dict, indent=2, ensure_ascii=False, default=str))
        except Exception as e:
            print(f"No se pudo convertir a diccionario: {e}")
            
        # 6. Información específica de FunctionGraph v2
        print("\n=== INFORMACIÓN ESPECÍFICA V2 ===")
        
        # Intentar acceder a atributos comunes de InvokeFunctionResponse
        attributes_to_check = ['result', 'error', 'logs', 'request_id', 'status_code']
        for attr in attributes_to_check:
            if hasattr(response, attr):
                value = getattr(response, attr)
                if value is not None:
                    print(f"{attr}: {value}")
        
        # 7. Si la función devuelve un resultado JSON, parsearlo
        if hasattr(response, 'result') and response.result:
            print("\n=== RESULTADO PARSEADO ===")
            try:
                result_json = json.loads(response.result)
                print(json.dumps(result_json, indent=2, ensure_ascii=False))
            except json.JSONDecodeError:
                print(f"Result (no JSON): {response.result}")
                
    except exceptions.ClientRequestException as e:
        print("=== ERROR ===")
        print(f"Status Code: {e.status_code}")
        print(f"Request ID: {e.request_id}")
        print(f"Error Code: {e.error_code}")
        print(f"Error Message: {e.error_msg}")
        # En v2, el error puede tener más detalles
        if hasattr(e, 'error_details'):
            print(f"Error Details: {e.error_details}")