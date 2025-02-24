package com.example.pedidos.infraestructure.output.mongo.adapter;

import com.example.pedidos.domain.model.TrazabilidadPedido;
import com.example.pedidos.domain.spi.ITrazabilidadPersistencePort;
import com.example.pedidos.infraestructure.output.mongo.entity.TrazabilidadPedidoEntity;
import com.example.pedidos.infraestructure.output.mongo.mapper.ITrazabilidadPedidoMapper;
import com.example.pedidos.infraestructure.output.mongo.repository.ITrazabilidadPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrazabilidadPedidoMongoAdapter implements ITrazabilidadPersistencePort {

    private final ITrazabilidadPedidoRepository iTrazabilidadPedidoRepository;
    private final ITrazabilidadPedidoMapper iTrazabilidadPedidoMapper;


    @Override
    public Flux<TrazabilidadPedido> listHistoryPedido(String findPedidoId) {
            return findByPedidoId(findPedidoId);
    }

    @Override
    public Mono<TrazabilidadPedido> saveTrazabilidad(TrazabilidadPedido trazabilidadPedidoToCreate) {
        TrazabilidadPedidoEntity mapperTrazabilidadPedidoEntity = iTrazabilidadPedidoMapper.toTrazabilidadPedidoEntity(trazabilidadPedidoToCreate);
        return iTrazabilidadPedidoRepository.save(mapperTrazabilidadPedidoEntity).map(iTrazabilidadPedidoMapper::toTrazabilidadPedido);
    }

    @Override
    public Flux<TrazabilidadPedido> findByPedidoId(String findPedidoId) {
        return iTrazabilidadPedidoRepository.findByPedidoId(findPedidoId).map(iTrazabilidadPedidoMapper::toTrazabilidadPedido);
    }
}
