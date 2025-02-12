package com.example.pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "plazoleta-service", url = "http://localhost:8082")
public interface RestauranteClient {

    @GetMapping("/api/plazoletas/{id}/existe")
    boolean existeRestaurante(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/api/dishes/{id}/platos")
    Map<String, Object> obtenerPlatosRestaurante(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}
