package com.example.plazoleta.infraestructure.output.jpa.mapper;

import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.infraestructure.output.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {

    RestaurantEntityMapper INSTANCE = Mappers.getMapper(RestaurantEntityMapper.class);

    Restaurant toRestaurant(RestaurantEntity restaurantEntity);

    RestaurantEntity toRestaurantEntity(Restaurant restaurant);

    List<Restaurant> toListRestaurant(List<RestaurantEntity> restaurantEntityList);

    List<RestaurantEntity> toListRestaurantEntity(List<Restaurant> restaurantList);
}
