package com.example.plazoleta.infraestructure.config;

import com.example.plazoleta.domain.api.IDishServicePort;
import com.example.plazoleta.domain.api.IRestaurantServicePort;
import com.example.plazoleta.domain.spi.*;
import com.example.plazoleta.domain.usecase.DishUseCase;
import com.example.plazoleta.domain.usecase.RestaurantUseCase;
import com.example.plazoleta.domain.validations.DishUseCaseValidation;
import com.example.plazoleta.domain.validations.RestaurantUseCaseValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                        IUserClientPort iUserClientPort, ISecurityContextPort iSecurityContextPort, RestaurantUseCaseValidation restaurantUseCaseValidation) {
        return new RestaurantUseCase(restaurantPersistencePort, iUserClientPort, iSecurityContextPort, restaurantUseCaseValidation);
    }

    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort,
                                            IRestaurantPersistencePort iRestaurantPersistencePort, ISecurityContextPort iSecurityContextPort, DishUseCaseValidation dishUseCaseValidation) {
        return new DishUseCase(dishPersistencePort, iRestaurantPersistencePort, iSecurityContextPort, dishUseCaseValidation);
    }

    @Bean
    public DishUseCaseValidation dishUseCaseValidation() {
        return new DishUseCaseValidation();
    }

    @Bean
    public RestaurantUseCaseValidation restaurantCaseUseValidation() {
        return new RestaurantUseCaseValidation();
    }


}
