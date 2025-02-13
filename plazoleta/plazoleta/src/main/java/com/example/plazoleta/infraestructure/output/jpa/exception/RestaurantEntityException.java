package com.example.plazoleta.infraestructure.output.jpa.exception;

public class RestaurantEntityException extends RuntimeException{

    private final RestaurantEntityExceptionType restaurantEntityExceptionType;

    public RestaurantEntityException(RestaurantEntityExceptionType restaurantEntityExceptionType) {
        super(restaurantEntityExceptionType.getMessage());
        this.restaurantEntityExceptionType = restaurantEntityExceptionType;
    }

    public RestaurantEntityExceptionType getRestaurantEntityType(){
        return restaurantEntityExceptionType;
    }
}
