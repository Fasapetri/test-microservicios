package com.example.plazoleta.infraestructure.output.jpa.mapper;

import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.infraestructure.output.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DishEntityMapper {

    DishEntityMapper INSTANCE = Mappers.getMapper(DishEntityMapper.class);

    Dish toDish(DishEntity dishEntity);

    DishEntity toDishEntity(Dish dish);

    List<Dish> toListDish(List<DishEntity> dishEntityList);

    List<DishEntity> toListDishEntity(List<Dish> dishList);
}
