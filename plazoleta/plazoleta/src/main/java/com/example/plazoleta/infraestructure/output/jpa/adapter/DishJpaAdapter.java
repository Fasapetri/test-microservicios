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
    public Dish saveDish(Dish dishToCreate) {
        DishEntity dishEntity = iDishRepository.save(dishEntityMapper.toDishEntity(dishToCreate));
        return dishEntityMapper.toDish(dishEntity);
    }

    @Override
    public Dish updateDish(Dish dishToUpdate) {
        DishEntity mapperDishEntity = iDishRepository.save(dishEntityMapper.toDishEntity(dishToUpdate));
        return dishEntityMapper.toDish(mapperDishEntity);
    }

    @Override
    public Dish updateDishStatus(Long findDishId) {
        Dish foundDish = this.findById(findDishId);
        DishEntity mapperDishEntity = iDishRepository.save(dishEntityMapper.toDishEntity(foundDish));
        return dishEntityMapper.toDish(mapperDishEntity);
    }

    @Override
    public List<Dish> getAllDish() {
        return dishEntityMapper.toListDish(iDishRepository.findAll());
    }

    @Override
    public List<Dish> getDishRestaurantCategory(Long findRestaurantId, String dishCategory) {
        return dishEntityMapper.toListDish(iDishRepository.findAllByRestaurantIdAndCategory(findRestaurantId, dishCategory));
    }

    @Override
    public List<Dish> getDishRestaurant(Long idRestaurant) {
        return dishEntityMapper.toListDish(iDishRepository.findAllByRestaurantId(idRestaurant));
    }

    @Override
    public Dish findById(Long dishId) {
        return iDishRepository.findById(dishId).map(dishEntityMapper::toDish).orElse(null);
    }
}
