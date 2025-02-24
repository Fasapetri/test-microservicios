package com.example.pedidos.domain.spi;

import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IPedidoPersistencePort {

    Mono<Pedido> savePedido(Pedido pedido);

    Flux<Pedido> findAllPedidos();

    Mono<Pedido> findByIdPedido(String id);

    Flux<Pedido> findByStatusPedido(EstadoPedido estadoPedido, Long restaurantId, int pagina, int cantidadPorPagina);

    Mono<Pedido> updateStatusPedido(Pedido pedidoToUpdateStatus);

    Mono<Pedido> updateStatusEntregadoPedido(Pedido pedidoToUpdateStatusEntregado);

    Mono<Pedido> canceledPedido(Pedido pedidoToUpdate);

    Mono<Pedido> findByClienteIdAndEstadoIn(Long clientId, EstadoPedido[] estadoPedido);
}
