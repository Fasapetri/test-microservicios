package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.ItemPedido;

import java.util.List;

public interface IItemPedidoPersistencePort {

    void saveAll(List<ItemPedido> itemsPedido);

    List<ItemPedido> findByPedidoId(Long findPedidoId);

}
