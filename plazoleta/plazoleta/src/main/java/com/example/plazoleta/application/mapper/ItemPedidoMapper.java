package com.example.plazoleta.application.mapper;

import com.example.plazoleta.application.dto.ItemPedidoRequest;
import com.example.plazoleta.application.dto.ItemPedidoResponse;
import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ItemPedidoMapper {

    ItemPedidoMapper INSTANCE = Mappers.getMapper(ItemPedidoMapper.class);

    @Mapping(source = "platoId", target = "plato", qualifiedByName = "mapIdToDish")
    ItemPedido toItemPedido(ItemPedidoRequest itemPedidoRequest);

    @Mapping(source = "plato.id", target = "platoId")
    ItemPedidoResponse toItemPedidoResponse(ItemPedido itemPedido);

    @Named("mapIdToDish")
    default Dish mapIdToDish(Long id){
        if(id == null){
            return null;
        }
        Dish dish = new Dish();
        dish.setId(id);
        return dish;
    }
}
