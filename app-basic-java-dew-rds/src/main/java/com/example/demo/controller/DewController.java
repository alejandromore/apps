package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.DewSecretsLoader;

@RestController
public class DewController {
    
    @GetMapping("/dew-info")
    public String getDewInfo() {
        StringBuilder response = new StringBuilder();
        
        response.append("<h1>Huawei Cloud DEW Information</h1>");
        
        if (DewSecretsLoader.areSecretsLoaded()) {
            response.append("<p style='color: green; font-weight: bold;'>✅ DEW SECRETS CARGADOS</p>");
            response.append("<h3>Configuración obtenida:</h3>");
            response.append("<table border='1' style='border-collapse: collapse;'>");
            response.append("<tr><th>Parámetro</th><th>Valor</th></tr>");
            
            String url = DewSecretsLoader.getDbUrl();
            String user = DewSecretsLoader.getDbUsername();
            String schema = DewSecretsLoader.getDbSchema();
            
            response.append("<tr><td>Database URL</td><td>").append(maskSensitive(url, "//", "@")).append("</td></tr>");
            response.append("<tr><td>Username</td><td>").append(user).append("</td></tr>");
            response.append("<tr><td>Schema</td><td>").append(schema).append("</td></tr>");
            response.append("<tr><td>Password</td><td>").append("****** (oculto)").append("</td></tr>");
            response.append("</table>");
            
            response.append("<br><p>🎯 <strong>ESTADO:</strong> Conectado exitosamente a Huawei Cloud DEW</p>");
        } else {
            response.append("<p style='color: orange; font-weight: bold;'>⚠️ DEW NO ACTIVO</p>");
            response.append("<p>Usando configuración local desde application.properties</p>");
        }
        
        return response.toString();
    }
    
    @GetMapping("/dew-ping")
    public String dewPing() {
        return DewSecretsLoader.areSecretsLoaded() ? 
               "DEW_ACTIVE:true|SECRETS_LOADED:true|SOURCE:HUAWEI_CLOUD" :
               "DEW_ACTIVE:false|SECRETS_LOADED:false|SOURCE:LOCAL_CONFIG";
    }
    
    private String maskSensitive(String text, String startDelimiter, String endDelimiter) {
        if (text == null) return "null";
        
        int startIdx = text.indexOf(startDelimiter);
        int endIdx = text.indexOf(endDelimiter, startIdx + startDelimiter.length());
        
        if (startIdx != -1 && endIdx != -1) {
            String prefix = text.substring(0, startIdx + startDelimiter.length());
            String suffix = text.substring(endIdx);
            return prefix + "******" + suffix;
        }
        return text;
    }
}