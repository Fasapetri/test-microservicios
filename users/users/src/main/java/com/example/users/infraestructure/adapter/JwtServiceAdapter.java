package com.example.users.infraestructure.adapter;

import com.example.users.domain.spi.IJwtServicePort;
import com.example.users.infraestructure.security.JwtUtil;
import com.example.users.infraestructure.constants.SecurityContextAdapterConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtServiceAdapter implements IJwtServicePort {

    private final JwtUtil jwtUtil;

    @Override
    public String generarToken(String email, String role, Long userId) {
        return jwtUtil.generateToken(email, role, userId);
    }

    @Override
    public String extractRoleFromToken(String token) {
        return jwtUtil.extractRole(cleanToken(token));
    }

    @Override
    public Long extractUserId(String token) {
        return jwtUtil.extractUserId(cleanToken(token));
    }

    @Override
    public String extractEmail(String token) {
        return jwtUtil.extractEmail(cleanToken(token));
    }

    @Override
    public Long getExpirationTime(String token) {
        return jwtUtil.getExpirationTime(token);
    }

    private String cleanToken(String token){
        return token.replace(SecurityContextAdapterConstants.BEARER_PREFIX, "");
    }
}
