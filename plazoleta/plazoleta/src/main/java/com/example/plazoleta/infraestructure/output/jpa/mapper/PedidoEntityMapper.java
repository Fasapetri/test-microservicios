package com.example.plazoleta.infraestructure.output.jpa.mapper;

import com.example.plazoleta.domain.model.Pedido;
import com.example.plazoleta.infraestructure.output.jpa.entity.PedidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RestaurantEntityMapper.class, ItemPedidoEntityMapper.class})
public interface PedidoEntityMapper {

    PedidoEntityMapper INSTANCE = Mappers.getMapper(PedidoEntityMapper.class);

    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(target = "items", ignore = true)
    Pedido toPedido(PedidoEntity pedidoEntity);

    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(target = "items", ignore = true)
    PedidoEntity toPedidoEntity(Pedido pedido);

    List<Pedido> toListPedido(List<PedidoEntity> pedidoEntityList);

}
