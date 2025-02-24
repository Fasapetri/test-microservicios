package com.example.plazoleta.infraestructure.client;

import com.example.plazoleta.application.dto.UserResponse;
import com.example.plazoleta.infraestructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/users", configuration = FeignConfig.class)
public interface UserFeignClient {

    @GetMapping("/id/{id}")
    UserResponse findById(@PathVariable("id") Long id);
}
