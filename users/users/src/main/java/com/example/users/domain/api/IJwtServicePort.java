package com.example.users.domain.api;

public interface IJwtServicePort {

    String generarToken(String email, String role, Long userId);
    String extractRoleFromToken(String token);
    Long extractUserId(String token);
    String extractEmail(String token);
}
