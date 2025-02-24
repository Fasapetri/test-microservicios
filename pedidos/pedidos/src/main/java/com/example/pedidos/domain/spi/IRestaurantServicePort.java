package com.example.pedidos.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IRestaurantServicePort {

    Mono<Boolean> existsRestaurant(Long findRestaurantId);

    Mono<List<Object>> findDishsRestaurant(Long findRestaurantId);
}
