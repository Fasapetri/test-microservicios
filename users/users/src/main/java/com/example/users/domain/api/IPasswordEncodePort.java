package com.example.users.domain.api;

public interface IPasswordEncodePort {

    String encode(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
