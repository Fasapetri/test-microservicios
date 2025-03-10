package com.example.users.application.handler;

import com.example.users.application.dto.AuthRequest;
import com.example.users.application.dto.AuthResponse;

public interface IAuthHandler {

    AuthResponse login(AuthRequest authRequest);

    void logoutUser(String token);

    boolean isTokenRevoked(String token);

    String extractToken(String authorizationHeader);
}
