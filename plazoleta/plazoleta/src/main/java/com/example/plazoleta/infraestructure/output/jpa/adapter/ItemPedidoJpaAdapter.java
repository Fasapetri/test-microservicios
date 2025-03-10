package com.example.plazoleta.infraestructure.output.jpa.adapter;

import com.example.plazoleta.domain.model.ItemPedido;
import com.example.plazoleta.domain.spi.IItemPedidoPersistencePort;
import com.example.plazoleta.infraestructure.output.jpa.entity.ItemPedidoEntity;
import com.example.plazoleta.infraestructure.output.jpa.mapper.ItemPedidoEntityMapper;
import com.example.plazoleta.infraestructure.output.jpa.repository.IItemPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemPedidoJpaAdapter implements IItemPedidoPersistencePort {

    private final ItemPedidoEntityMapper itemPedidoEntityMapper;
    private final IItemPedidoRepository itemPedidoRepository;

    @Override
    public void saveAll(List<ItemPedido> itemsPedido) {
        List<ItemPedidoEntity> mapperEntity = itemPedidoEntityMapper.toListItemPedidoEntity(itemsPedido);
        itemPedidoRepository.saveAll(mapperEntity);
    }

    @Override
    public List<ItemPedido> findByPedidoId(Long findPedidoId) {
        return itemPedidoEntityMapper.toListItemPedido(itemPedidoRepository.findByPedidoId(findPedidoId));
    }
}
