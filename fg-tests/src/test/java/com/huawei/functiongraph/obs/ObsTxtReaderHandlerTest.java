package com.huawei.functiongraph.obs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

public class ObsTxtReaderHandlerTest {
    
    private ObsTxtReaderHandler handler;
    private Map<String, Object> testEvent;
    
    @BeforeEach
    public void setUp() {
        handler = new ObsTxtReaderHandler();
        
        // Crear evento de prueba
        testEvent = new HashMap<>();
        testEvent.put("bucket", "test-bucket");
        testEvent.put("object", "test-file.txt");
    }
    
    @Test
    @DisplayName("Test: Handler con evento válido")
    public void testHandlerWithValidEvent() throws JsonProcessingException {
        // Configurar variables de entorno simuladas
        try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
            mockedSystem.when(() -> System.getenv("HUAWEI_AK")).thenReturn("test-ak");
            mockedSystem.when(() -> System.getenv("HUAWEI_SK")).thenReturn("test-sk");
            mockedSystem.when(() -> System.getenv("OBS_BUCKET")).thenReturn("test-bucket");
            mockedSystem.when(() -> System.getenv("OBS_REGION")).thenReturn("test-region");
            
            // Ejecutar handler
            Map<String, Object> response = handler.handler(testEvent);
            
            // Verificar respuesta
            assertNotNull(response);
            assertTrue(response.containsKey("statusCode"));
            assertTrue(response.containsKey("body"));
            
            // En este caso, el handler intentará conectar a OBS real
            // Podría fallar si no hay credenciales reales
            // Esto es solo para verificar que el método se ejecuta
        }
    }
    
    @Test
    @DisplayName("Test: Handler sin credenciales")
    public void testHandlerWithoutCredentials() throws JsonProcessingException {
        // Simular variables de entorno vacías
        try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
            mockedSystem.when(() -> System.getenv(anyString())).thenReturn(null);
            
            // Ejecutar handler
            Map<String, Object> response = handler.handler(testEvent);
            
            // Debería retornar error 500
            assertEquals(500, response.get("statusCode"));
            
            String body = (String) response.get("body");
            assertTrue(body.contains("ERROR"));
            assertTrue(body.contains("Credenciales"));
        }
    }
    
    @Test
    @DisplayName("Test: Evento con formato Records (OBS trigger)")
    public void testHandlerWithRecordsFormat() throws JsonProcessingException {
        // Crear evento en formato OBS trigger
        Map<String, Object> obsEvent = new HashMap<>();
        Map<String, Object> obs = new HashMap<>();
        obs.put("bucket", "obs-bucket");
        obs.put("object", "file.txt");
        
        Map<String, Object> records = new HashMap<>();
        records.put("obs", obs);
        obsEvent.put("Records", records);
        
        try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
            mockedSystem.when(() -> System.getenv("HUAWEI_AK")).thenReturn("test-ak");
            mockedSystem.when(() -> System.getenv("HUAWEI_SK")).thenReturn("test-sk");
            
            // Ejecutar handler
            Map<String, Object> response = handler.handler(obsEvent);
            
            assertNotNull(response);
            // El handler intentará procesar el evento
        }
    }
    
    @Test
    @DisplayName("Test: Handler con valores por defecto")
    public void testHandlerWithDefaultValues() throws JsonProcessingException {
        // Evento vacío debería usar valores por defecto
        Map<String, Object> emptyEvent = new HashMap<>();
        
        try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
            mockedSystem.when(() -> System.getenv("HUAWEI_AK")).thenReturn("test-ak");
            mockedSystem.when(() -> System.getenv("HUAWEI_SK")).thenReturn("test-sk");
            mockedSystem.when(() -> System.getenv("OBS_BUCKET")).thenReturn("default-bucket");
            mockedSystem.when(() -> System.getenv("OBS_REGION")).thenReturn("default-region");
            
            Map<String, Object> response = handler.handler(emptyEvent);
            
            assertNotNull(response);
            // Usará bucket y archivo por defecto
        }
    }
    
    @Test
    @DisplayName("Test: handleRequest con String event")
    public void testHandleRequestWithStringEvent() {
        String jsonEvent = "{\"bucket\": \"string-bucket\", \"object\": \"string-file.txt\"}";
        
        try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
            mockedSystem.when(() -> System.getenv("HUAWEI_AK")).thenReturn("test-ak");
            mockedSystem.when(() -> System.getenv("HUAWEI_SK")).thenReturn("test-sk");
            
            Map<String, Object> response = handler.handleRequest(jsonEvent, null);
            
            assertNotNull(response);
            assertTrue(response.containsKey("statusCode"));
        }
    }
    
    @Test
    @DisplayName("Test: handleRequest con Map event")
    public void testHandleRequestWithMapEvent() {
        Map<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("bucket", "map-bucket");
        mapEvent.put("object", "map-file.txt");
        
        try (MockedStatic<System> mockedSystem = Mockito.mockStatic(System.class)) {
            mockedSystem.when(() -> System.getenv("HUAWEI_AK")).thenReturn("test-ak");
            mockedSystem.when(() -> System.getenv("HUAWEI_SK")).thenReturn("test-sk");
            
            Map<String, Object> response = handler.handleRequest(mapEvent, null);
            
            assertNotNull(response);
            assertTrue(response.containsKey("statusCode"));
        }
    }
    
    @Test
    @DisplayName("Test: handleRequest con tipo de evento inválido")
    public void testHandleRequestWithInvalidEventType() {
        // Pasar un tipo de evento no soportado
        Integer invalidEvent = 123;
        
        Map<String, Object> response = handler.handleRequest(invalidEvent, null);
        
        assertEquals(500, response.get("statusCode"));
        String body = (String) response.get("body");
        assertTrue(body.contains("error"));
    }
}