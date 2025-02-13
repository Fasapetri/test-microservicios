package com.example.plazoleta.domain.exception;

public enum RestaurantExceptionType {
    INVALID_ROL_CREATED_RESTAURANT("Solo los usuarios con rol ADMIN pueden crear restaurantes."),
    INVALID_ROL_PROPIETARIO("El ID del propietario no es valido o no tiene el rol de propietario"),
    NIT_RESTAURANT_ALREADY_EXISTS("El NIT ya está registrado"),
    INVALID_NAME_RESTAURANT("El nombre no puede contener solo números"),
    INVALID_PHONE_RESTAURANT("El telefono debe contener un maximo de 13 numeros incluido el +");


    private final String message;

    RestaurantExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
