package com.example.pedidos.infraestructure.output.mongo.adapter;

import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.Pedido;
import com.example.pedidos.domain.spi.IPedidoPersistencePort;
import com.example.pedidos.infraestructure.output.mongo.entity.PedidoEntity;
import com.example.pedidos.infraestructure.output.mongo.mapper.IPedidoEntityMapper;
import com.example.pedidos.infraestructure.output.mongo.repository.IPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PedidoMongoAdapter implements IPedidoPersistencePort {

    private final IPedidoRepository pedidoRepository;
    private final IPedidoEntityMapper pedidoEntityMapper;

    @Override
    public Mono<Pedido> savePedido(Pedido pedidoToCreate) {
        PedidoEntity mapperPedidoEntity = pedidoEntityMapper.toPedidoEntity(pedidoToCreate);
        return pedidoRepository.save(mapperPedidoEntity).map(pedidoEntityMapper::toPedido);
    }

    @Override
    public Flux<Pedido> findAllPedidos() {
        return pedidoRepository.findAll().map(pedidoEntityMapper::toPedido);
    }

    @Override
    public Mono<Pedido> findByIdPedido(String findPedidoId) {
        return pedidoRepository.findById(findPedidoId).map(pedidoEntityMapper::toPedido);
    }

    @Override
    public Mono<Pedido> updateStatusPedido(Pedido pedidoToupdateStatus) {
        PedidoEntity mapperPedidoEntity = pedidoEntityMapper.toPedidoEntity(pedidoToupdateStatus);
        return pedidoRepository.save(mapperPedidoEntity).map(pedidoEntityMapper::toPedido);
    }

    @Override
    public Mono<Pedido> findByClienteIdAndEstadoIn(Long clientId, EstadoPedido[] estadoPedido) {
        return pedidoRepository.findByClienteIdAndEstadoIn(clientId, estadoPedido)
                .map(pedidoEntityMapper::toPedido)
                .switchIfEmpty(Mono.empty());
    }

}
