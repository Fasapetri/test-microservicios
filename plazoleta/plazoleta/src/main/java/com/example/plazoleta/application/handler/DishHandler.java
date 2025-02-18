package com.example.plazoleta.application.handler;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.application.mapper.DishMapper;
import com.example.plazoleta.domain.api.IDishServicePort;
import com.example.plazoleta.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler{

    private final IDishServicePort iDishServicePort;
    private final DishMapper dishMapper;

    @Override
    public DishResponse saveDish(DishRequest dishRequest, String token) {
        Dish mapperDish = dishMapper.toDish(dishRequest);
        iDishServicePort.saveDish(mapperDish, token);
        return dishMapper.toDishResponse(mapperDish);
    }

    @Override
    public DishResponse updateDish(Long id_dish, DishRequest dishRequest, String token) {
        Dish dish = dishMapper.toDish(dishRequest);
        iDishServicePort.updateDish(id_dish, dish, token);
        return dishMapper.toDishResponse(dish);
    }

    @Override
    public DishResponse updateDishStatus(Long idDish, String token) {
        return dishMapper.toDishResponse(iDishServicePort.updateDishStatus(idDish, token));
    }

    @Override
    public List<DishResponse> getAllDish(String token) {
        return dishMapper.toListDishResponse(iDishServicePort.getAllDish(token));
    }

    @Override
    public List<DishResponse> getDishRestaurant(Long idRestaurant, String token) {
        return dishMapper.toListDishResponse(iDishServicePort.getDishRestaurant(idRestaurant, token));
    }

    @Override
    public DishResponse findById(Long dishId, String token) {
        return dishMapper.toDishResponse(iDishServicePort.findById(dishId, token));
    }
}
