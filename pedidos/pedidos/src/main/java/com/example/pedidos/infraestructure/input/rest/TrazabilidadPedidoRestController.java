package com.example.pedidos.infraestructure.input.rest;

import com.example.pedidos.application.dto.TrazabilidadPedidoResponse;
import com.example.pedidos.application.handler.TrazabilidadPedidoHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/trazabilidad")
@RequiredArgsConstructor
public class TrazabilidadPedidoRestController {

    private final TrazabilidadPedidoHandler trazabilidadPedidoHandler;

    @GetMapping("/history-pedido")
    public Flux<TrazabilidadPedidoResponse> obtenerHistorialPedido(@RequestParam String findPedidoId){
        return trazabilidadPedidoHandler.listHistoryPedido(findPedidoId);
    }
}
