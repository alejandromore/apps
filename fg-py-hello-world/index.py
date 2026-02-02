import json

# PARA HUAWEI FUNCTIONGRAPH
def handler(event, context):
    print("=== DEBUG EVENT ===")
    print(event)
    print("===================")

    if isinstance(event, str):
        event = json.loads(event)

    query_params = event.get("queryString", {}) or {}
    name = query_params.get("name", "World")

    response_body = {
        "message": f"Hello {name} from Huawei FunctionGraph!"
    }

    return {
        "statusCode": 200,
        "headers": {"Content-Type": "application/json"},
        "body": json.dumps(response_body)
    }