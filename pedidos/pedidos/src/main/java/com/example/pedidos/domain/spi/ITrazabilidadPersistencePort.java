package com.example.pedidos.domain.spi;

import com.example.pedidos.domain.model.TrazabilidadPedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITrazabilidadPersistencePort {

    Mono<TrazabilidadPedido> saveTrazabilidad(TrazabilidadPedido trazabilidadPedido);

    Flux<TrazabilidadPedido> findByPedidoId(String pedidoId);
}
