package com.example.plazoleta.domain.exception;

public enum AsignarEmpleadoRestaurantExceptionType {

    NOT_EXISTS_USER("El usuario no existe en el sistema"),
    EMPLEADO_ALREADY_ASSIGNED_TO_RESTAURANT("El empleado ya está asignado a este restaurante"),
    PROPIETARIO_DOES_NOT_OWN_RESTAURANT("El propietario ingresado, no es dueño del restaurante"),
    EMPLEADO_ALREADY_ASSIGNED("El empleado ya está asignado a un restaurante"),
    USER_NOT_ROL_EMPLEADO("El usuario a asignar a restaurante no es un empleado");


    private final String message;

    AsignarEmpleadoRestaurantExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
