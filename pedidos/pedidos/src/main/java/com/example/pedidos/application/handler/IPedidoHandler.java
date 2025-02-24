package com.example.pedidos.application.handler;

import com.example.pedidos.application.dto.PedidoRequest;
import com.example.pedidos.application.dto.PedidoResponse;
import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPedidoHandler {

    Mono<PedidoResponse> savePedido(PedidoRequest pedidoToCreate);

    Flux<PedidoResponse> findAllPedidos();

    Mono<PedidoResponse> findByIdPedido(String id);

    Flux<PedidoResponse> findByStatusPedido(EstadoPedido findStatusPedido, Long findRestaurantId, int pagina, int cantidadPorPagina);

    Mono<String> updateStatusPedido(String findPedidoId, String newStatusPedido);

    Mono<String> updateStatusEntregadoPedido(String findPedidoId, String smsPinSecurityRetirePedido);

    Mono<String> canceledPedido(String findPedidoId);

    Flux<String> obtainPedidoEfficiency();

    Flux<String> employeeEfficiencyRanking();
}
