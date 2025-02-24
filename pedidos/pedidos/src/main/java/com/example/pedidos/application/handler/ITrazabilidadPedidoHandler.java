package com.example.pedidos.application.handler;

import com.example.pedidos.application.dto.TrazabilidadPedidoResponse;
import com.example.pedidos.domain.model.TrazabilidadPedido;
import reactor.core.publisher.Flux;

public interface ITrazabilidadPedidoHandler {

    Flux<TrazabilidadPedidoResponse> listHistoryPedido(String findPedidoId);
}
