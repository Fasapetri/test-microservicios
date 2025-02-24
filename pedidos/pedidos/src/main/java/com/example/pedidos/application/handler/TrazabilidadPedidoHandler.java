package com.example.pedidos.application.handler;

import com.example.pedidos.application.dto.TrazabilidadPedidoResponse;
import com.example.pedidos.application.mapper.TrazabilidadPedidoMapper;
import com.example.pedidos.domain.api.ITrazabilidadServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Transactional
public class TrazabilidadPedidoHandler implements ITrazabilidadPedidoHandler{

    private final ITrazabilidadServicePort iTrazabilidadServicePort;
    private final TrazabilidadPedidoMapper trazabilidadPedidoMapper;

    @Override
    public Flux<TrazabilidadPedidoResponse> listHistoryPedido(String pedidoId) {
        return iTrazabilidadServicePort.listHistoryPedido(pedidoId).map(trazabilidadPedidoMapper::toTrazabilidadPedidoResponse);
    }
}
