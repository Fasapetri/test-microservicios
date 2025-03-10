package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPedidoPersistencePort {

    Pedido savePedido(Pedido pedido);

    List<Pedido> findAllPedidos();

    Pedido findByIdPedido(Long findPedidoId);

    Pedido updateStatusPedido(Pedido pedidoToUpdateStatus);

    Pedido findByClienteIdAndEstadoIn(Long clientId, EstadoPedido[] estadoPedido);

    Page<Pedido> findByEstadoAndRestaurantId(EstadoPedido estadoPedido, Long restaurantId, Pageable pageable);

}
