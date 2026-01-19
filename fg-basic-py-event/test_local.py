# Copia y pega esto directamente en la terminal Python:
import sys
import json
sys.path.append('.')
from index import handler

class MockContext:
    def __init__(self):
        self.function_name = 'test'
        self.memory_limit_in_mb = 128
        self.request_id = 'test-123'
    def get_remaining_time_in_millis(self):
        return 3000

# Test GET
event = {
    'httpMethod': 'GET',
    'path': '/hello',
    'queryStringParameters': {'name': 'Alejandro More'},
    'headers': {'User-Agent': 'Terminal'}
}

context = MockContext()
result = handler(event, context)
print(f"Status: {result['statusCode']}")
print(f"Body: {result['body']}")