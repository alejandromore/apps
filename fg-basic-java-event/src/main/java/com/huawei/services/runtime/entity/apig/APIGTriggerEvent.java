package com.huawei.services.runtime.entity.apig;

import java.util.Map;

public class APIGTriggerEvent {
    private Map<String, String> queryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> headers;
    private String body;
    
    // Getters y Setters
    public Map<String, String> getQueryStringParameters() {
        return queryStringParameters;
    }
    
    public void setQueryStringParameters(Map<String, String> queryStringParameters) {
        this.queryStringParameters = queryStringParameters;
    }
    
    public Map<String, String> getPathParameters() {
        return pathParameters;
    }
    
    public void setPathParameters(Map<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}