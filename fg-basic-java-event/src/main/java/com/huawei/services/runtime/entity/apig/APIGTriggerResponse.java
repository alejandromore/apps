package com.huawei.services.runtime.entity.apig;

import java.util.Map;

public class APIGTriggerResponse {
    private int statusCode;
    private Map<String, String> headers;
    private String body;
    
    public APIGTriggerResponse(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public String getBody() {
        return body;
    }
}