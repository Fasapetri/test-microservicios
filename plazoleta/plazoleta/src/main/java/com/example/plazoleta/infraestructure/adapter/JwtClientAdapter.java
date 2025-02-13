package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.application.dto.TokenValidationResponse;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IJwtServicePort;
import com.example.plazoleta.infraestructure.client.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtClientAdapter implements IJwtServicePort {

    private final AuthFeignClient authFeignClient;

    @Override
    public User validateToken(String token) {
        TokenValidationResponse tokenValidationResponse = authFeignClient.validateToken(token);
        return new User(tokenValidationResponse.getUserId(), tokenValidationResponse.getEmail(), tokenValidationResponse.getRole());
    }
}
