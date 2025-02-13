package com.example.users.domain.exception;

public class UserException extends RuntimeException{

    private final UserExceptionType userExceptionType;


    public UserException(UserExceptionType userExceptionType) {
        super(userExceptionType.getMessage());
        this.userExceptionType = userExceptionType;
    }

    public UserExceptionType getType(){
        return userExceptionType;
    }
}
