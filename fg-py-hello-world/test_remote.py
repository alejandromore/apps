import json
from huaweicloudsdkcore.auth.credentials import BasicCredentials
from huaweicloudsdkfunctiongraph.v2 import FunctionGraphClient, InvokeFunctionRequest
from huaweicloudsdkfunctiongraph.v2.region.functiongraph_region import FunctionGraphRegion

# Config
ak = "8ENLOAE2QCECKCRKANEU"
sk = "vddTKjKuG8hcNGOb1cYv3jZ03RLlkOFEEhEHphl8"
project_id = "0371a9a7f90b493fadebbf130f6fcd2c"

# Client
client = FunctionGraphClient.new_builder() \
    .with_credentials(BasicCredentials(ak, sk, project_id)) \
    .with_region(FunctionGraphRegion.value_of("la-south-2")) \
    .build()

# Request
request = InvokeFunctionRequest()
request.function_urn = "urn:fss:la-south-2:0371a9a7f90b493fadebbf130f6fcd2c:function:default:fg-py-hello-world:latest"
#request.body = json.dumps({"name": "Alejandro"})
request.body = json.dumps({
    "queryString": json.dumps({"name": "Alejandro"})
})

request.header_params = {
    "X-Cff-Invoke-Type": "Sync"  # Para invocación síncrona
    # "X-Cff-Invoke-Type": "Async"  # Para invocación asíncrona
}

# Execute
response = client.invoke_function(request)

# Print result - en v2 response suele tener atributos diferentes
print("Status:", response.status_code if hasattr(response, 'status_code') else 'N/A')
print("Body:", response.body if hasattr(response, 'body') else 'N/A')
print("Result:", response.result if hasattr(response, 'result') else 'N/A')

# Convertir a dict para ver todo
if hasattr(response, 'to_dict'):
    print("\nTodo:")
    print(json.dumps(response.to_dict(), indent=2))
else:
    print("\nResponse object:", response)