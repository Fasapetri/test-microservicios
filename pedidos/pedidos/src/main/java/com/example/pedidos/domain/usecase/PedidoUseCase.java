package com.example.pedidos.domain.usecase;

import com.example.pedidos.domain.api.IPedidoServicePort;
import com.example.pedidos.domain.constants.PedidoUseCaseConstants;
import com.example.pedidos.domain.exception.PedidoException;
import com.example.pedidos.domain.exception.PedidoExceptionType;
import com.example.pedidos.domain.model.Client;
import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.Pedido;
import com.example.pedidos.domain.model.TrazabilidadPedido;
import com.example.pedidos.domain.spi.*;
import com.example.pedidos.domain.validations.PedidoUseCaseValidation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final ITrazabilidadPersistencePort trazabilidadPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IUserClientServicePort userClientServicePort;
    private final ISmsServicePort smsServicePort;
    private final ISecurityContextPort securityContextPort;
    private final PedidoUseCaseValidation pedidoUseCaseValidation;

    public PedidoUseCase(IPedidoPersistencePort iPedidoPersistencePort,
                         ITrazabilidadPersistencePort iTrazabilidadPersistencePort,
                         IRestaurantServicePort iRestaurantServicePort, IUserClientServicePort iClientServicePort,
                         ISmsServicePort iSmsServicePort, ISecurityContextPort securityContextPort, PedidoUseCaseValidation pedidoUseCaseValidation) {
        this.pedidoPersistencePort = iPedidoPersistencePort;
        this.trazabilidadPersistencePort = iTrazabilidadPersistencePort;
        this.restaurantServicePort = iRestaurantServicePort;
        this.userClientServicePort = iClientServicePort;
        this.smsServicePort = iSmsServicePort;
        this.securityContextPort = securityContextPort;
        this.pedidoUseCaseValidation = pedidoUseCaseValidation;
    }

    @Override
    public Mono<Pedido> savePedido(Pedido pedidoToCreate) {
        return Mono.zip(
                securityContextPort.getUserAuthenticateRol(),
                securityContextPort.getAuthenticatedUserId()
        ).flatMap(tuple -> {
                    Long userAuthenticatedId = tuple.getT2();
                    String userAuthenticatedRol = tuple.getT1();

                    return pedidoUseCaseValidation.validarRolEmpleado(userAuthenticatedRol)
                            .then(Mono.defer(() -> {

                                pedidoToCreate.setEmpleadoId(userAuthenticatedId);

                                return Mono.zip(restaurantServicePort.existsRestaurant(pedidoToCreate.getRestauranteId()),
                                                userClientServicePort.findDataClient(pedidoToCreate.getClienteId()).switchIfEmpty(
                                                        Mono.error(new PedidoException(PedidoExceptionType.CLIENT_PEDIDO_NOT_EXISTS))
                                                ))
                                        .flatMap(objects -> {
                                            boolean existsRestaurantToPedido = objects.getT1();
                                            Client foundClienteToPedido = objects.getT2();

                                            return pedidoUseCaseValidation.validarExistenciaRestaurante(existsRestaurantToPedido)
                                                    .then(Mono.defer(() -> {
                                                        pedidoToCreate.setClienteId(foundClienteToPedido.getId());

                                                        return pedidoPersistencePort.findByClienteIdAndEstadoIn(foundClienteToPedido.getId(), new EstadoPedido[]{EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO})
                                                                .flatMap(foundPedidoUserStatusActive -> Mono.<Pedido>error(new PedidoException(PedidoExceptionType.CLIENT_STATUS_PEDIDO_IN)))
                                                                .switchIfEmpty(Mono.defer(() ->
                                                                        restaurantServicePort.findDishsRestaurant(pedidoToCreate.getRestauranteId())
                                                                                .switchIfEmpty(Mono.error(new PedidoException(PedidoExceptionType.RESTAURANT_NOT_EXISTS)))
                                                                                .flatMap(listDishRestaurant ->

                                                                                    pedidoUseCaseValidation.validarItemsPedido(pedidoToCreate.getItems())
                                                                                            .then(Mono.defer(() ->
                                                                                                Flux.fromIterable(pedidoToCreate.getItems())
                                                                                                        .flatMap(dishToPedido ->
                                                                                                                pedidoUseCaseValidation.validationItemDishToPedido(listDishRestaurant, dishToPedido)
                                                                                                        ).collectList()
                                                                                                        .flatMap(dishAvailableToPedido -> {

                                                                                                            if(dishAvailableToPedido.isEmpty()){
                                                                                                                return Mono.error(new PedidoException(PedidoExceptionType.PEDIDO_NOT_ITEMS));
                                                                                                            }

                                                                                                            pedidoToCreate.setEstado(EstadoPedido.PENDIENTE);
                                                                                                            pedidoToCreate.setFechaCreacion(LocalDateTime.now());
                                                                                                            pedidoToCreate.setItems(dishAvailableToPedido);

                                                                                                            return pedidoPersistencePort.savePedido(pedidoToCreate);
                                                                                                        })
                                                                                            ))

                                                                                )
                                                                ));

                                                    }));



                                        });
                            }));

                });
    }

    @Override
    public Flux<Pedido> findAllPedidos() {
        return pedidoPersistencePort.findAllPedidos()
                .switchIfEmpty(Flux.error(new PedidoException(PedidoExceptionType.FIND_NOT_DATA_PEDIDOS)));
    }

    @Override
    public Mono<Pedido> findByIdPedido(String findPedidoId) {
        return pedidoPersistencePort.findByIdPedido(findPedidoId)
                .switchIfEmpty(Mono.error(new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO)));
    }

    @Override
    public Flux<Pedido> findByStatusPedido(EstadoPedido findEstadoPedido, Long findRestaurantId, int pagina, int cantidadPorPagina) {
        return securityContextPort.getUserAuthenticateRol()
                .flatMapMany(userAuthenticatedRol -> {
                    
                    if(!PedidoUseCaseConstants.ROLE_EMPLOYEE.equalsIgnoreCase(userAuthenticatedRol)){
                        throw new PedidoException(PedidoExceptionType.ROL_INVALID);
                    }

                    return pedidoPersistencePort.findAllPedidos()
                            .filter(pedido -> pedido.getEstado().equals(findEstadoPedido) && pedido.getRestauranteId().equals(findRestaurantId))
                            .skip((long) (pagina - 1) * cantidadPorPagina)
                            .take(cantidadPorPagina)
                            .switchIfEmpty(Flux.error(new PedidoException(PedidoExceptionType.NOT_EXISTS_PEDIDOS_STATUS_IN_RESTAURANT)));
                });
    }

    @Override
    public Mono<String> updateStatusPedido(String findPedidoId, String newStatusPedido) {
        return Mono.zip(
                securityContextPort.getUserAuthenticateRol(),
                securityContextPort.getAuthenticatedUserId()
                )
                .flatMap( tuple -> {

                    String userAuthenticatedRol = tuple.getT1();
                    Long userAuthenticatedId = tuple.getT2();
                    EstadoPedido newStatus = EstadoPedido.valueOf(newStatusPedido.toUpperCase());

                    if (!PedidoUseCaseConstants.ROLE_EMPLOYEE.equalsIgnoreCase(userAuthenticatedRol) && !EstadoPedido.CANCELADO.equals(newStatus)) {
                        return Mono.error(new PedidoException(PedidoExceptionType.ROL_INVALID));
                    }

                    return pedidoPersistencePort.findByIdPedido(findPedidoId)
                            .switchIfEmpty(Mono.error(new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO)))
                            .flatMap(pedidoUpdateToStatus -> {
                                EstadoPedido statusPedidoPrevious = pedidoUpdateToStatus.getEstado();
                                pedidoUpdateToStatus.setEstado(newStatus);
                                Mono<Pedido> resultSendSmsClientPedidoListo = Mono.just(pedidoUpdateToStatus);

                                if(pedidoUpdateToStatus.getEstado().equals(EstadoPedido.ENTREGADO)){
                                    pedidoUpdateToStatus.setFechaEntrega(LocalDateTime.now());
                                }

                                if(pedidoUpdateToStatus.getEstado().equals(EstadoPedido.LISTO)){
                                     resultSendSmsClientPedidoListo = this.updateStatusListo(pedidoUpdateToStatus);
                                }

                                return resultSendSmsClientPedidoListo
                                        .flatMap(pedidoPersistencePort::updateStatusPedido)
                                        .flatMap(updatedStatusPedido -> {

                                            TrazabilidadPedido newTrazabilidadUpdateStatusPedido = pedidoUseCaseValidation.createTrazabilidadToUpdateStatusPedido(updatedStatusPedido, userAuthenticatedId, statusPedidoPrevious);

                                            return trazabilidadPersistencePort.saveTrazabilidad(newTrazabilidadUpdateStatusPedido)
                                                    .thenReturn(PedidoUseCaseConstants.ORDER_STATUS_UPDATED_SUCCESS + statusPedidoPrevious + " a " + updatedStatusPedido.getEstado())
                                                    .onErrorResume(e -> Mono.error(new PedidoException(PedidoExceptionType.ERROR_UPDATE_STATUS_PEDIDO, "ERROR: " + e.getMessage())));
                                        });
                            });
                });
    }

    @Override
    public Mono<String> updateStatusEntregadoPedido(String findPedidoId, String smsPinSecurityRetirePedido) {
        return securityContextPort.getUserAuthenticateRol()
                .flatMap(userAuthenticatedRol -> {

                    if(!PedidoUseCaseConstants.ROLE_EMPLOYEE.equalsIgnoreCase(userAuthenticatedRol)){
                        return Mono.error( new PedidoException(PedidoExceptionType.ROL_INVALID));
                    }

                    return pedidoPersistencePort.findByIdPedido(findPedidoId)
                            .switchIfEmpty(Mono.error(new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO)))
                            .flatMap(pedidoUpdateStatusToEntregado ->

                               pedidoUseCaseValidation.validationUpdateStatusEntregadoPedido(pedidoUpdateStatusToEntregado, smsPinSecurityRetirePedido)
                                        .then(updateStatusPedido(pedidoUpdateStatusToEntregado.getId(), EstadoPedido.ENTREGADO.toString()))
                                        .thenReturn(PedidoUseCaseConstants.ORDER_UPDATED_TO_DELIVERED_SUCCESS)

                            ).onErrorResume(e -> Mono.error(new PedidoException(PedidoExceptionType.ERROR_UPDATE_STATUS_PEDIDO, "ERROR: " + e.getMessage())));
                });
    }

    @Override
    public Mono<String> canceledPedido(String findPedidoId) {
        return Mono.zip(
                securityContextPort.getUserAuthenticateRol(),
                securityContextPort.getAuthenticatedUserId()
                )
                .flatMap(tuple -> {

                    String userAuthenticatedRol = tuple.getT1();
                    Long userAuthenticatedId = tuple.getT2();

                    if(!PedidoUseCaseConstants.ROLE_CLIENT.equalsIgnoreCase(userAuthenticatedRol)){
                        return Mono.error(new PedidoException(PedidoExceptionType.ROL_INVALID));
                    }

                    return pedidoPersistencePort.findByIdPedido(findPedidoId)
                            .switchIfEmpty(Mono.error(new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO)))
                            .flatMap(pedidoUpdateStatusToCanceled -> {
                                if(!EstadoPedido.PENDIENTE.equals(pedidoUpdateStatusToCanceled.getEstado())){
                                    return Mono.error(new PedidoException(PedidoExceptionType.PEDIDO_IN_PREPARACION_NOT_CANCELED));
                                }

                                if(!userAuthenticatedId.equals(pedidoUpdateStatusToCanceled.getClienteId())){
                                    return Mono.error(new PedidoException(PedidoExceptionType.PEDIDO_NOT_OWNER_ERROR));
                                }

                                pedidoUpdateStatusToCanceled.setEstado(EstadoPedido.CANCELADO);

                                return updateStatusPedido(pedidoUpdateStatusToCanceled.getId(), EstadoPedido.CANCELADO.toString())
                                        .thenReturn(PedidoUseCaseConstants.ORDER_CANCELED_SUCCESS)
                                        .onErrorResume(e ->  Mono.error(new PedidoException(PedidoExceptionType.ERROR_CANCELED_PEDIDO)));
                            });
                });
    }

    @Override
    public Flux<String> obtainPedidoEfficiency() {
        return securityContextPort.getUserAuthenticateRol()
                .flatMapMany(userAuthenticatedRol -> {

                    if(!PedidoUseCaseConstants.ROLE_PROPIETARIO.equalsIgnoreCase(userAuthenticatedRol)){
                        return Flux.error(new PedidoException(PedidoExceptionType.ROL_INVALID));
                    }

                    return findAllPedidos()
                            .filter(pedidoFilter -> pedidoFilter.getFechaEntrega() != null)
                            .map(pedido -> {
                                Duration duracion = Duration.between(pedido.getFechaCreacion(), pedido.getFechaEntrega());
                                return String.format(PedidoUseCaseConstants.ORDER_TOTAL_TIME, pedido.getId(), duracion.toMinutes());
                            });
                });
    }

    @Override
    public Flux<String> employeeEfficiencyRanking() {
        return securityContextPort.getUserAuthenticateRol()
                .flatMapMany(userAuthenticatedRol -> {

                    if(!PedidoUseCaseConstants.ROLE_PROPIETARIO.equalsIgnoreCase(userAuthenticatedRol)){
                        return Flux.error(new PedidoException(PedidoExceptionType.ROL_INVALID));
                    }

                    return findAllPedidos()
                            .filter(pedidoFilter -> pedidoFilter.getFechaEntrega() != null)
                            .groupBy(Pedido::getEmpleadoId)
                            .flatMap(group -> group.collectList().map(listaPedidos -> {
                                long totalTiempo = listaPedidos.stream()
                                        .mapToLong(p -> Duration.between(p.getFechaCreacion(), p.getFechaEntrega()).toMinutes())
                                        .sum();
                                long promedioTiempo = totalTiempo / listaPedidos.size();

                                return String.format(PedidoUseCaseConstants.EMPLOYEE_AVERAGE_DELIVERY_TIME,
                                        listaPedidos.get(0).getEmpleadoId(), promedioTiempo
                                );
                            })).sort();
                });
    }

    private Mono<Pedido> updateStatusListo(Pedido pedidoToUpdateStatus){
        if(!EstadoPedido.LISTO.equals(pedidoToUpdateStatus.getEstado())){
            return Mono.error(new PedidoException(PedidoExceptionType.PEDIDO_NOT_LISTO_NOTIFICATION));
        }

        return userClientServicePort.findDataClient(pedidoToUpdateStatus.getClienteId())
                .flatMap(cliente -> {

                    String pingSeguridad = generarPin();
                    String mensaje = String.format(
                            PedidoUseCaseConstants.ORDER_READY_WITH_PIN,
                            pingSeguridad
                    );

                    pedidoToUpdateStatus.setPinSeguridad(pingSeguridad);

                    return pedidoPersistencePort.savePedido(pedidoToUpdateStatus)
                            .then(smsServicePort.sendSms(cliente.getPhone(), mensaje))
                            .thenReturn(pedidoToUpdateStatus);
                });
    }

    private String generarPin(){
        return String.format(PedidoUseCaseConstants.PIN_FORMAT, ThreadLocalRandom.current().nextInt(PedidoUseCaseConstants.PIN_MAX_VALUE));
    }

}
