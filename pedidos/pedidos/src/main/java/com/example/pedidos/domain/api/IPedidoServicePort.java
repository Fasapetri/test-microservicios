package com.example.pedidos.domain.api;

import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPedidoServicePort {

    Mono<Pedido> savePedido(Pedido pedido);

    Flux<Pedido> findAllPedidos();

    Mono<Pedido> findByIdPedido(String id);

    Flux<Pedido> findByStatusPedido(EstadoPedido estadoPedido, Long restaurantId, int pagina, int cantidadPorPagina);

    Mono<String> updateStatusPedido(String pedidoId, String estadoPedido);

    Mono<String> updateStatusEntregadoPedido(String pedidoId, String pinSeguridad);

    Mono<String> canceledPedido(String pedidoId);

    Flux<String> obtainPedidoEfficiency();

    Flux<String> employeeEfficiencyRanking();
}
