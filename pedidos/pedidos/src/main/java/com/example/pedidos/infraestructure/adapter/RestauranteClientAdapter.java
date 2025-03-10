package com.example.pedidos.infraestructure.adapter;

import com.example.pedidos.domain.spi.IRestaurantServicePort;
import com.example.pedidos.infraestructure.constants.SecurityContextAdapterConstants;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RestauranteClientAdapter implements IRestaurantServicePort {

    private final WebClient webClient;

    public RestauranteClientAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(SecurityContextAdapterConstants.WEBCLIENT_BASEURL_RESTAURANT)
                .filter(addAuthToken())
                .build();
    }


    @Override
    public Mono<Boolean> existsRestaurant(Long findRestaurantId) {
        return webClient.get()
                .uri(SecurityContextAdapterConstants.WEBCLIENT_URI_EXISTSRESTAURANT, findRestaurantId)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    @Override
    public Mono<List<Object>> findDishsRestaurant(Long findRestaurantId) {
        return webClient.get()
                .uri(SecurityContextAdapterConstants.WEBCLIENT_URI_FIND_DISH_RESTAURANT, findRestaurantId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Object>>() {
                });
    }

    private ExchangeFilterFunction addAuthToken() {
        return (request, next) -> ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    if (authentication != null && authentication.getCredentials() instanceof String) {
                        return (String) authentication.getCredentials();
                    }
                    return null;
                })
                .flatMap(token -> {
                    ClientRequest.Builder requestBuilder = ClientRequest.from(request);
                    if (token != null) {
                        requestBuilder.header(SecurityContextAdapterConstants.AUTHORIZATION_HEADER, SecurityContextAdapterConstants.BEARER_PREFIX + token);
                    }
                    return next.exchange(requestBuilder.build());
                });
    }
}
