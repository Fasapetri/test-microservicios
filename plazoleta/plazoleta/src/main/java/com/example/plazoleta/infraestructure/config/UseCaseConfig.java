package com.example.plazoleta.infraestructure.config;

import com.example.plazoleta.domain.api.IDishServicePort;
import com.example.plazoleta.domain.api.IEmpleadoRestaurantServicePort;
import com.example.plazoleta.domain.api.IPedidoServicePort;
import com.example.plazoleta.domain.api.IRestaurantServicePort;
import com.example.plazoleta.domain.spi.*;
import com.example.plazoleta.domain.usecase.DishUseCase;
import com.example.plazoleta.domain.usecase.EmpleadoRestaurantUseCase;
import com.example.plazoleta.domain.usecase.PedidoUseCase;
import com.example.plazoleta.domain.usecase.RestaurantUseCase;
import com.example.plazoleta.domain.validations.DishUseCaseValidation;
import com.example.plazoleta.domain.validations.EmpleadoRestaurantCaseValidation;
import com.example.plazoleta.domain.validations.PedidoUseCaseValidation;
import com.example.plazoleta.domain.validations.RestaurantUseCaseValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                        IUserClientPort iUserClientPort, RestaurantUseCaseValidation restaurantUseCaseValidation) {
        return new RestaurantUseCase(restaurantPersistencePort, iUserClientPort, restaurantUseCaseValidation);
    }

    @Bean
    public IDishServicePort dishServicePort(IDishPersistencePort dishPersistencePort,
                                            IRestaurantPersistencePort iRestaurantPersistencePort, ISecurityContextPort iSecurityContextPort, DishUseCaseValidation dishUseCaseValidation) {
        return new DishUseCase(dishPersistencePort, iRestaurantPersistencePort, iSecurityContextPort, dishUseCaseValidation);
    }

    @Bean
    public IEmpleadoRestaurantServicePort empleadoRestaurantServicePort(IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort,
                                                                         ISecurityContextPort securityContextPort,
                                                                        EmpleadoRestaurantCaseValidation empleadoRestaurantCaseValidation,
                                                                        IRestaurantPersistencePort restaurantPersistencePort){
        return new EmpleadoRestaurantUseCase(empleadoRestaurantPersistencePort, securityContextPort, empleadoRestaurantCaseValidation, restaurantPersistencePort);
    }

    @Bean
    public IPedidoServicePort pedidoServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                IPedidoPersistencePort pedidoPersistencePort,
                                                ISecurityContextPort securityContextPort,
                                                IDishPersistencePort dishPersistencePort,
                                                PedidoUseCaseValidation pedidoUseCaseValidation,
                                                IItemPedidoPersistencePort itemPedidoPersistencePort,
                                                ITrazabilidadPersistencePort trazabilidadPersistencePort,
                                                 IUserClientPort userClientPort,
                                                IMensajeriaSmsPersistencePort smsPersistencePort){
        return new PedidoUseCase(restaurantPersistencePort,
                pedidoPersistencePort, securityContextPort,
                dishPersistencePort, pedidoUseCaseValidation,
                itemPedidoPersistencePort, trazabilidadPersistencePort, userClientPort, smsPersistencePort);
    }

    @Bean
    public DishUseCaseValidation dishUseCaseValidation() {
        return new DishUseCaseValidation();
    }

    @Bean
    public RestaurantUseCaseValidation restaurantCaseUseValidation() {
        return new RestaurantUseCaseValidation();
    }

    @Bean
    public EmpleadoRestaurantCaseValidation empleadoRestaurantCaseValidation(IUserClientPort userClientPort,
                                                                             IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort){
        return new EmpleadoRestaurantCaseValidation(userClientPort, empleadoRestaurantPersistencePort);
    }

    @Bean
    public PedidoUseCaseValidation pedidoUseCaseValidation(
            IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort,
            IPedidoPersistencePort pedidoPersistencePort, ISecurityContextPort securityContextPort){
        return new PedidoUseCaseValidation(empleadoRestaurantPersistencePort, pedidoPersistencePort,
                securityContextPort);
    }

}
