package com.example.users.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de usuario")
                        .version("1.0.0")
                        .description("Documentacion de la API para el consumo y gestion de usuarios en la plataforma"));
    }
}
