package com.example.plazoleta.infraestructure.client;

import com.example.plazoleta.domain.model.TrazabilidadPedido;
import com.example.plazoleta.infraestructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "trazabilidad", url = "http://localhost:8083/api/trazabilidad", configuration = FeignConfig.class)
public interface TrazabilidadFeignClient {

    @PostMapping("/save")
    void savedTrazabilidad(@RequestBody TrazabilidadPedido trazabilidadPedido);

    @GetMapping("history-pedido")
    List<TrazabilidadPedido> obtenerTrazabilidadPedido(@RequestParam Long findPedidoId);
}
