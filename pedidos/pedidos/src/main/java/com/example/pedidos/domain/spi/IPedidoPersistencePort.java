package com.example.pedidos.domain.spi;

import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPedidoPersistencePort {

    Mono<Pedido> savePedido(Pedido pedido);

    Flux<Pedido> findAllPedidos();

    Mono<Pedido> findByIdPedido(String id);

    Mono<Pedido> updateStatusPedido(Pedido pedidoToUpdateStatus);

    Mono<Pedido> findByClienteIdAndEstadoIn(Long clientId, EstadoPedido[] estadoPedido);
}
