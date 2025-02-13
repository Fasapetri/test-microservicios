package com.example.plazoleta.infraestructure.output.jpa.exception;

public enum DishEntityExceptionType {

    EXIST_DISH("El plato ya se encuentra registrado"),
    DISH_NOT_DATA("No existen datos de platos registrados."),
    DISH_NOT_FOUND("El plato no existe");

    private final String message;

    DishEntityExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
