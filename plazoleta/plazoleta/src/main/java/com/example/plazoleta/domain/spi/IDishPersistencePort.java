package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.Dish;

import java.util.List;

public interface IDishPersistencePort {


    Dish saveDish(Dish dish);

    Dish updateDish(Dish dish);

    Dish updateDishStatus(Long idDish);

    List<Dish> getAllDish();

    List<Dish> getDishRestaurantCategory(Long idRestaurant, String dishCategory);

    List<Dish> getDishRestaurant(Long idRestaurant);

    Dish findById(Long dishId);
}
