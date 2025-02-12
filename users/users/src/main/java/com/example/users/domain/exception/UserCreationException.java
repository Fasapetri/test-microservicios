package com.example.users.domain.exception;

public class UserCreationException extends RuntimeException{

    public UserCreationException(String message){
        super(message);
    }
}
