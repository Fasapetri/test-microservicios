package com.example.pedidos.repository;

import com.example.pedidos.model.EstadoPedido;
import com.example.pedidos.model.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {

    Optional<Pedido> findByClienteIdAndEstadoIn(Long cliente_id, EstadoPedido[] estado);
    List<Pedido> findByEstadoAndRestauranteId(EstadoPedido estadoPedido, Long restauranteId);
}
