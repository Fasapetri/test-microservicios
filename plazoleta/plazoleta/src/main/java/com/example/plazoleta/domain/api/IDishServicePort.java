package com.example.plazoleta.domain.api;

import com.example.plazoleta.domain.model.Dish;

import java.util.List;

public interface IDishServicePort {

    Dish saveDish(Dish dish, String token);

    Dish updateDish(Dish dish, String token);

    Dish updateDishStatus(Long idDish, String token);

    List<Dish> getAllDish();

    List<Dish> getDishRestaurant(Long idRestaurant, String token);

    Dish findById(Long dishId);
}
