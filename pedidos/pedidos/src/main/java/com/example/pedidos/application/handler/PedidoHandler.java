package com.example.pedidos.application.handler;

import com.example.pedidos.application.dto.PedidoRequest;
import com.example.pedidos.application.dto.PedidoResponse;
import com.example.pedidos.application.mapper.PedidoMapper;
import com.example.pedidos.domain.api.IPedidoServicePort;
import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoHandler implements IPedidoHandler{

    private final IPedidoServicePort iPedidoServicePort;
    private final PedidoMapper pedidoMapper;

    @Override
    public Mono<PedidoResponse> savePedido(PedidoRequest pedidoToCreate) {
        Pedido mapperPedido = pedidoMapper.toPedido(pedidoToCreate);
        return iPedidoServicePort.savePedido(mapperPedido).map(pedidoMapper::toPedidoResponse);
    }

    @Override
    public Flux<PedidoResponse> findAllPedidos() {
        return iPedidoServicePort.findAllPedidos().map(pedidoMapper::toPedidoResponse);
    }

    @Override
    public Mono<PedidoResponse> findByIdPedido(String findPedidoId) {
        return iPedidoServicePort.findByIdPedido(findPedidoId).map(pedidoMapper::toPedidoResponse);
    }

    @Override
    public Flux<PedidoResponse> findByStatusPedido(EstadoPedido findStatusPedido, Long findRestaurantId, int pagina, int cantidadPorPagina) {
        return iPedidoServicePort.findByStatusPedido(findStatusPedido, findRestaurantId, pagina, cantidadPorPagina).map(pedidoMapper::toPedidoResponse);
    }

    @Override
    public Mono<String> updateStatusPedido(String findPedidoId, String newStatusPedido) {
        return iPedidoServicePort.updateStatusPedido(findPedidoId, newStatusPedido);
    }

    @Override
    public Mono<String> updateStatusEntregadoPedido(String findPedidoId, String smsPinSecurityRetirePedido) {
        return iPedidoServicePort.updateStatusEntregadoPedido(findPedidoId, smsPinSecurityRetirePedido);
    }

    @Override
    public Mono<String> canceledPedido(String findPedidoId) {
        return iPedidoServicePort.canceledPedido(findPedidoId);
    }

    @Override
    public Flux<String> obtainPedidoEfficiency() {
        return iPedidoServicePort.obtainPedidoEfficiency();
    }

    @Override
    public Flux<String> employeeEfficiencyRanking() {
        return iPedidoServicePort.employeeEfficiencyRanking();
    }
}
