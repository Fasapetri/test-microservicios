package com.example.plazoleta.infraestructure.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "auth-service", url = "http://localhost:8081/api/auth")
public interface AuthClient {

    @GetMapping("/validate")
    Map<String, Object> validateToken(@RequestHeader("Authorization") String token);

    @Data
    @NoArgsConstructor
    class AuthenticateUser {

        private Long id;
        private String email;
        private String rol;

        public AuthenticateUser(Long id, String email, String rol){
            this.id = id;
            this.email = email;
            this.rol = rol;
        }
    }
}
