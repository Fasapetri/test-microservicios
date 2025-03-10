package com.example.pedidos.infraestructure.input.rest;

import com.example.pedidos.application.dto.PedidoRequest;
import com.example.pedidos.application.dto.PedidoResponse;
import com.example.pedidos.application.handler.PedidoHandler;
import com.example.pedidos.domain.model.EstadoPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/pedidos")
@RequiredArgsConstructor
public class PedidoRestController {

    private final PedidoHandler pedidoHandler;

    @PostMapping("/save")
    public Mono<PedidoResponse> crearPedido(@RequestBody PedidoRequest pedidoToCreate){
        return pedidoHandler.savePedido(pedidoToCreate);
    }

    @GetMapping
    public Flux<PedidoResponse> obtenerPedido(){
        return pedidoHandler.findAllPedidos();
    }

    @GetMapping("/list-status")
    public Flux<PedidoResponse> listarPedidosPorEstado(@RequestParam EstadoPedido findStatusPedido, @RequestParam Long findRestaurantId, @RequestParam int pagina, @RequestParam int cantidadPorPagina){
        return pedidoHandler.findByStatusPedido(findStatusPedido, findRestaurantId, pagina, cantidadPorPagina);
    }

    @PutMapping("/edit-status")
    public Mono<String> actualizarEstadoPedido(@RequestParam String findPedidoId, @RequestParam String newStatusPedido){
        return pedidoHandler.updateStatusPedido(findPedidoId, newStatusPedido);

    }

    @PutMapping("/entregar")
    public Mono<String> cambiarEstadoEntregado(@RequestParam String findPedidoId, @RequestParam String smsPinSecurityRetirePedido){
        return pedidoHandler.updateStatusEntregadoPedido(findPedidoId, smsPinSecurityRetirePedido);
    }

    @PutMapping("/{id}/cancelar")
    public Mono<String> cancelarPedido(@PathVariable("id") String findPedidoId){
        return pedidoHandler.canceledPedido(findPedidoId);
    }

    @GetMapping("/efficiency")
    public Flux<String> obtainPedidoEfficiency(){
        return pedidoHandler.obtainPedidoEfficiency();
    }

    @GetMapping("/ranking-employee")
    public Flux<String> employeeEfficiencyRanking(){
        return pedidoHandler.employeeEfficiencyRanking();
    }
}
