package com.example.pedidos.service;

import com.example.pedidos.client.UsuarioClient;
import com.example.pedidos.model.TrazabilidadPedido;
import com.example.pedidos.repository.TrazabilidadRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

        return Mono.fromCallable(()-> usuarioClient.validateToken(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(usuario -> {
                    Long userId = convertirUserId(usuario.get(("userId")));

                    return trazabilidadRepository.findByPedidoId(idPedido)
                            .filter(trazabilidadPedido -> trazabilidadPedido.getClienteId().equals(userId))
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("No tienes permisos para consultar este pedido")));
                });


    }

    private Long convertirUserId(Object objectId){
        if(objectId instanceof Integer){
            return ((Integer) objectId).longValue();
        } else if(objectId instanceof Long){
            return (Long) objectId;
        } else {
            throw new IllegalArgumentException("Tipo de userId desconocido: " + objectId.getClass().getName());
        }
    }
}
