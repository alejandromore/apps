package com.example;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.example.dto.Trabajador;
import com.huawei.services.runtime.Context;
import com.huawei.services.runtime.entity.apig.APIGTriggerEvent;
import com.huawei.services.runtime.entity.apig.APIGTriggerResponse;

public class HttpFunctionHandler {
    public APIGTriggerResponse handler(APIGTriggerEvent event, Context context) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        Gson gson = new Gson();
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("query", event.getQueryStringParameters());
        response.put("path", event.getPathParameters());
        response.put("header", event.getHeaders());
        response.put("body", gson.fromJson(event.getBody(), Trabajador.class));
        return new APIGTriggerResponse(200, headers, gson.toJson(response) );
    }
    public static void main(String[] args) {
    }
}