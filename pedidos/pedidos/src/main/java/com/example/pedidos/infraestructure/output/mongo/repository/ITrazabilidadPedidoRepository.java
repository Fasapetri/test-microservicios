package com.example.pedidos.infraestructure.output.mongo.repository;

import com.example.pedidos.infraestructure.output.mongo.entity.TrazabilidadPedidoEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ITrazabilidadPedidoRepository extends ReactiveMongoRepository<TrazabilidadPedidoEntity, String> {

    Flux<TrazabilidadPedidoEntity> findByPedidoId(String pedidoId);

}
