package com.example.plazoleta.domain.exception;

public class DishException extends RuntimeException{

    private final DishExceptionType dishExceptionType;

    public DishException(DishExceptionType dishExceptionType) {
        super(dishExceptionType.getMessage());
        this.dishExceptionType = dishExceptionType;
    }

    public DishExceptionType getDishType(){
        return dishExceptionType;
    }
}
