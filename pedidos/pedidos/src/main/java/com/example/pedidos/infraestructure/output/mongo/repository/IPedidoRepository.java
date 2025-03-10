package com.example.pedidos.infraestructure.output.mongo.repository;

import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.infraestructure.output.mongo.entity.PedidoEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IPedidoRepository extends ReactiveMongoRepository<PedidoEntity, String> {

    Mono<PedidoEntity> findByClienteIdAndEstadoIn(Long findClientId, EstadoPedido[] statusActivePedido);
}
