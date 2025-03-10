package com.example.plazoleta.domain.validations;

import com.example.plazoleta.domain.exception.DishException;
import com.example.plazoleta.domain.exception.DishExceptionType;
import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.Restaurant;

import static com.example.plazoleta.domain.constants.DishUseCaseConstants.ROLE_PROPIETARIO;

public class DishUseCaseValidation {

    public void valitationCreatedDish(Long userAuthenticatedId, Restaurant foundRestaurant){

        if(foundRestaurant == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_RESTAURANT);
        }

        if (!foundRestaurant.getId_proprietary().equals(userAuthenticatedId)) {
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }

    }

    public void validationUpdateDish(Long userAuthenticatedId, Dish oldDish){

        if(oldDish == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_DISH);
        }

        Restaurant foundRestaurant = oldDish.getRestaurant();

        if(!foundRestaurant.getId_proprietary().equals(userAuthenticatedId)){
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }
    }

    public void validationUpdateStatusDish(Long userAuthenticatedId, Dish dishToUpdate){
        if(dishToUpdate == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_DISH);
        }

        if (!dishToUpdate.getRestaurant().getId_proprietary().equals(userAuthenticatedId)){
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }
    }

    public void validationFindDishRestaurant(Restaurant findRestaurant){
        if(findRestaurant == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_RESTAURANT);
        }

    }
}
