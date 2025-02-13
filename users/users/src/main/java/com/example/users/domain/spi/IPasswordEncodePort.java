package com.example.users.domain.spi;

public interface IPasswordEncodePort {

    String encode(String password);

    boolean matches(String rawPassword, String encodedPassword);
}
