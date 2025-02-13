package com.example.plazoleta.infraestructure.config;

import com.example.plazoleta.domain.api.IDishServicePort;
import com.example.plazoleta.domain.api.IRestaurantServicePort;
import com.example.plazoleta.domain.spi.IDishPersistencePort;
import com.example.plazoleta.domain.spi.IJwtServicePort;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.IUserClientPort;
import com.example.plazoleta.domain.usecase.DishUseCase;
import com.example.plazoleta.domain.usecase.RestaurantUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,                                            IJwtServicePort jwtServicePort,
                                                        IUserClientPort iUserClientPort) {
        return new RestaurantUseCase(restaurantPersistencePort, jwtServicePort, iUserClientPort);
    }

    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort,
                                            IJwtServicePort jwtServicePort, IRestaurantPersistencePort iRestaurantPersistencePort) {
        return new DishUseCase(dishPersistencePort, jwtServicePort, iRestaurantPersistencePort);
    }

}
