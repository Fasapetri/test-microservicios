package com.example.users.application.handler;

import com.example.users.application.dto.AuthRequest;
import com.example.users.application.dto.AuthResponse;
import com.example.users.application.dto.TokenValidationResponse;
import com.example.users.application.mapper.AuthMapper;
import com.example.users.domain.api.IAuthServicePort;
import com.example.users.domain.api.IJwtServicePort;
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

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        String token = iAuthServicePort.authenticate(authRequest.getEmail(), authRequest.getPassword());
        return authMapper.authRequestToAuthResponse(authRequest, token);
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
