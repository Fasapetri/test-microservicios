package com.example.users.domain.usecase;


import com.example.users.domain.api.IAuthServicePort;
import com.example.users.domain.spi.IJwtServicePort;
import com.example.users.domain.spi.IPasswordEncodePort;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.ITokenBlackListServicePort;
import com.example.users.domain.spi.IUserPersistencePort;

import static com.example.users.domain.constants.AuthConstants.*;

public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncodePort passwordEncodePort;
    private final IJwtServicePort jwtServicePort;
    private final ITokenBlackListServicePort tokenBlackListServicePort;

    public AuthUseCase(IUserPersistencePort iUserPersistencePort, IPasswordEncodePort iPasswordEncodePort, IJwtServicePort iJwtServicePort, ITokenBlackListServicePort iTokenBlackListServicePort) {
        this.userPersistencePort = iUserPersistencePort;
        this.passwordEncodePort = iPasswordEncodePort;
        this.jwtServicePort = iJwtServicePort;
        this.tokenBlackListServicePort = iTokenBlackListServicePort;
    }

    @Override
    public String authenticate(String email, String password) {
        User user = userPersistencePort.findByEmailUser(email);

        if(!passwordEncodePort.matches(password, user.getPassword())){
            throw  new IllegalArgumentException(INVALID_CREDENTIALS_MESSAGE);
        }

        return jwtServicePort.generarToken(user.getEmail(), user.getRol(), user.getId());
    }

    @Override
    public void invalidateToken(String token, long expirationTime) {
        tokenBlackListServicePort.invalidateToken(token, expirationTime);
    }

    @Override
    public boolean isTokenInvalidated(String token) {
        return  tokenBlackListServicePort.isTokenInvalidated(token);
    }

    @Override
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX_LENGTH);
        }
        return null;
    }
}
