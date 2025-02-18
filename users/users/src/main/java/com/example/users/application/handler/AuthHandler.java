package com.example.users.application.handler;

import com.example.users.application.dto.AuthRequest;
import com.example.users.application.dto.AuthResponse;
import com.example.users.application.dto.TokenValidationResponse;
import com.example.users.application.mapper.AuthMapper;
import com.example.users.domain.api.IAuthServicePort;
import com.example.users.domain.spi.IJwtServicePort;
import com.example.users.domain.spi.ITokenBlackListServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthHandler implements IAuthHandler{

    private final IAuthServicePort iAuthServicePort;
    private final AuthMapper authMapper;
    private final IJwtServicePort iJwtServicePort;
    private final ITokenBlackListServicePort iTokenBlackListServicePort;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        String token = iAuthServicePort.authenticate(authRequest.getEmail(), authRequest.getPassword());
        return authMapper.authRequestToAuthResponse(authRequest, token);
    }

    @Override
    public void logoutUser(String token) {
        long expirationTime = iJwtServicePort.getExpirationTime(token);
        iTokenBlackListServicePort.invalidateToken(token, expirationTime);
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return iTokenBlackListServicePort.isTokenInvalidated(token);
    }

    @Override
    public String extractToken(String authorizationHeader) {
        return iAuthServicePort.extractToken(authorizationHeader);
    }

    public TokenValidationResponse validateToken(String token){
        Long userId = iJwtServicePort.extractUserId(token);
        String email = iJwtServicePort.extractEmail(token);
        String rol = iJwtServicePort.extractRoleFromToken(token);

        return TokenValidationResponse.builder()
                .userId(userId)
                .email(email)
                .role(rol)
                .build();
    }
}
