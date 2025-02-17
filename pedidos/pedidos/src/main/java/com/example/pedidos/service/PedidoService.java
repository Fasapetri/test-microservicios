package com.example.pedidos.service;

import com.example.pedidos.client.RestauranteClient;
import com.example.pedidos.client.UsuarioClient;
import com.example.pedidos.model.*;
import com.example.pedidos.repository.PedidoRepository;
import com.example.pedidos.repository.TrazabilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final RestauranteClient restauranteClient;
    private final UsuarioClient usuarioClient;
    private final TrazabilidadRepository trazabilidadRepository;

    @Transactional
    public Mono<Pedido> crearPedido(String token, Pedido pedido){

        return Mono.fromCallable(() -> usuarioClient.validateToken(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(usuario -> {
                    Long userId = convertirUserId(usuario.get("userId"));
                    String rol = (String) usuario.get("rol");

                    if(!"EMPLEADO".equalsIgnoreCase(rol)){
                        return Mono.error(new IllegalArgumentException("Solo los empleados pueden realizar pedido"));
                    }

                    pedido.setEmpleadoId(userId);

                    Mono<Boolean> existeRestauranteMono = Mono.fromCallable(()-> restauranteClient.existeRestaurante(pedido.getRestauranteId(), token))
                            .subscribeOn(Schedulers.boundedElastic());

                    Mono<ClienteDTO> clienteDTOMono = Mono.fromCallable(()-> usuarioClient.findById(pedido.getClienteId(), token))
                            .subscribeOn(Schedulers.boundedElastic())
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("El cliente no existe")));

                    return Mono.zip(existeRestauranteMono, clienteDTOMono)
                            .flatMap(objects -> {
                                boolean existsRestaurant = objects.getT1();
                                ClienteDTO cliente = objects.getT2();

                                if(!existsRestaurant){
                                    return Mono.error(new IllegalArgumentException("Restaurante no existe"));
                                }

                                if(pedidoRepository.findByClienteIdAndEstadoIn(cliente.getId(),
                                        new EstadoPedido[]{EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO}).isPresent()){
                                    return Mono.error(new IllegalArgumentException("Este cliente ya tiene un pedido en curso"));
                                }
                                pedido.setClienteId(cliente.getId());

                                return Mono.fromCallable(()-> restauranteClient.obtenerPlatosRestaurante(pedido.getRestauranteId(), token))
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .flatMap(listaPlatos -> {
                                            Set<String> platosDisponiblesId = listaPlatos.keySet();

                                            if(pedido.getItems() == null || pedido.getItems().isEmpty()){
                                                return Mono.error(new IllegalArgumentException("El pedido debe contener al menos un item"));
                                            }
                                            return Flux.fromIterable(pedido.getItems())
                                                    .flatMap(item -> {
                                                        String idPlatoEnString = String.valueOf(item.getPlatoId());

                                                        if(!platosDisponiblesId.contains(idPlatoEnString)) {
                                                            return Mono.error(new IllegalArgumentException("El plato con el id: " + item.getPlatoId() + "no está disponible en el restaurante"));
                                                        }

                                                        Map<String, Object> plato = (Map<String, Object>) listaPlatos.get(idPlatoEnString);

                                                        if (!Boolean.TRUE.equals(plato.get("active"))) {
                                                            return Mono.error(new IllegalArgumentException("El plato con ID " + idPlatoEnString + " no está activo en el menú."));
                                                        }

                                                        return Mono.just(item);
                                                    }).collectList()
                                                    .flatMap(itemsValidos -> {
                                                        pedido.setEstado(EstadoPedido.PENDIENTE);
                                                        pedido.setFechaCreacion(LocalDateTime.now());

                                                        return Mono.fromCallable(()-> pedidoRepository.save(pedido))
                                                                .subscribeOn(Schedulers.boundedElastic());
                                                    });
                                        });
                            });

                });
    }


    public Flux<Pedido> obtenerPedidos(){
        return Flux.fromIterable(pedidoRepository.findAll())
                .switchIfEmpty(Flux.error(new IllegalArgumentException("No hay pedidos disponibles")));
    }

    public Mono<Pedido> obtenerPedidoId(String id){
        return Mono.justOrEmpty(pedidoRepository.findById(id))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No existe el pedido")));
    }

    public Flux<Pedido> obtenerPedidosPorEstado(String token, EstadoPedido estado, Long restauranteId, int pagina, int cantidadPorPagina){
        Map<String, Object> usuario = usuarioClient.validateToken(token);
        List<Pedido> listaPedidos = null;
        String rol = (String) usuario.get("rol");


        if(!"EMPLEADO".equalsIgnoreCase(rol)){
            throw new IllegalArgumentException("No tienes permiso para ver la información");
        }

        return Flux.fromIterable(pedidoRepository.findAll())
                .filter(pedido -> pedido.getEstado().equals(estado) && pedido.getRestauranteId().equals(restauranteId))
                .skip((long) (pagina - 1) * cantidadPorPagina)
                .take(cantidadPorPagina)
                .switchIfEmpty(Flux.error(new IllegalArgumentException("No existen pedidos con ese estado en el restaurante")));
    }

    @Transactional
    public Mono<String> actualizarEstadoPedido(String token, String idPedido, String estadoPedido) {

        return Mono.fromCallable(()-> usuarioClient.validateToken(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap( usuario -> {
                    String rol = (String) usuario.get("rol");
                    Long userId = convertirUserId(usuario.get("userId"));

                    if (!"EMPLEADO".equalsIgnoreCase(rol)) {
                        return Mono.error(new IllegalArgumentException("No tienes permisos para esta acción"));
                    }

                    return Mono.justOrEmpty(pedidoRepository.findById(idPedido))
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("El pedido no existe")))
                            .flatMap(pedido -> {
                                EstadoPedido estadoPedidoAnterior = pedido.getEstado();
                                pedido.setEstado(EstadoPedido.valueOf(estadoPedido.toUpperCase()));

                                if(pedido.getEstado().equals(EstadoPedido.ENTREGADO)){
                                    pedido.setFechaEntrega(LocalDateTime.now());
                                }

                                return Mono.fromCallable(()-> pedidoRepository.save(pedido))
                                        .flatMap(savePedido -> {
                                            TrazabilidadPedido trazabilidad = new TrazabilidadPedido();
                                            trazabilidad.setPedidoId(savePedido.getId());
                                            trazabilidad.setClienteId(savePedido.getClienteId());
                                            trazabilidad.setEmpleadoId(userId);
                                            trazabilidad.setEstadoNuevo(savePedido.getEstado());
                                            trazabilidad.setEstadoAnterior(estadoPedidoAnterior);
                                            trazabilidad.setFechaCambio(LocalDateTime.now());

                                            return trazabilidadRepository.save(trazabilidad)
                                                    .thenReturn("El estado del pedido se ha actualizado exitosamente")
                                                    .onErrorResume(e -> Mono.error(new IllegalArgumentException("Error al actualizar el estado del pedido " + e.getMessage())));
                                        });
                            });
                });

    }

    @Transactional
    public Mono<String> cambiarEstadoEntregadoPedido(String token, String idPedido, String pinSeguridad) {

        return Mono.fromCallable(()-> usuarioClient.validateToken(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(usuario -> {
                    String rol = (String) usuario.get("rol");

                    if(!"EMPLEADO".equalsIgnoreCase(rol)){
                        return Mono.error( new IllegalArgumentException("No tiene permisos para realizar esta acción, solo los roles empleados"));
                    }

                    return Mono.justOrEmpty(pedidoRepository.findById(idPedido))
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("El pedido no existe")))
                            .flatMap(pedido -> {
                                if(!EstadoPedido.LISTO.equals(pedido.getEstado())){
                                    return Mono.error(new IllegalArgumentException("El pedido no está en estado LISTO"));
                                }

                                if(EstadoPedido.ENTREGADO.equals(pedido.getEstado())){
                                    return Mono.error(new IllegalArgumentException("El pedido ya ha sido entregado y no puede modificarse"));
                                }

                                if(!pinSeguridad.equals(pedido.getPinSeguridad())){
                                    return Mono.error(new IllegalArgumentException("El pin de seguridad es incorrecto"));
                                }

                                return actualizarEstadoPedido(token, pedido.getId(), EstadoPedido.ENTREGADO.toString())
                                        .thenReturn("El pedido ha sido modificado a ENTREGADO correctamente")
                                        .onErrorResume(e -> Mono.error(new RuntimeException("Error al actualizar el estado del pedido " + e.getMessage())));

                            });
                });

    }

    @Transactional
    public Mono<String> cancelarPedido(String token, String idPedido){

        return Mono.fromCallable(()-> usuarioClient.validateToken(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(usuario -> {
                    String rol = (String) usuario.get("rol");

                    if(!"CLIENTE".equalsIgnoreCase(rol)){
                        return Mono.error(new IllegalArgumentException("No tiene permisos, solo el rol tipo CLIENTE puede realizar esta acción"));
                    }

                    return Mono.justOrEmpty(pedidoRepository.findById(idPedido))
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("El pedido no existe")))
                            .flatMap(pedido -> {
                                if(!EstadoPedido.PENDIENTE.equals(pedido.getEstado())){
                                    return Mono.error(new IllegalArgumentException("Lo sentimos, su pedido ya esta EN_PREPARACION y no puede ser cancelado"));
                                }

                                return actualizarEstadoPedido(token, pedido.getId(), EstadoPedido.CANCELADO.toString())
                                        .thenReturn("El pedido fue cancelado con exito")
                                        .onErrorResume(e ->  Mono.error(new RuntimeException("Error al cancelar el pedido " + e.getMessage())));
                            });
                });

    }

    public Flux<String> obtenerEficienciaPedido(String token){

        return Mono.<Map<String, Object>>fromCallable(()-> usuarioClient.validateToken(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(usuario -> {
                    String rol = (String) usuario.get("rol");

                    if(!"PROPIETARIO".equalsIgnoreCase(rol)){
                        return Flux.error(new IllegalArgumentException("No tienes permisos para esta acción"));
                    }

                    return Flux.defer(()-> Flux.fromIterable(pedidoRepository.findAll()))
                            .subscribeOn(Schedulers.boundedElastic())
                            .filter(pedido -> pedido.getFechaEntrega() != null)
                            .map(pedido -> {
                                Duration duracion = Duration.between(pedido.getFechaCreacion(), pedido.getFechaEntrega());
                                return String.format("Pedido %s: Tiempo total %d minutos", pedido.getId(), duracion.toMinutes());
                            });
                });
    }

    public Flux<String> rankingEficienciaEmpleados(String token){

        return Mono.fromCallable(()-> usuarioClient.validateToken(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(usuario -> {
                    String rol = (String) usuario.get("rol");

                    if(!"PROPIETARIO".equalsIgnoreCase(rol)){
                        return Flux.error(new IllegalArgumentException("No tienes permisos para esta acción"));
                    }

                    return Flux.defer(()-> Flux.fromIterable(pedidoRepository.findAll()))
                            .subscribeOn(Schedulers.boundedElastic())
                            .filter(pedido -> pedido.getFechaEntrega() != null)
                            .groupBy(Pedido::getEmpleadoId)
                            .flatMap(group -> group.collectList().map(listaPedidos -> {
                                long totalTiempo = listaPedidos.stream()
                                        .mapToLong(p -> Duration.between(p.getFechaCreacion(), p.getFechaEntrega()).toMinutes())
                                        .sum();
                                long promedioTiempo = totalTiempo / listaPedidos.size();

                                return String.format("Empleado %d: Tiempo promedio de entrega %d minutos",
                                        listaPedidos.get(0).getEmpleadoId(), promedioTiempo
                                );
                            })).sort();
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
