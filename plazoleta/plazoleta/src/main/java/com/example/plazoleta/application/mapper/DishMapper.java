package com.example.plazoleta.application.mapper;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.application.dto.RestaurantResponse;
import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DishMapper {

    DishMapper INSTANCE = Mappers.getMapper(DishMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id_restaurant", target = "restaurant", qualifiedByName = "mapIdToRestaurant")
    Dish toDish(DishRequest dishRequest);

    @Named("mapIdToRestaurant")
    default Restaurant mapIdToRestaurant(Long id){
        if(id == null){
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        return restaurant;
    }

    DishResponse toDishResponse(Dish dish);

    List<Dish> toListDish(List<DishRequest> dishRequestList);

    List<DishResponse> toListDishResponse(List<Dish> dishList);

}
