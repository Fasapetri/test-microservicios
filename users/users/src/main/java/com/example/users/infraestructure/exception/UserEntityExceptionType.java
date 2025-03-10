package com.example.users.infraestructure.exception;

public enum UserEntityExceptionType {

    USER_NOT_FOUND("El usuario no existe");

    private final String message;

    UserEntityExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
