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

    private final IDishServicePort dishServicePort;
    private final DishMapper dishMapper;

    @Override
    public DishResponse saveDish(DishRequest dishToCreate) {
        Dish mapperDish = dishMapper.toDish(dishToCreate);
        dishServicePort.saveDish(mapperDish);
        return dishMapper.toDishResponse(mapperDish);
    }

    @Override
    public DishResponse updateDish(Long findDishId, DishRequest dishToUpdate) {
        Dish mapperDish = dishMapper.toDish(dishToUpdate);
        dishServicePort.updateDish(findDishId, mapperDish);
        return dishMapper.toDishResponse(mapperDish);
    }

    @Override
    public DishResponse updateDishStatus(Long findDishId) {
        return dishMapper.toDishResponse(dishServicePort.updateDishStatus(findDishId));
    }

    @Override
    public List<DishResponse> getAllDish() {
        return dishMapper.toListDishResponse(dishServicePort.getAllDish());
    }

    @Override
    public List<DishResponse> getDishRestaurantCategory(Long findRestaurantId, String dishCategory) {
        return dishMapper.toListDishResponse(dishServicePort.getDishRestaurantCategory(findRestaurantId, dishCategory));
    }

    @Override
    public DishResponse findById(Long findDishId) {
        return dishMapper.toDishResponse(dishServicePort.findById(findDishId));
    }
}
