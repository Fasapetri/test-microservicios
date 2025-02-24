package com.example.pedidos.infraestructure.output.mongo.mapper;

import com.example.pedidos.domain.model.TrazabilidadPedido;
import com.example.pedidos.infraestructure.output.mongo.entity.TrazabilidadPedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITrazabilidadPedidoMapper {

    ITrazabilidadPedidoMapper INSTANCE = Mappers.getMapper(ITrazabilidadPedidoMapper.class);

    TrazabilidadPedido toTrazabilidadPedido(TrazabilidadPedidoEntity trazabilidadPedidoEntity);

    TrazabilidadPedidoEntity toTrazabilidadPedidoEntity(TrazabilidadPedido trazabilidadPedido);

}
