package com.example.plazoleta.infraestructure.output.jpa.mapper;

import com.example.plazoleta.domain.model.ItemPedido;
import com.example.plazoleta.infraestructure.output.jpa.entity.ItemPedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PedidoEntityMapper.class, DishEntityMapper.class})
public interface ItemPedidoEntityMapper {

    ItemPedidoEntityMapper INSTANCE = Mappers.getMapper(ItemPedidoEntityMapper.class);

    @Mapping(source = "pedido", target = "pedido")
    @Mapping(source = "plato", target = "plato")
    ItemPedido toItemPedido(ItemPedidoEntity itemPedidoEntity);

    @Mapping(source = "pedido", target = "pedido")
    @Mapping(source = "plato", target = "plato")
    ItemPedidoEntity toItemPedidoEntity(ItemPedido itemPedido);

    List<ItemPedido> toListItemPedido(List<ItemPedidoEntity> itemPedidoEntityList);
    List<ItemPedidoEntity> toListItemPedidoEntity(List<ItemPedido> itemPedidoList);
}
