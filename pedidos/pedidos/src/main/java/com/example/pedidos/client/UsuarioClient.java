package com.example.pedidos.client;


import com.example.pedidos.model.ClienteDTO;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UsuarioClient {

    @GetMapping("/api/auth/validate")
    Map<String, Object> validateToken(@RequestHeader("Authorization") String token);

    @GetMapping("/api/users/id/{id}")
    ClienteDTO findById(@PathVariable Long id, @RequestHeader("Authorization") String token);

}
