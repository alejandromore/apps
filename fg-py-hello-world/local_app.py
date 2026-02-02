from fastapi import FastAPI, Request
from index import handler

app = FastAPI()

@app.get("/")
async def root(request: Request):
    event = {
        "queryString": dict(request.query_params),
        "headers": dict(request.headers),
        "body": None
    }

    result = handler(event, context={})
    return result
