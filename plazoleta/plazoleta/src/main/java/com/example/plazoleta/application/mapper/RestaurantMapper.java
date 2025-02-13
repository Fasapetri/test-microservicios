package com.example.plazoleta.application.mapper;

import com.example.plazoleta.application.dto.RestaurantRequest;
import com.example.plazoleta.application.dto.RestaurantResponse;
import com.example.plazoleta.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(com.example.plazoleta.application.mapper.RestaurantMapper.class);

    @Mapping(target = "id", ignore = true)
    Restaurant toRestaurant(RestaurantRequest restaurantRequest);

    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    List<Restaurant> toListRestaurant(List<RestaurantRequest> restaurantRequestList);

    List<RestaurantResponse> toListRestaurantResponse(List<Restaurant> restaurantList);
}
