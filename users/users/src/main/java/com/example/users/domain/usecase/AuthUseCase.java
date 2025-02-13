package com.example.users.domain.usecase;


import com.example.users.domain.api.IAuthServicePort;
import com.example.users.domain.spi.IJwtServicePort;
import com.example.users.domain.spi.IPasswordEncodePort;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;

public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort iUserPersistencePort;
    private final IPasswordEncodePort iPasswordEncodePort;
    private final IJwtServicePort iJwtServicePort;

    public AuthUseCase(IUserPersistencePort iUserPersistencePort, IPasswordEncodePort iPasswordEncodePort, IJwtServicePort iJwtServicePort) {
        this.iUserPersistencePort = iUserPersistencePort;
        this.iPasswordEncodePort = iPasswordEncodePort;
        this.iJwtServicePort = iJwtServicePort;
    }

    @Override
    public String authenticate(String email, String password) {
        User user = iUserPersistencePort.findByEmailUser(email);

        if(user == null || !iPasswordEncodePort.matches(password, user.getPassword())){
            throw  new IllegalArgumentException("Credenciales inválidas");
        }

        return iJwtServicePort.generarToken(user.getEmail(), user.getRol(), user.getId());
    }
}
