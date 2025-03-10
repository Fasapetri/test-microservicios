package com.example.plazoleta.infraestructure.output.jpa.adapter;

import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.domain.model.Pedido;
import com.example.plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.plazoleta.infraestructure.output.jpa.entity.PedidoEntity;
import com.example.plazoleta.infraestructure.output.jpa.mapper.PedidoEntityMapper;
import com.example.plazoleta.infraestructure.output.jpa.repository.IPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PedidoJpaAdapter implements IPedidoPersistencePort {

    private final IPedidoRepository pedidoRepository;
    private final PedidoEntityMapper pedidoEntityMapper;

    @Override
    public Pedido savePedido(Pedido pedidoToCreate) {
        PedidoEntity mapperPedidoEntity = pedidoEntityMapper.toPedidoEntity(pedidoToCreate);
        return pedidoEntityMapper.toPedido(pedidoRepository.save(mapperPedidoEntity));
    }

    @Override
    public List<Pedido> findAllPedidos() {
        return pedidoEntityMapper.toListPedido(pedidoRepository.findAll());
    }

    @Override
    public Pedido findByIdPedido(Long findPedidoId) {
        return pedidoEntityMapper.toPedido(pedidoRepository.findById(findPedidoId).orElse(null));
    }

    @Override
    public Pedido updateStatusPedido(Pedido pedidoToUpdateStatus) {
        PedidoEntity mapperPedidoEntity = pedidoEntityMapper.toPedidoEntity(pedidoToUpdateStatus);
        return pedidoEntityMapper.toPedido(pedidoRepository.save(mapperPedidoEntity));
    }

    @Override
    public Pedido findByClienteIdAndEstadoIn(Long clientId, EstadoPedido[] estadoPedido) {
        return pedidoEntityMapper.toPedido(pedidoRepository.findByClienteIdAndEstadoIn(clientId,estadoPedido));
    }

    @Override
    public Page<Pedido> findByEstadoAndRestaurantId(EstadoPedido estadoPedido, Long restaurantId, Pageable pageable) {
        return pedidoRepository.findByEstadoAndRestaurantId(estadoPedido, restaurantId, pageable).map(pedidoEntityMapper::toPedido);
    }
}
