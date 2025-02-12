package com.example.pedidos.service;

import com.example.pedidos.model.ClienteDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ClienteService {

    private final WebClient webClient;

    public ClienteService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public Mono<ClienteDTO> obtenerDatosCliente(Long idCliente, String token){
        return webClient.get()
                .uri("/api/users/id/{id}", idCliente)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(ClienteDTO.class);

    }
}
