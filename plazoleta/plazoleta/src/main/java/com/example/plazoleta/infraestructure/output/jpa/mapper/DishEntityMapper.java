package com.example.plazoleta.infraestructure.output.jpa.mapper;

import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.infraestructure.output.jpa.entity.DishEntity;
import com.example.plazoleta.infraestructure.output.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DishEntityMapper {

    DishEntityMapper INSTANCE = Mappers.getMapper(DishEntityMapper.class);

    @Mapping(source = "restaurant", target = "restaurant", qualifiedByName = "mapRestaurant")
    Dish toDish(DishEntity dishEntity);

    @Mapping(source = "restaurant", target = "restaurant")
    DishEntity toDishEntity(Dish dish);

    List<Dish> toListDish(List<DishEntity> dishEntityList);

    List<DishEntity> toListDishEntity(List<Dish> dishList);

    @Named("mapRestaurant")
    default Restaurant mapRestaurant(RestaurantEntity restaurantEntity) {
        if (restaurantEntity == null) {
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantEntity.getId());
        restaurant.setId_proprietary(restaurantEntity.getProprietaryId());
        return restaurant;
    }
}
