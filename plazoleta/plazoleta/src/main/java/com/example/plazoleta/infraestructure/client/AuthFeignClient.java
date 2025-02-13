package com.example.plazoleta.infraestructure.client;

import com.example.plazoleta.application.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", url = "http://localhost:8081/api/auth")
public interface AuthFeignClient {

    @GetMapping("/validate")
    TokenValidationResponse validateToken(@RequestHeader("Authorization") String token);
}
