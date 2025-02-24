package com.example.pedidos.domain.spi;

import com.example.pedidos.domain.model.Client;
import reactor.core.publisher.Mono;

public interface IUserClientServicePort {

    Mono<Client> findDataClient(Long clientId);

}
