package com.example.users.domain.spi;

public interface ITokenBlackListServicePort {

    void invalidateToken(String token, long expirationTime);

    boolean isTokenInvalidated(String token);
}
