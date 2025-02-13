package com.example.plazoleta.domain.exception;

public enum DishExceptionType {
    INVALID_ROL_CREATED_DISH("Solo los usuarios con rol PROPIETARIO pueden registrar platos."),
    NOT_EXISTS_RESTAURANT("El restaurante no existe"),
    RESTAURANT_DOES_NOT_BELONG("El restaurante no pertenece al propietario autenticado."),
    INVALID_ROL_UPDATE_DISH("Solo los usuarios con rol PROPIETARIO pueden actualizar platos."),
    NOT_EXISTS_DISH("El plato no existe");

    private final String message;


    DishExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
