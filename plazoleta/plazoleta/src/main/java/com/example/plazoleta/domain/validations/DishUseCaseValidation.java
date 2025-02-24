package com.example.plazoleta.domain.validations;

import com.example.plazoleta.domain.exception.DishException;
import com.example.plazoleta.domain.exception.DishExceptionType;
import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.Restaurant;

import static com.example.plazoleta.domain.constants.DishUseCaseConstants.ROLE_PROPIETARIO;

public class DishUseCaseValidation {

    public void valitationCreatedDish(String userAuthenticatedRol, Long userAuthenticatedId, Restaurant foundRestaurant){

        if (!ROLE_PROPIETARIO.equalsIgnoreCase(userAuthenticatedRol)) {
            throw new DishException(DishExceptionType.INVALID_ROL_CREATED_DISH);
        }

        if(foundRestaurant == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_RESTAURANT);
        }

        if (!foundRestaurant.getId_proprietary().equals(userAuthenticatedId)) {
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }

    }

    public void validationUpdateDish(String userAuthenticatedRol, Long userAuthenticatedId, Dish oldDish){
        if(!ROLE_PROPIETARIO.equalsIgnoreCase(userAuthenticatedRol)){
            throw new DishException(DishExceptionType.INVALID_ROL_UPDATE_DISH);
        }

        if(oldDish == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_DISH);
        }

        Restaurant foundRestaurant = oldDish.getRestaurant();

        if(!foundRestaurant.getId_proprietary().equals(userAuthenticatedId)){
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }
    }

    public void validationUpdateStatusDish(String userAuthenticatedRol, Long userAuthenticatedId, Dish dishToUpdate){
        if(!ROLE_PROPIETARIO.equalsIgnoreCase(userAuthenticatedRol)){
            throw new DishException(DishExceptionType.INVALID_ROL_UPDATE_DISH);
        }

        if(dishToUpdate == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_DISH);
        }

        if (!dishToUpdate.getRestaurant().getId_proprietary().equals(userAuthenticatedId)){
            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
        }
    }

    public void validationFindDishRestaurant(Restaurant findRestaurant, Long userAuthenticatedId){
        if(findRestaurant == null){
            throw new DishException(DishExceptionType.NOT_EXISTS_RESTAURANT);
        }

//        if(!userAuthenticatedId.equals(findRestaurant.getId_proprietary())){
//            throw new DishException(DishExceptionType.RESTAURANT_DOES_NOT_BELONG);
//        }
    }
}
