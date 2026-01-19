package com.huawei.functiongraph.obs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.ObsObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ObsTxtReaderHandler {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static ObsClient obsClient = null;
    
    /**
     * Handler principal para Huawei FunctionGraph
     * Formato requerido: public Map<String, Object> handler(Map<String, Object> event)
     * @throws JsonProcessingException 
     */
    public Map<String, Object> handler(Map<String, Object> event) throws JsonProcessingException {
        System.out.println("[FunctionGraph] Iniciando ObsTxtReaderHandler");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Log del evento recibido
            System.out.println("📨 Evento recibido: " + MAPPER.writeValueAsString(event));
            
            // Extraer parámetros del evento (formato FunctionGraph)
            String bucketName = extractValue(event, "bucket", "OBS_BUCKET", "obs-test-alejandro");
            String fileName = extractValue(event, "object", "OBS_FILE", "sample.txt");
            
            // También soportar el formato de evento OBS de FunctionGraph
            if (event.containsKey("Records")) {
                Map<String, Object> records = (Map<String, Object>) event.get("Records");
                if (records.containsKey("obs")) {
                    Map<String, Object> obs = (Map<String, Object>) records.get("obs");
                    bucketName = (String) obs.getOrDefault("bucket", bucketName);
                    fileName = (String) obs.getOrDefault("object", fileName);
                }
            }
            
            System.out.println("🔍 Configuración:");
            System.out.println("   Bucket: " + bucketName);
            System.out.println("   Archivo: " + fileName);
            
            // Obtener credenciales de variables de entorno (FunctionGraph las inyecta)
            String ak = System.getenv("HUAWEI_AK");
            String sk = System.getenv("HUAWEI_SK");
            String region = System.getenv("OBS_REGION");
            
            if (ak == null || sk == null) {
                throw new RuntimeException("Credenciales HUAWEI_AK y HUAWEI_SK deben configurarse en variables de entorno de FunctionGraph");
            }
            
            if (region == null) {
                region = "la-south-2"; // Región por defecto
            }
            
            // Inicializar cliente OBS (singleton para reutilizar conexiones)
            synchronized (ObsTxtReaderHandler.class) {
                if (obsClient == null) {
                    ObsConfiguration config = new ObsConfiguration();
                    config.setEndPoint("obs." + region + ".myhuaweicloud.com");
                    config.setSocketTimeout(30000);
                    config.setConnectionTimeout(10000);
                    config.setMaxIdleConnections(10);
                    
                    obsClient = new ObsClient(ak, sk, config);
                    System.out.println("✅ Cliente OBS inicializado para región: " + region);
                }
            }
            
            // Leer archivo del OBS
            System.out.println("📖 Leyendo archivo desde OBS: " + bucketName + "/" + fileName);
            ObsObject obsObject = obsClient.getObject(bucketName, fileName);
            
            if (obsObject == null) {
                throw new RuntimeException("Archivo no encontrado en OBS: " + fileName);
            }
            
            // Procesar contenido
            StringBuilder content = new StringBuilder();
            int lineCount = 0;
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(obsObject.getObjectContent(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                    lineCount++;
                }
            }
            
            String fileContent = content.toString();
            
            // Crear resultado exitoso
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("code", 200);
            result.put("message", "Archivo leído exitosamente");
            result.put("bucket", bucketName);
            result.put("fileName", fileName);
            result.put("contentLength", fileContent.length());
            result.put("lines", lineCount);
            result.put("timestamp", System.currentTimeMillis());
            
            // Incluir contenido (truncado si es muy largo para FunctionGraph)
            if (fileContent.length() <= 1000) {
                result.put("content", fileContent);
            } else {
                result.put("content", fileContent.substring(0, 1000) + "... [TRUNCADO]");
                result.put("fullContentLength", fileContent.length());
                result.put("truncated", true);
            }
            
            // Respuesta para FunctionGraph
            response.put("statusCode", 200);
            response.put("body", MAPPER.writeValueAsString(result));
            response.put("headers", createHeaders());
            
            System.out.println("✅ Handler completado exitosamente");
            System.out.println("📊 Líneas procesadas: " + lineCount);
            
        } catch (Exception e) {
            System.err.println("❌ Error en handler: " + e.getMessage());
            
            // Crear respuesta de error para FunctionGraph
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("code", 500);
            error.put("message", e.getMessage());
            error.put("timestamp", System.currentTimeMillis());
            
            response.put("statusCode", 500);
            response.put("body", MAPPER.writeValueAsString(error));
            response.put("headers", createHeaders());
            
            // Log detallado para debugging
            e.printStackTrace();
        }
        
        return response;
    }
    
    /**
     * Extrae valores del evento o variables de entorno
     */
    private String extractValue(Map<String, Object> event, String eventKey, String envKey, String defaultValue) {
        // 1. Buscar en el evento
        if (event != null && event.containsKey(eventKey)) {
            Object value = event.get(eventKey);
            return value != null ? value.toString() : defaultValue;
        }
        
        // 2. Buscar en variables de entorno
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }
        
        // 3. Usar valor por defecto
        return defaultValue;
    }
    
    /**
     * Crea headers HTTP para la respuesta
     */
    private Map<String, String> createHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Powered-By", "Huawei-FunctionGraph");
        headers.put("X-Handler", "ObsTxtReaderHandler");
        return headers;
    }
    
    /**
     * Método alternativo para compatibilidad con versiones anteriores
     */
    public Map<String, Object> handleRequest(Object event, Object context) {
        try {
            // Convertir event a Map si es necesario
            if (event instanceof String) {
                return handler(MAPPER.readValue((String) event, Map.class));
            } else if (event instanceof Map) {
                return handler((Map<String, Object>) event);
            } else {
                throw new IllegalArgumentException("Tipo de evento no soportado");
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("body", "{\"error\":\"" + e.getMessage() + "\"}");
            return errorResponse;
        }
    }
}