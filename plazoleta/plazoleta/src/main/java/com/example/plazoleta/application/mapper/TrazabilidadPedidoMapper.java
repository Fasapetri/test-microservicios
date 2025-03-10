package com.example.plazoleta.application.mapper;

import com.example.plazoleta.application.dto.TrazabilidadPedidoResponse;
import com.example.plazoleta.domain.model.TrazabilidadPedido;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrazabilidadPedidoMapper {

    TrazabilidadPedidoMapper INSTANCE = Mappers.getMapper(TrazabilidadPedidoMapper.class);

    List<TrazabilidadPedidoResponse> toListTrazabilidadPedidoResponse(List<TrazabilidadPedido> trazabilidadPedidoList);
}
