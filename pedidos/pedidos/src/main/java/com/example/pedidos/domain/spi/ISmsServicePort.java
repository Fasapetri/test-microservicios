package com.example.pedidos.domain.spi;

import reactor.core.publisher.Mono;

public interface ISmsServicePort {

    Mono<Void> sendSms(String numclient, String message);
}
