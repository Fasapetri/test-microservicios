package com.example.plazoleta.infraestructure.output.jpa.repository;

import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.infraestructure.output.jpa.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPedidoRepository extends JpaRepository<PedidoEntity, Long> {

    PedidoEntity findByClienteIdAndEstadoIn(Long findClientId, EstadoPedido[] statusActivePedido);

    Page<PedidoEntity> findByEstadoAndRestaurantId(EstadoPedido estadoPedido,
                                                   Long restaurantId,
                                                   Pageable pageable);
}
