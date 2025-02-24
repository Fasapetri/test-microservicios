package com.example.pedidos.infraestructure.adapter;

import com.example.pedidos.application.dto.ClientResponse;
import com.example.pedidos.domain.model.Client;
import com.example.pedidos.domain.spi.IUserClientServicePort;
import com.example.pedidos.infraestructure.constants.SecurityContextAdapterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserClientAdapter implements IUserClientServicePort {

    private final WebClient webClient;

    public UserClientAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081")
                .filter(addAuthToken())
                .build();
    }

    @Override
    public Mono<Client> findDataClient(Long findClientId) {
        return webClient.get()
                .uri("/api/users/id/{id}", findClientId)
                .retrieve()
                .bodyToMono(Client.class)
                .map(clientResponse -> new Client(clientResponse.getId(), clientResponse.getName(), clientResponse.getLastName(), clientResponse.getPhone()));

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
