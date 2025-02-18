package com.example.users.domain.api;

public interface IAuthServicePort {

    String authenticate(String email, String password);

    void invalidateToken(String token, long expirationTime);

    boolean isTokenInvalidated(String token);

    String extractToken(String authorizationHeader);
}
