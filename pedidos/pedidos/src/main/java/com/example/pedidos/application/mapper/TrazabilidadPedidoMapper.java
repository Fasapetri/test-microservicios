package com.example.pedidos.application.mapper;

import com.example.pedidos.application.dto.TrazabilidadPedidoResponse;
import com.example.pedidos.domain.model.TrazabilidadPedido;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrazabilidadPedidoMapper {

    TrazabilidadPedidoMapper INSTANCE = Mappers.getMapper(TrazabilidadPedidoMapper.class);

    TrazabilidadPedidoResponse toTrazabilidadPedidoResponse(TrazabilidadPedido trazabilidadPedido);
}
