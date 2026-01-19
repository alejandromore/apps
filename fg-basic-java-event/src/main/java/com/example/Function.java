package com.example;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.services.runtime.Context;
import com.huawei.services.runtime.entity.apig.APIGTriggerEvent;
import com.huawei.services.runtime.entity.apig.APIGTriggerResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Function implements HttpHandler {
    
    public APIGTriggerResponse handler(APIGTriggerEvent event, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Gson gson = new Gson();
        Map<String, Object> response = new HashMap<>();
        response.put("query", event.getQueryStringParameters());
        response.put("path", event.getPathParameters());
        response.put("header", event.getHeaders());
        response.put("body", gson.fromJson(event.getBody(), Trabajador.class));
        return new APIGTriggerResponse(200, headers, gson.toJson(response));
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            
            Gson gson = new Gson();
            Map<String, Object> requestData = gson.fromJson(requestBody, 
                new TypeToken<Map<String, Object>>(){}.getType());
            
            APIGTriggerEvent event = new APIGTriggerEvent();
            event.setQueryStringParameters(convertMap(requestData.get("queryStringParameters")));
            event.setPathParameters(convertMap(requestData.get("pathParameters")));
            event.setHeaders(convertMap(requestData.get("headers")));
            event.setBody(requestData.get("body").toString());
            
            Context context = new Context() {};
            APIGTriggerResponse response = handler(event, context);
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(response.getStatusCode(), 
                response.getBody().getBytes().length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBody().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            String error = "{\"error\": \"" + e.getMessage() + "\"}";
            exchange.sendResponseHeaders(500, error.getBytes().length);
            exchange.getResponseBody().write(error.getBytes());
        } finally {
            exchange.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, String> convertMap(Object obj) {
        if (obj == null) return new HashMap<>();
        Map<String, Object> original = (Map<String, Object>) obj;
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }
    
    public static void main(String[] args) throws Exception {
        com.sun.net.httpserver.HttpServer server = 
            com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(8080), 0);
        
        server.createContext("/test", new Function());
        server.start();
        System.out.println("✅ Servidor local iniciado en http://localhost:8080/test");
    }
}