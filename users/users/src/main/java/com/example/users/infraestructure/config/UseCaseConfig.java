package com.example.users.infraestructure.config;

import com.example.users.domain.api.IAuthServicePort;
import com.example.users.domain.spi.IJwtServicePort;
import com.example.users.domain.spi.IPasswordEncodePort;
import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.spi.ITokenBlackListServicePort;
import com.example.users.domain.spi.IUserPersistencePort;
import com.example.users.domain.usecase.AuthUseCase;
import com.example.users.domain.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public IAuthServicePort authServicePort(IUserPersistencePort userPersistencePort,
                                            IPasswordEncodePort passwordEncoderPort,
                                            IJwtServicePort jwtServicePort, ITokenBlackListServicePort iTokenBlackListServicePort) {
        return new AuthUseCase(userPersistencePort, passwordEncoderPort, jwtServicePort, iTokenBlackListServicePort);
    }

    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort,IPasswordEncodePort passwordEncoderPort,
                                            IJwtServicePort jwtServicePort) {
        return new UserUseCase(userPersistencePort, jwtServicePort, passwordEncoderPort);
    }
}

