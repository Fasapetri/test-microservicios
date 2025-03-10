package com.example.plazoleta.domain.exception;

public class AsignarEmpleadoRestaurantException extends RuntimeException{

    private final AsignarEmpleadoRestaurantExceptionType asignarEmpleadoRestaurantExceptionType;

    public AsignarEmpleadoRestaurantException(AsignarEmpleadoRestaurantExceptionType asignarEmpleadoRestaurantExceptionType) {
        super(asignarEmpleadoRestaurantExceptionType.getMessage());
        this.asignarEmpleadoRestaurantExceptionType = asignarEmpleadoRestaurantExceptionType;
    }

    public AsignarEmpleadoRestaurantExceptionType getType(){
        return asignarEmpleadoRestaurantExceptionType;
    }

}
