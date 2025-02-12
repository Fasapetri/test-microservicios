package com.example.pedidos.service;

import com.example.pedidos.client.RestauranteClient;
import com.example.pedidos.client.UsuarioClient;
import com.example.pedidos.model.EstadoPedido;
import com.example.pedidos.model.ItemPedido;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.model.TrazabilidadPedido;
import com.example.pedidos.repository.PedidoRepository;
import com.example.pedidos.repository.TrazabilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .flatMap(usuario -> {
                    Object objectId = usuario.get("userId");
                    Long userId;
                    String rol = (String) usuario.get("rol");

                    if(objectId instanceof Integer){
                        userId = ((Integer) objectId).longValue();
                    } else if(objectId instanceof Long){
                        userId = (Long) objectId;
                    } else {
                        return Mono.error(new IllegalArgumentException("Tipo de userId desconocido " + objectId.getClass().getName()));
                    }

                    if(!"EMPLEADO".equalsIgnoreCase(rol)){
                        return Mono.error(new IllegalArgumentException("Solo los empleados pueden realizar pedido"));
                    }

                    pedido.setEmpleadoId(userId);

                    return Mono.fromCallable(() -> restauranteClient.existeRestaurante(pedido.getRestauranteId(), token))
                            .flatMap(existe -> {
                                if(!existe){
                                    return Mono.error(new IllegalArgumentException("Restaurante no existe"));
                                }

                                return Mono.fromCallable(() -> usuarioClient.findById(pedido.getClienteId(), token))
                                        .switchIfEmpty(Mono.error(new IllegalArgumentException("El cliente no existe")))
                                        .flatMap(clienteDTO -> {
                                                if(pedidoRepository.findByClienteIdAndEstadoIn(
                                                        clienteDTO.getId(),
                                                        new EstadoPedido[]{EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO}).isPresent()){
                                                    return Mono.error(new IllegalArgumentException("Este cliente ya tiene un pedido en curso"));
                                                }
                                                pedido.setClienteId(clienteDTO.getId());
                                                return Mono.fromCallable(() -> restauranteClient.obtenerPlatosRestaurante(pedido.getRestauranteId(), token))
                                                        .flatMap(listaPlatos -> {
                                                            Set<String> platosDisponiblesIds = listaPlatos.keySet();

                                                            if(pedido.getItems() == null || pedido.getItems().isEmpty()){
                                                                return Mono.error(new IllegalArgumentException("El pedido debe contener al menos un item"));
                                                            }

                                                            for(ItemPedido item: pedido.getItems()){
                                                                String idPlatoEnString = String.valueOf(item.getPlatoId());
                                                                if (!platosDisponiblesIds.contains(idPlatoEnString)){
                                                                    return Mono.error(new RuntimeException("el plato con el id " + item.getPlatoId() + " no esta disponible en el restaurante"));
                                                                }
                                                                Map<String, Object> plato = (Map<String, Object>) listaPlatos.get(idPlatoEnString);

                                                                if (plato.containsKey("active") && !(Boolean) plato.get("active")) {
                                                                    return Mono.error(new RuntimeException("El plato con ID " + plato.get("active") + " no está activo en el menú."));
                                                                }
                                                            }

                                                            pedido.setEstado(EstadoPedido.PENDIENTE);
                                                            pedido.setFechaCreacion(LocalDateTime.now());
                                                            return Mono.fromCallable(() -> pedidoRepository.save(pedido));
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
        Map<String, Object> usuario = usuarioClient.validateToken(token);
        String rol = (String) usuario.get("rol");

        Object objectId = usuario.get("userId");
        Long userId;

        if(objectId instanceof Integer){
            userId = ((Integer) objectId).longValue();
        } else if(objectId instanceof Long){
            userId = (Long) objectId;
        } else {
            throw new IllegalArgumentException("Tipo de userId desconocido: " + objectId.getClass().getName());
        }

        if (!"EMPLEADO".equalsIgnoreCase(rol)) {
            return Mono.error(new IllegalArgumentException("No tienes permisos para esta acción"));
        }

        return Mono.justOrEmpty(pedidoRepository.findById(idPedido))
                .flatMap(pedido -> {

                    EstadoPedido estadoAnterior = pedido.getEstado();
                    pedido.setEstado(EstadoPedido.valueOf(estadoPedido.toUpperCase()));

                    if(pedido.getEstado().equals(EstadoPedido.ENTREGADO)){
                        pedido.setFechaEntrega(LocalDateTime.now());
                    }

                    return Mono.fromCallable(() -> pedidoRepository.save(pedido))
                            .flatMap(savePedido -> {
                                TrazabilidadPedido trazabilidad = new TrazabilidadPedido();
                                trazabilidad.setPedidoId(savePedido.getId());
                                trazabilidad.setClienteId(savePedido.getClienteId());
                                trazabilidad.setEmpleadoId(userId);
                                trazabilidad.setEstadoNuevo(savePedido.getEstado());
                                trazabilidad.setEstadoAnterior(estadoAnterior);
                                trazabilidad.setFechaCambio(LocalDateTime.now());

                                return trazabilidadRepository.save(trazabilidad)
                                        .thenReturn("El estado del pedido se ha actualizado correctamente");
                            }).onErrorResume(e -> Mono.error(new RuntimeException("Error al actualizar el pedido  " + e.getMessage())));
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("El pedido no existe")));


    }

    @Transactional
    public Mono<String> cambiarEstadoEntregadoPedido(String token, String idPedido, String pinSeguridad) {
        Map<String, Object> usuario = usuarioClient.validateToken(token);
        String rol = (String) usuario.get("rol");

        if(!"EMPLEADO".equalsIgnoreCase(rol)){
          return Mono.error( new IllegalArgumentException("No tiene permisos para realizar esta acción, solo los roles empleados"));
        }

        return Mono.justOrEmpty(pedidoRepository.findById(idPedido))
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

                    return Mono.fromCallable(() -> actualizarEstadoPedido(token, pedido.getId(), EstadoPedido.ENTREGADO.toString()))
                            .thenReturn("El pedido ha sido modificado a ENTREGADO correctamente")
                            .onErrorResume(e -> Mono.error(new RuntimeException("Error al actualizar el estado del pedido " + e.getMessage())));

                }).switchIfEmpty(Mono.error(new IllegalArgumentException("pedido no encontrado")));
    }

    @Transactional
    public Mono<String> cancelarPedido(String token, String idPedido){

        Map<String, Object> usuario = usuarioClient.validateToken(token);
        String rol = (String) usuario.get("rol");

        if(!"CLIENTE".equalsIgnoreCase(rol)){
            return Mono.error(new IllegalArgumentException("No tiene permisos, solo el rol tipo CLIENTE puede realizar esta acción"));
        }

        return Mono.justOrEmpty(pedidoRepository.findById(idPedido))
                .flatMap(pedido -> {
                    if(!EstadoPedido.PENDIENTE.equals(pedido.getEstado())){
                        return Mono.error(new IllegalArgumentException("Lo sentimos, su pedido ya esta EN_PREPARACION y no puede ser cancelado"));
                    }

                    return Mono.fromCallable(() -> actualizarEstadoPedido(token, pedido.getId(), EstadoPedido.CANCELADO.toString()))
                            .thenReturn("El pedido fue cancelado con exito")
                            .onErrorResume(e ->  Mono.error(new RuntimeException("Error al cancelar el pedido " + e.getMessage())));
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Pedido no encontrado")));
    }

    public Flux<String> obtenerEficienciaPedido(String token){
        Map<String, Object> usuario = usuarioClient.validateToken(token);
        String rol = (String) usuario.get("rol");

        if(!"PROPIETARIO".equalsIgnoreCase(rol)){
            return Flux.error(new IllegalArgumentException("No tienes permisos para esta acción"));
        }

        return Flux.fromIterable(pedidoRepository.findAll())
                .filter(pedido -> pedido.getFechaEntrega() != null)
                .map(pedido -> {
                    Duration duracion = Duration.between(pedido.getFechaCreacion(), pedido.getFechaEntrega());
                    return String.format("Pedido %s: Tiempo total %d minutos", pedido.getId(), duracion.toMinutes());
                });
    }

    public Flux<String> rankingEficienciaEmpleados(String token){
        Map<String, Object> usuario = usuarioClient.validateToken(token);
        String rol = (String) usuario.get("rol");

        if(!"PROPIETARIO".equalsIgnoreCase(rol)){
            return Flux.error(new IllegalArgumentException("No tienes permisos para esta acción"));
        }

        return Flux.fromIterable(pedidoRepository.findAll())
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
    }
}
