package com.example.pedidos.domain.api;

import com.example.pedidos.model.Pedido;
import reactor.core.publisher.Mono;

public interface IPedidoServicePort {

    Mono<Pedido>
}
