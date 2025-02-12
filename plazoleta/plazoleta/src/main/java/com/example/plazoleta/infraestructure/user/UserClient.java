package com.example.plazoleta.infraestructure.user;


import feign.Feign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/users")
public interface UserClient {

    @GetMapping("/id/{id}")
    UserResponse getUserByIdProprietary(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UserResponse {

        private Long id;
        private String name;
        private String email;
        private String rol;

        public UserResponse(Long id, String email, String rol){
            this.id = id;
            this.email = email;
            this.rol = rol;
        }
    }
}
