package com.example.plazoleta.infraestructure.config;

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
                        .title("Microservicio de plazoleta")
                        .version("1.0.0")
                        .description("Documentacion de la API para el consumo y gestion de restaurantes y platos en la plataforma"));
    }
}
