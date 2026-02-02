from index import handler
import json

def test_hello_world():
    event = {
        "queryString": {"name": "Test"},
        "headers": {},
        "body": None
    }

    response = handler(event, context={})
    body = json.loads(response["body"])

    assert response["statusCode"] == 200
    assert body["message"] == "Hello Test from Huawei FunctionGraph!"
