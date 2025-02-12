package com.example.users.domain.api;

public interface IAuthServicePort {

    String authenticate(String email, String password);
}
