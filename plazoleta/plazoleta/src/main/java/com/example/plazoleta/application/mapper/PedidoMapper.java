package com.example.plazoleta.application.mapper;

import com.example.plazoleta.application.dto.PedidoRequest;
import com.example.plazoleta.application.dto.PedidoResponse;
import com.example.plazoleta.domain.model.Pedido;
import com.example.plazoleta.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ItemPedidoMapper.class})
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(source = "restauranteId", target = "restaurant", qualifiedByName = "mapIdToRestaurant")
    Pedido toPedido(PedidoRequest pedidoRequest);

    @Mapping(source = "id", target = "pedidoId")
    @Mapping(source = "restaurant.id", target = "restauranteId")
    @Mapping(source = "pinSeguridad", target = "pinSeguridad")
    PedidoResponse toPedidoResponse(Pedido pedido);

    @Named("mapIdToRestaurant")
    default Restaurant mapIdToRestaurant(Long id){
        if(id == null){
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        return restaurant;
    }
}
