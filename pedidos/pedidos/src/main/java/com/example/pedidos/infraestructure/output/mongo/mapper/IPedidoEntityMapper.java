package com.example.pedidos.infraestructure.output.mongo.mapper;

import com.example.pedidos.domain.model.Pedido;
import com.example.pedidos.infraestructure.output.mongo.entity.PedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedSourcePolicy = ReportingPolicy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPedidoEntityMapper {

    IPedidoEntityMapper INSTANCE = Mappers.getMapper(IPedidoEntityMapper.class);

    Pedido toPedido(PedidoEntity pedidoEntity);

    PedidoEntity toPedidoEntity(Pedido pedido);

    List<Pedido> toPedidoList(List<PedidoEntity> pedidoEntityList);

    List<PedidoEntity> toPedidoEntityList(List<Pedido> pedidoList);
}
