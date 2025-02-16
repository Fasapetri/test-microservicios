package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.api.IDishServicePort;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IJwtServicePort;
import com.example.plazoleta.domain.exception.DishException;
import com.example.plazoleta.domain.exception.DishExceptionType;
import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.spi.IDishPersistencePort;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;

import java.util.List;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort iDishPersistencePort;
    private final IJwtServicePort iJwtServicePort;
    private final IRestaurantPersistencePort iRestaurantPersistencePort;

    public DishUseCase(IDishPersistencePort iDishPersistencePort, IJwtServicePort iJwtServicePort, IRestaurantPersistencePort iRestaurantPersistencePort) {
        this.iDishPersistencePort = iDishPersistencePort;
        this.iJwtServicePort = iJwtServicePort;
        this.iRestaurantPersistencePort = iRestaurantPersistencePort;
    }

    @Override
    public Dish saveDish(Dish dish, String token) {
        User loginUser = iJwtServicePort.validateToken(token);

        if (!"PROPIETARIO".equalsIgnoreCase(loginUser.getRol())) {
            throw new DishException(DishExceptionType.INVALID_ROL_CREATED_DISH);
        }

        Restaurant restaurant = iRestaurantPersistencePort.findById(dish.getRestaurant().getId());

        if(restaurant == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_RESTAURANT);
        }

        if (!restaurant.getId_proprietary().equals(loginUser.getId())) {
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }

        return iDishPersistencePort.saveDish(dish);
    }

    @Override
    public Dish updateDish(Dish dish, String token) {

        User loginUser = iJwtServicePort.validateToken(token);

        if(!"PROPIETARIO".equalsIgnoreCase(loginUser.getRol())){
            throw new DishException(DishExceptionType.INVALID_ROL_UPDATE_DISH);
        }

        Dish oldDish = iDishPersistencePort.findById(dish.getId());

        if(oldDish == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_DISH);
        }

        Restaurant restaurant = dish.getRestaurant();

        if(!restaurant.getId_proprietary().equals(loginUser.getId())){
            throw new DishException(DishExceptionType.NOT_EXISTS_RESTAURANT);
        }

        dish.setId(oldDish.getId());

        return iDishPersistencePort.updateDish(dish);
    }

    @Override
    public Dish updateDishStatus(Long idDish, String token) {

        User loginUser = iJwtServicePort.validateToken(token);

        if(!"PROPIETARIO".equalsIgnoreCase(loginUser.getRol())){
            throw new DishException(DishExceptionType.INVALID_ROL_UPDATE_DISH);
        }

        Dish dish = iDishPersistencePort.findById(idDish);

        if(dish == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_DISH);
        }

        if (!dish.getRestaurant().getId_proprietary().equals(loginUser.getId())){
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }

        dish.setActive(!dish.getActive());
        return iDishPersistencePort.updateDishStatus(dish.getId());
    }

    @Override
    public List<Dish> getAllDish() {
        return iDishPersistencePort.getAllDish();
    }

    @Override
    public List<Dish> getDishRestaurant(Long idRestaurant, String token) {
        Restaurant restaurant = iRestaurantPersistencePort.findById(idRestaurant);
        if(restaurant == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_RESTAURANT);
        }
        return iDishPersistencePort.getDishRestaurant(idRestaurant);
    }

    @Override
    public Dish findById(Long dishId) {
        return iDishPersistencePort.findById(dishId);
    }
}
