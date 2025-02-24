package com.example.plazoleta.domain.validations;

import com.example.plazoleta.domain.constants.RestaurantUseCaseConstants;
import com.example.plazoleta.domain.exception.RestaurantException;
import com.example.plazoleta.domain.exception.RestaurantExceptionType;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.model.User;

import java.util.regex.Pattern;

public class RestaurantUseCaseValidation {

    public void ValidationCreateRestaurant(String userAuthenticatedRol, User findRestaurantPropietario, Restaurant restaurantToCreate){
        if (!RestaurantUseCaseConstants.ROLE_ADMIN.equalsIgnoreCase(userAuthenticatedRol)) {
            throw new RestaurantException(RestaurantExceptionType.INVALID_ROL_CREATED_RESTAURANT);
        }

        if(findRestaurantPropietario == null || !RestaurantUseCaseConstants.ROLE_PROPIETARIO.equals(findRestaurantPropietario.getRol())){
            throw new RestaurantException(RestaurantExceptionType.INVALID_ROL_PROPIETARIO);
        }

        if(Pattern.matches(RestaurantUseCaseConstants.REGEX_NUMERIC_ONLY, restaurantToCreate.getName())){
            throw new RestaurantException(RestaurantExceptionType.INVALID_NAME_RESTAURANT);
        }

        if(!restaurantToCreate.getPhone().matches(RestaurantUseCaseConstants.REGEX_PHONE_NUMBER)){
            throw new RestaurantException(RestaurantExceptionType.INVALID_PHONE_RESTAURANT);
        }
    }
}
