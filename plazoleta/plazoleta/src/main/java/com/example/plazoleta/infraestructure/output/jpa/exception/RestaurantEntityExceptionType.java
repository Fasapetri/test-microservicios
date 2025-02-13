package com.example.plazoleta.infraestructure.output.jpa.exception;

public enum RestaurantEntityExceptionType {

    EXIST_RESTAURANT("El restaurante ya existe"),
    RESTAURANT_NOT_DATA("No existen datos de restaurantes registrado"),
    RESTAURANT_NOT_FOUND("El restaurante no existe");

    private final String message;

    RestaurantEntityExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
