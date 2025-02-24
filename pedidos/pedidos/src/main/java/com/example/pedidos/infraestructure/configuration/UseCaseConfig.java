package com.example.pedidos.infraestructure.configuration;

import com.example.pedidos.domain.api.IPedidoServicePort;
import com.example.pedidos.domain.api.ITrazabilidadServicePort;
import com.example.pedidos.domain.spi.*;
import com.example.pedidos.domain.usecase.PedidoUseCase;
import com.example.pedidos.domain.usecase.TrazabilidadUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

        @Bean
        public IPedidoServicePort iPedidoServicePort(IPedidoPersistencePort iPedidoPersistencePort,
                                                     ITrazabilidadPersistencePort iTrazabilidadPersistencePort,
                                                     IRestaurantServicePort iRestaurantServicePort, IUserClientServicePort iClientServicePort,
                                                     ISmsServicePort iSmsServicePort, ISecurityContextPort securityContextPort){
            return new PedidoUseCase(iPedidoPersistencePort, iTrazabilidadPersistencePort, iRestaurantServicePort, iClientServicePort, iSmsServicePort, securityContextPort);

        }

        @Bean
        public ITrazabilidadServicePort iTrazabilidadServicePort(ITrazabilidadPersistencePort iTrazabilidadPersistencePort, ISecurityContextPort securityContextPort){
            return new TrazabilidadUseCase(iTrazabilidadPersistencePort, securityContextPort);
        }
}
