package com.example.pedidos.domain.api;

import com.example.pedidos.domain.model.TrazabilidadPedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITrazabilidadServicePort {

    Flux<TrazabilidadPedido> listHistoryPedido(String pedidoId);

    Mono<TrazabilidadPedido> saveTrazabilidad(TrazabilidadPedido trazabilidadPedido);

    Flux<TrazabilidadPedido> findByPedidoId(String pedidoId);
}
