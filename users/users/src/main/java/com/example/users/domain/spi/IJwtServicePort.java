package com.example.users.domain.spi;

public interface IJwtServicePort {

    String generarToken(String email, String role, Long userId);

    String extractRoleFromToken(String token);

    Long extractUserId(String token);

    String extractEmail(String token);

    Long getExpirationTime(String token);


}
