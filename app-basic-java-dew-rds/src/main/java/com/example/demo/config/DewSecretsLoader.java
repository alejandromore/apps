package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.csms.v1.CsmsClient;
import com.huaweicloud.sdk.csms.v1.model.ShowSecretVersionRequest;
import com.huaweicloud.sdk.csms.v1.model.ShowSecretVersionResponse;
import com.huaweicloud.sdk.csms.v1.region.CsmsRegion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class DewSecretsLoader {

    @Value("${huaweicloud.dew.enabled}")
    private boolean dewEnabled;

    @Value("${huaweicloud.dew.secret-name}")
    private String secretName;

    @Value("${huaweicloud.dew.region}")
    private String region;

    @Value("${huaweicloud.auth.ak}")
    private String accessKey;

    @Value("${huaweicloud.auth.sk}")
    private String secretKey;

    @Value("${huaweicloud.auth.project-id}")
    private String projectId;

    // Variables estáticas
    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String DB_SCHEMA;

    public DewSecretsLoader() {
        System.out.println("DEBUG: Constructor ejecutado");
    }

    @PostConstruct
    public void loadSecrets() {
        System.out.println("DEBUG: Inicializando secrets..."); // ← Breakpoint aquí

        if (!dewEnabled) {
            System.out.println("DEW deshabilitado, usando configuración local");
            return;
        }

        System.out.println("🚀 Iniciando carga de secrets desde Huawei Cloud DEW...");
        System.out.println("   Región: " + region);
        System.out.println("   Secret Name: " + secretName);
        System.out.println("   Project ID: " + projectId);

        try {
            // 1. Configurar credenciales
            ICredential auth = new BasicCredentials()
                    .withAk(accessKey)
                    .withSk(secretKey)
                    .withProjectId(projectId);

            // 2. Crear cliente DEW
            CsmsClient csmsClient = CsmsClient.newBuilder()
                    .withCredential(auth)
                    .withRegion(CsmsRegion.valueOf(region))
                    .build();

            // 3. Preparar y ejecutar la solicitud
            ShowSecretVersionRequest request = new ShowSecretVersionRequest()
                .withSecretName(secretName)
                .withVersionId("latest");  // Usar "latest" para la última versión

            ShowSecretVersionResponse response = csmsClient.showSecretVersion(request);

            // 4. Procesar la respuesta
            if (response.getVersion() != null) {
                // Primero intentar con secretString (texto plano)
                String secretContent = response.getVersion().getSecretString();
                
                if (secretContent == null || secretContent.isEmpty()) {
                    // Si no hay secretString, intentar con secretBinary (base64)
                    String secretBase64 = response.getVersion().getSecretBinary();
                    if (secretBase64 != null && !secretBase64.isEmpty()) {
                        byte[] decodedBytes = Base64.getDecoder().decode(secretBase64);
                        secretContent = new String(decodedBytes, StandardCharsets.UTF_8);
                    }
                }
                
                if (secretContent != null && !secretContent.isEmpty()) {
                    System.out.println("✅ Secret obtenido exitosamente de DEW");
                    System.out.println("📄 Contenido: " + secretContent);
                    parseSecretsFromJson(secretContent);
                } else {
                    System.out.println("⚠️  El secreto está vacío");
                }
            } else {
                System.out.println("❌ No se pudo obtener la versión del secreto");
            }

        } catch (ServiceResponseException e) {
            System.err.println("❌ Error de servicio Huawei Cloud:");
            System.err.println("   Status Code: " + e.getHttpStatusCode());
            System.err.println("   Error Code: " + e.getErrorCode());
            System.err.println("   Error Msg: " + e.getErrorMsg());
            System.err.println("   Request ID: " + e.getRequestId());
        } catch (ConnectionException e) {
            System.err.println("❌ Error de conexión a Huawei Cloud:");
            System.err.println("   Causa: " + e.getMessage());
        } catch (RequestTimeoutException e) {
            System.err.println("❌ Timeout al conectar con Huawei Cloud:");
            System.err.println("   Causa: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Error en argumentos/región:");
            System.err.println("   Causa: " + e.getMessage());
            System.err.println("   Región especificada: " + region);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado:");
            e.printStackTrace();
        }
    }

    private void parseSecretsFromJson(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        // Parsear JSON
        Map<String, String> secrets = mapper.readValue(jsonString, 
            mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        
        DB_URL = secrets.get("db_url");
        DB_USERNAME = secrets.get("db_username");
        DB_PASSWORD = secrets.get("db_password");
        DB_SCHEMA = secrets.get("db_schema");
        
        if (DB_SCHEMA == null) {
            DB_SCHEMA = "dummy_data";
        }
        
        // Validar que todos los campos requeridos están presentes
        if (DB_URL == null || DB_USERNAME == null || DB_PASSWORD == null) {
            System.err.println("⚠️ Advertencia: Faltan campos requeridos en el secret");
            System.err.println("   Campos encontrados: " + secrets.keySet());
        }
    }
    
    // Getters (mantener igual)
    public static String getDbUrl() {
        return DB_URL;
    }
    
    public static String getDbUsername() {
        return DB_USERNAME;
    }
    
    public static String getDbPassword() {
        return DB_PASSWORD;
    }
    
    public static String getDbSchema() {
        return DB_SCHEMA;
    }
    
    public static boolean areSecretsLoaded() {
        return DB_URL != null && !DB_URL.isEmpty();
    }
}