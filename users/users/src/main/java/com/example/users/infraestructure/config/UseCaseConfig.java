package com.example.users.infraestructure.config;

import com.example.users.domain.api.IAuthServicePort;
import com.example.users.domain.spi.*;
import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.usecase.AuthUseCase;
import com.example.users.domain.usecase.UserUseCase;
import com.example.users.domain.validations.UserCaseUseValidation;
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
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort, IPasswordEncodePort passwordEncoderPort,
                                              UserCaseUseValidation userCaseUseValidation) {
        return new UserUseCase(userPersistencePort, passwordEncoderPort, userCaseUseValidation);
    }

    @Bean
    public UserCaseUseValidation userCaseUseValidation() {
        return new UserCaseUseValidation();
    }
}

