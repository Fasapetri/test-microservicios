package com.example.users.infraestructure.adapter;

import com.example.users.domain.spi.ITokenBlackListServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenBlackListAdapter implements ITokenBlackListServicePort {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void invalidateToken(String token, long expirationTime) {
        redisTemplate.opsForValue().set(token, "revoked", expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isTokenInvalidated(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
