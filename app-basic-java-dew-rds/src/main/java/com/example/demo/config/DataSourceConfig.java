package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@DependsOn("dewSecretsLoader")
public class DataSourceConfig {
    
    @Value("${spring.datasource.url}")
    private String defaultDbUrl;
    
    @Value("${spring.datasource.username}")
    private String defaultDbUsername;
    
    @Value("${spring.datasource.password}")
    private String defaultDbPassword;
    
    @Primary
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        
        // Usar DEW si está disponible
        if (DewSecretsLoader.areSecretsLoaded()) {
            dataSource.setJdbcUrl(DewSecretsLoader.getDbUrl());
            dataSource.setUsername(DewSecretsLoader.getDbUsername());
            dataSource.setPassword(DewSecretsLoader.getDbPassword());
            
            // Establecer schema desde DEW
            System.setProperty("spring.jpa.properties.hibernate.default_schema", 
                             DewSecretsLoader.getDbSchema());
            
            System.out.println("✅ Usando configuración DEW Secrets");
        } else {
            // Usar configuración local
            dataSource.setJdbcUrl(defaultDbUrl);
            dataSource.setUsername(defaultDbUsername);
            dataSource.setPassword(defaultDbPassword);
            System.out.println("ℹ️ Usando configuración local");
        }
        
        // Configuración común
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);
        dataSource.setConnectionTimeout(30000);
        
        return dataSource;
    }
}