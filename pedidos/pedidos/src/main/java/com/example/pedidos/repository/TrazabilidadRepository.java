package com.example.pedidos.repository;

import com.example.pedidos.model.TrazabilidadPedido;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TrazabilidadRepository extends ReactiveMongoRepository<TrazabilidadPedido, String> {

    Flux<TrazabilidadPedido> findByPedidoId(String pedidoId);
}
