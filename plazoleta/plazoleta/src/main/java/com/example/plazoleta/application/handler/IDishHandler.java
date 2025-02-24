package com.example.plazoleta.application.handler;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.domain.model.Dish;

import java.util.List;

public interface IDishHandler {

    DishResponse saveDish(DishRequest dishRequest);

    DishResponse updateDish(Long id_dish, DishRequest dishRequest);

    DishResponse updateDishStatus(Long idDish);

    List<DishResponse> getAllDish();

    List<DishResponse> getDishRestaurant(Long idRestaurant);

    DishResponse findById(Long dishId);
}
