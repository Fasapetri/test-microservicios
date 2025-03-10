package com.example.pedidos.domain.api;

import com.example.pedidos.domain.model.TrazabilidadPedido;
import reactor.core.publisher.Flux;

public interface ITrazabilidadServicePort {

    Flux<TrazabilidadPedido> listHistoryPedido(String pedidoId);

}
