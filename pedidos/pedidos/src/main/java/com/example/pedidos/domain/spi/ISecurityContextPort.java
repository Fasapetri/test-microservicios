package com.example.pedidos.domain.spi;

import reactor.core.publisher.Mono;

public interface ISecurityContextPort {

    Mono<String> getUserAuthenticateRol();

    Mono<Long> getAuthenticatedUserId();


}
