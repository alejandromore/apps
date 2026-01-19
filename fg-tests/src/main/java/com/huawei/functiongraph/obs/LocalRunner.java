package com.huawei.functiongraph.obs;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LocalRunner {
    
    public static void main(String[] args) {
        System.out.println("=== Huawei FunctionGraph - Local Runner ===");
        
        try {
            // Crear handler
            ObsTxtReaderHandler handler = new ObsTxtReaderHandler();
            
            // Cargar evento de prueba
            Map<String, Object> event = loadTestEvent();
            
            // Configurar variables de entorno si no están
            setupEnvironment();
            
            // Ejecutar handler
            System.out.println("\nEjecutando handler...");
            Map<String, Object> response = handler.handleRequest(event, null);
            
            // Mostrar resultados
            System.out.println("\n=== RESULTADOS ===");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(response);
            System.out.println(json);
            
            // Guardar resultados
            mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("local-result.json"), response);
            System.out.println("\nResultado guardado en: local-result.json");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\nPresiona Enter para salir...");
        new Scanner(System.in).nextLine();
    }
    
    private static Map<String, Object> loadTestEvent() throws Exception {
        File eventFile = new File("test-event.json");
        if (eventFile.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(eventFile, Map.class);
        }
        
        // Evento por defecto
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> obs = new HashMap<>();
        obs.put("bucket", "test-bucket");
        obs.put("object", "test-file.txt");
        obs.put("eventName", "ObjectCreated:Put");
        
        Map<String, Object> records = new HashMap<>();
        records.put("obs", obs);
        event.put("Records", records);
        
        return event;
    }
    
    private static void setupEnvironment() {
        if (System.getenv("HUAWEI_AK") == null) {
            System.out.println("\n⚠️  Credenciales no configuradas");
            System.out.println("Configura las variables de entorno:");
            System.out.println("  HUAWEI_AK=tu_access_key");
            System.out.println("  HUAWEI_SK=tu_secret_key");
            System.out.println("  OBS_ENDPOINT=obs.region.huaweicloud.com");
            System.out.println("  OBS_BUCKET=nombre-bucket");
        }
    }
}