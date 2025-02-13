package com.example.plazoleta.infraestructure.output.jpa.adapter;

import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.spi.IDishPersistencePort;
import com.example.plazoleta.infraestructure.output.jpa.entity.DishEntity;
import com.example.plazoleta.infraestructure.output.jpa.exception.DishEntityException;
import com.example.plazoleta.infraestructure.output.jpa.exception.DishEntityExceptionType;
import com.example.plazoleta.infraestructure.output.jpa.mapper.DishEntityMapper;
import com.example.plazoleta.infraestructure.output.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository iDishRepository;
    private final DishEntityMapper dishEntityMapper;

    @Override
    public Dish saveDish(Dish dish) {
        DishEntity dishEntity = iDishRepository.save(dishEntityMapper.toDishEntity(dish));
        return dishEntityMapper.toDish(dishEntity);
    }

    @Override
    public Dish updateDish(Dish dish) {
        DishEntity dishEntity = iDishRepository.save(dishEntityMapper.toDishEntity(dish));
        return dishEntityMapper.toDish(dishEntity);
    }

    @Override
    public Dish updateDishStatus(Long idDish) {
        DishEntity dishEntity = iDishRepository.save(idDish);
        return dishEntityMapper.toDish(dishEntity);
    }

    @Override
    public List<Dish> getAllDish() {
        List<DishEntity> listEntities= iDishRepository.findAll();
        if(listEntities.isEmpty()){
            throw new DishEntityException(DishEntityExceptionType.DISH_NOT_DATA);
        }
        return dishEntityMapper.toListDish(listEntities);
    }

    @Override
    public List<Dish> getDishRestaurant(Long idRestaurant) {
        List<DishEntity> listEntities = iDishRepository.findAllByRestaurantId(idRestaurant);

        if(listEntities.isEmpty()){
            throw new DishEntityException(DishEntityExceptionType.DISH_NOT_DATA);
        }
        return dishEntityMapper.toListDish(listEntities);
    }

    @Override
    public Dish findById(Long dishId) {
        return dishEntityMapper.toDish(iDishRepository.findById(dishId)
                .orElseThrow(() -> new DishEntityException(DishEntityExceptionType.DISH_NOT_FOUND)));
    }
}
