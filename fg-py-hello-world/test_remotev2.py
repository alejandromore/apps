# coding: utf-8

import os
from huaweicloudsdkcore.auth.credentials import BasicCredentials
from huaweicloudsdkfunctiongraph.v2.region.functiongraph_region import FunctionGraphRegion
from huaweicloudsdkcore.exceptions import exceptions
from huaweicloudsdkfunctiongraph.v2 import *

if __name__ == "__main__":
    # The AK and SK used for authentication are hard-coded or stored in plaintext, which has great security risks. It is recommended that the AK and SK be stored in ciphertext in configuration files or environment variables and decrypted during use to ensure security.
    # In this example, AK and SK are stored in environment variables for authentication. Before running this example, set environment variables CLOUD_SDK_AK and CLOUD_SDK_SK in the local environment
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
        print(response)

        print("\n=== RESULTADO DE LA FUNCIÓN ===")
        if hasattr(response, 'result'):
            print(f"Result: {response.result}")
    except exceptions.ClientRequestException as e:
        print(e.status_code)
        print(e.request_id)
        print(e.error_code)
        print(e.error_msg)