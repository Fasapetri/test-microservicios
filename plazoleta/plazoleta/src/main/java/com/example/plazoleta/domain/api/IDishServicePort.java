package com.example.plazoleta.domain.api;

import com.example.plazoleta.domain.model.Dish;

import java.util.List;

public interface IDishServicePort {

    Dish saveDish(Dish dish);

    Dish updateDish(Long id_dish, Dish dish);

    Dish updateDishStatus(Long idDish);

    List<Dish> getAllDish();

    List<Dish> getDishRestaurantCategory(Long idRestaurant, String dishCategory);

    Dish findById(Long dishId);
}
