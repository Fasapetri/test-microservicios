package com.example.pedidos.application.mapper;


import com.example.pedidos.application.dto.PedidoRequest;
import com.example.pedidos.application.dto.PedidoResponse;
import com.example.pedidos.domain.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedSourcePolicy = ReportingPolicy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(target = "id", ignore = true)
    Pedido toPedido(PedidoRequest pedidoRequest);

    PedidoResponse toPedidoResponse(Pedido pedido);

    List<PedidoResponse> toPedidoResponseList(List<Pedido> pedidos);
}
