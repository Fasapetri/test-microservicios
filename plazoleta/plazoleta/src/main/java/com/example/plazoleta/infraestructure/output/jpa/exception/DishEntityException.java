package com.example.plazoleta.infraestructure.output.jpa.exception;

public class DishEntityException extends RuntimeException{

    private final DishEntityExceptionType dishEntityExceptionType;


    public DishEntityException(DishEntityExceptionType dishEntityExceptionType) {
        super(dishEntityExceptionType.getMessage());
        this.dishEntityExceptionType = dishEntityExceptionType;
    }

    public DishEntityExceptionType dishEntityType(){
        return dishEntityExceptionType;
    }
}
