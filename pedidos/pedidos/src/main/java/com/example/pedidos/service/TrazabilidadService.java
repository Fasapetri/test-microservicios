package com.example.pedidos.service;

import com.example.pedidos.client.UsuarioClient;
import com.example.pedidos.model.TrazabilidadPedido;
import com.example.pedidos.repository.TrazabilidadRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class TrazabilidadService {

    private final TrazabilidadRepository trazabilidadRepository;
    private final UsuarioClient usuarioClient;

    public TrazabilidadService(TrazabilidadRepository trazabilidadRepository, UsuarioClient usuarioClient) {
        this.trazabilidadRepository = trazabilidadRepository;
        this.usuarioClient = usuarioClient;
    }

    public Flux<TrazabilidadPedido> listarHistorialPedido(String token, String idPedido){
        Map<String, Object> usuario = usuarioClient.validateToken(token);
        Object objectId = usuario.get("userId");
        Long userId;

        if(objectId instanceof Integer){
            userId = ((Integer) objectId).longValue();
        } else if(objectId instanceof Long){
            userId = (Long) objectId;
        } else {
            throw new IllegalArgumentException("Tipo de userId desconocido: " + objectId.getClass().getName());
        }
        return trazabilidadRepository.findByPedidoId(idPedido)
                .filter(trazabilidadPedido -> trazabilidadPedido.getClienteId().equals(userId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No tienes permisos para consultar este pedido")));
    }
}
