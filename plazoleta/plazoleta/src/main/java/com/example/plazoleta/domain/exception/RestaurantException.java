package com.example.plazoleta.domain.exception;

public class RestaurantException extends RuntimeException{

    private final RestaurantExceptionType restaurantExceptionType;

    public RestaurantException(RestaurantExceptionType restaurantExceptionType) {
        super(restaurantExceptionType.getMessage());
        this.restaurantExceptionType = restaurantExceptionType;
    }

    public RestaurantExceptionType getRestaurantType(){
        return restaurantExceptionType;
    }
}
