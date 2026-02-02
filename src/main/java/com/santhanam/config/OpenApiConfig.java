package com.santhanam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for the application.
*/
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Organization Group Service API")
                        .version("1.0.0")
                        .description("API documentation for Organization Group Service by Santhanam. This service manages groups, hierarchy, and user memberships as per the Organizations v1.2 assessment requirements."));
    }
}