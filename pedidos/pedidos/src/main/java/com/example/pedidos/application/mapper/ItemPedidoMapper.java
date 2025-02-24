package com.example.pedidos.application.mapper;

import com.example.pedidos.application.dto.ItemPedidoRequest;
import com.example.pedidos.application.dto.ItemPedidoResponse;
import com.example.pedidos.domain.model.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ItemPedidoMapper {

    ItemPedidoMapper INSTANCE = Mappers.getMapper(ItemPedidoMapper.class);

    ItemPedido toItemPedido(ItemPedidoRequest itemPedidoRequest);

    ItemPedidoResponse toItemPedidoResponse(ItemPedido itemPedido);
}
