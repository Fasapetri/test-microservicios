package com.example.users.domain.usecase;


import com.example.users.domain.api.IAuthServicePort;
import com.example.users.domain.spi.IJwtServicePort;
import com.example.users.domain.spi.IPasswordEncodePort;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.ITokenBlackListServicePort;
import com.example.users.domain.spi.IUserPersistencePort;

public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort iUserPersistencePort;
    private final IPasswordEncodePort iPasswordEncodePort;
    private final IJwtServicePort iJwtServicePort;
    private final ITokenBlackListServicePort iTokenBlackListServicePort;

    public AuthUseCase(IUserPersistencePort iUserPersistencePort, IPasswordEncodePort iPasswordEncodePort, IJwtServicePort iJwtServicePort, ITokenBlackListServicePort iTokenBlackListServicePort) {
        this.iUserPersistencePort = iUserPersistencePort;
        this.iPasswordEncodePort = iPasswordEncodePort;
        this.iJwtServicePort = iJwtServicePort;
        this.iTokenBlackListServicePort = iTokenBlackListServicePort;
    }

    @Override
    public String authenticate(String email, String password) {
        User user = iUserPersistencePort.findByEmailUser(email);

        if(user == null || !iPasswordEncodePort.matches(password, user.getPassword())){
            throw  new IllegalArgumentException("Credenciales inválidas");
        }

        return iJwtServicePort.generarToken(user.getEmail(), user.getRol(), user.getId());
    }

    @Override
    public void invalidateToken(String token, long expirationTime) {
        iTokenBlackListServicePort.invalidateToken(token, expirationTime);
    }

    @Override
    public boolean isTokenInvalidated(String token) {
        return  iTokenBlackListServicePort.isTokenInvalidated(token);
    }

    @Override
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
