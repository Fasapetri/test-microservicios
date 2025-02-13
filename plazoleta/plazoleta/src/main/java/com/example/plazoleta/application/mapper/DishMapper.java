package com.example.plazoleta.application.mapper;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DishMapper {

    DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);

    @Mapping(target = "id", ignore = true)
    Dish toDish(DishRequest dishRequest);

    DishResponse toDishResponse(Dish dish);

    List<Dish> toListDish(List<DishRequest> dishRequestList);

    List<DishResponse> toListDishResponse(List<Dish> dishList);

}
