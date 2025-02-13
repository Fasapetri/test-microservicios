package com.example.plazoleta.application.handler;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.domain.model.Dish;

import java.util.List;

public interface IDishHandler {

    DishResponse saveDish(DishRequest dishRequest, String token);

    DishResponse updateDish(DishRequest dishRequest, String token);

    DishResponse updateDishStatus(Long idDish, String token);

    List<DishResponse> getAllDish();

    List<DishResponse> getDishRestaurant(Long idRestaurant, String token);

    DishResponse findById(Long dishId);
}
