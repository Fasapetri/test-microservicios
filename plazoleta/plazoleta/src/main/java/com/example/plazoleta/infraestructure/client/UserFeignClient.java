package com.example.plazoleta.infraestructure.client;

import com.example.plazoleta.application.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/users")
public interface UserFeignClient {

    @GetMapping("/id/{id}")
    UserResponse findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}
