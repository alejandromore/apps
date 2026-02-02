import json
from huaweicloudsdkcore.auth.credentials import BasicCredentials
from huaweicloudsdkcore.exceptions import exceptions
from huaweicloudsdkfunctiongraph.v2 import FunctionGraphClient
from huaweicloudsdkfunctiongraph.v2.model.invoke_function_request import InvokeFunctionRequest
from huaweicloudsdkfunctiongraph.v2.region.functiongraph_region import FunctionGraphRegion

# --- Credenciales ---
ak = "8ENLOAE2QCECKCRKANEU"
sk = "vddTKjKuG8hcNGOb1cYv3jZ03RLlkOFEEhEHphl8"
project_id = "0371a9a7f90b493fadebbf130f6fcd2c"
region = "la-south-2"
function_urn = "urn:fss:la-south-2:0371a9a7f90b493fadebbf130f6fcd2c:function:default:fg-py-hello-world:latest"

credentials = BasicCredentials(ak, sk, project_id)

# --- Crear cliente ---
client = FunctionGraphClient.new_builder() \
    .with_credentials(credentials) \
    .with_region(FunctionGraphRegion.value_of(region)) \
    .build()

# --- Payload ---
payload = {"queryString": {"name": "Alejandro"}}

# --- Escapar body igual que en la consola ---
body_str = json.dumps(payload)
body_escaped = json.dumps(body_str)   # ✨ clave: doble escape de JSON

# --- Crear request ---
request = InvokeFunctionRequest(
    function_urn=function_urn,
    body=body_escaped
)

# --- Invocar función ---
try:
    response = client.invoke_function(request)
    
    if response.result:
        # el response.result viene como string JSON
        top_level = json.loads(response.result)
        inner_body = json.loads(top_level.get("body", "{}"))
    else:
        top_level = {}
        inner_body = {}

    print("✅ Response StatusCode:", top_level.get("statusCode"))
    print("✅ Response Headers:", json.dumps(top_level.get("headers", {}), indent=4))
    print("✅ Response Body:", json.dumps(inner_body, indent=4))

except exceptions.ClientRequestException as e:
    print("❌ ClientRequestException:", e)
except Exception as e:
    print("❌ Otro error:", e)
