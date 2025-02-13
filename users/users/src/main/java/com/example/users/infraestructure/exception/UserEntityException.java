package com.example.users.infraestructure.exception;

public class UserEntityException extends RuntimeException{

    private final UserEntityExceptionType userEntityExceptionType;

    public UserEntityException(UserEntityExceptionType userEntityExceptionType) {
        super(userEntityExceptionType.getMessage());
        this.userEntityExceptionType = userEntityExceptionType;
    }
}
