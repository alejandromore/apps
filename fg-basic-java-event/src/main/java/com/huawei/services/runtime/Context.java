package com.huawei.services.runtime;

import java.util.Map;

public interface Context {
    default String getRequestId() {
        return "local-test-request-id";
    }
    
    default String getFunctionUrn() {
        return "local-test-function-urn";
    }
    
    default Map<String, String> getUserData() {
        return Map.of();
    }
}