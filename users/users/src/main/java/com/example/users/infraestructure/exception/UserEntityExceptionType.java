package com.example.users.infraestructure.exception;

public enum UserEntityExceptionType {
    USER_ALREADY_EXISTS("Ya existe un usuario creado con ese email"),
    USER_NOT_DATA("No existen datos de usuarios registrado"),
    USER_NOT_FOUND("El usuario no existe");

    private final String message;

    UserEntityExceptionType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
