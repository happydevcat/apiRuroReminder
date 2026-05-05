package com.ai.reminder.ruro.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ruroOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ruro - Apple Reminders Clone API")
                        .description("REST API for managing reminder lists and reminders with smart views")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ruro")
                                .email("axiosoft.ruro@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("H2 Console")
                        .url("http://localhost:8033/h2-console"));
    }
}
