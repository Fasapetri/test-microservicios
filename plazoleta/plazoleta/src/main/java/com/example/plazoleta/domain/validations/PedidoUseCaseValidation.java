package com.example.plazoleta.domain.validations;

import com.example.plazoleta.domain.exception.PedidoException;
import com.example.plazoleta.domain.exception.PedidoExceptionType;
import com.example.plazoleta.domain.model.*;
import com.example.plazoleta.domain.spi.IEmpleadoRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.plazoleta.domain.spi.ISecurityContextPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PedidoUseCaseValidation {


    private final IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort;
    private final IPedidoPersistencePort pedidoPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public PedidoUseCaseValidation(
            IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort,
            IPedidoPersistencePort pedidoPersistencePort, ISecurityContextPort securityContextPort) {
        this.empleadoRestaurantPersistencePort = empleadoRestaurantPersistencePort;
        this.pedidoPersistencePort = pedidoPersistencePort;
        this.securityContextPort = securityContextPort;
    }

    public void validarItemsPedido(List<ItemPedido> items) {
        if (items == null || items.isEmpty()) {
            throw new PedidoException(PedidoExceptionType.PEDIDO_NOT_ITEMS);
        }
    }

    public ItemPedido validationItemDishToPedido(List<Dish> listDishRestaurant, ItemPedido dishToPedido) {
        String convertIdDishToString = String.valueOf(dishToPedido.getPlato().getId());
        Optional<Dish> obtainDishToPedidoOpt = listDishRestaurant.stream()
                .filter(dish -> convertIdDishToString.equals(String.valueOf(dish.getId())))
                .findFirst();

        if (obtainDishToPedidoOpt.isEmpty()) {
            throw new PedidoException(PedidoExceptionType.PLATO_NOT_AVAILABLE, "ID: " + dishToPedido.getPlato().getId());
        }

        Dish obtainDishToPedido = obtainDishToPedidoOpt.get();

        if (Boolean.FALSE.equals(obtainDishToPedido.getActive())) {
            throw new PedidoException(PedidoExceptionType.PLATO_NOT_ACTIVE, "ID: " + convertIdDishToString);
        }

        return dishToPedido;
    }

    public void validateListDishResturant(List<Dish> listDishRestaurant){
        if (listDishRestaurant == null || listDishRestaurant.isEmpty()) {
            throw new PedidoException(PedidoExceptionType.DISHS_NOT_DATA_TO_RESTAURANT);
        }
    }

    public void validateListDishAvailableIsEmpty(List<ItemPedido> dishAvailableToPedido){
        if (dishAvailableToPedido.isEmpty()) {
            throw new PedidoException(PedidoExceptionType.PEDIDO_NOT_ITEMS);
        }
    }

    public void validationUserAuthenticatedBelongsRestaurant(Long userAuthenticatedId, Long restaurantId){
        if(!empleadoRestaurantPersistencePort.existsByUserIdAndRestaurantId(userAuthenticatedId, restaurantId)){
            throw new PedidoException(PedidoExceptionType.EMPLEADO_DOES_NOT_BELONGS_TO_RESTAURANT);
        }
    }

    public void validationPedidoEmpleadoAsignado(Pedido pedidoFindEmpleado, Long userAuthenticatedId){

        if(pedidoFindEmpleado.getEmpleadoId() != null && !pedidoFindEmpleado.getEmpleadoId().equals(userAuthenticatedId)){
            throw new PedidoException(PedidoExceptionType.ORDER_ALREADY_ASSIGNED_TO_EMPLOYEE);
        }

    }

    public TrazabilidadPedido createTrazabilidadToUpdateStatusPedido(Pedido updatedStatusPedido,
                                                                     Long userAuthenticatedId,
                                                                     EstadoPedido statusPedidoPrevious){

        TrazabilidadPedido newTrazabilidadUpdateStatusPedido = new TrazabilidadPedido();
        newTrazabilidadUpdateStatusPedido.setPedidoId(updatedStatusPedido.getId());
        newTrazabilidadUpdateStatusPedido.setClienteId(updatedStatusPedido.getClienteId());
        newTrazabilidadUpdateStatusPedido.setEmpleadoId(userAuthenticatedId);
        newTrazabilidadUpdateStatusPedido.setEstadoNuevo(updatedStatusPedido.getEstado());
        newTrazabilidadUpdateStatusPedido.setEstadoAnterior(statusPedidoPrevious);
        newTrazabilidadUpdateStatusPedido.setFechaCambio(LocalDateTime.now());

        return newTrazabilidadUpdateStatusPedido;
    }

    public void validationStatusPedidoActualPendiente(EstadoPedido estadoActualPedido){

        if(!EstadoPedido.PENDIENTE.equals(estadoActualPedido)){
            throw new PedidoException(PedidoExceptionType.ONLY_PENDING_ORDERS_CAN_BE_UPDATED_TO_PREPARATION);
        }
    }

    public void validationStatusPedidoActualEnPreparacion(EstadoPedido estadoActualPedido){

        if(!EstadoPedido.EN_PREPARACION.equals(estadoActualPedido)){
            throw new PedidoException(PedidoExceptionType.ONLY_PREPARATION_ORDERS_CAN_BE_UPDATED_TO_LISTO);
        }
    }

    public void validationStatusPedidoActualEnListo(EstadoPedido estadoActualPedido){
        if(!EstadoPedido.LISTO.equals(estadoActualPedido)){
            throw new PedidoException(PedidoExceptionType.ONLY_LISTO_ORDERS_CAN_BE_UPDATED_TO_ENTREGADO);
        }
    }

    public void validationPinSecurityPedido(Pedido updateStatusPedido, String pinSecurity){
        if(!pinSecurity.equals(updateStatusPedido.getPinSeguridad())){
            throw new PedidoException(PedidoExceptionType.PIN_SEGURITY_INCORRECT);
        }
    }

    public void validationUserAuthenticatedBelongsClientePedido(Long userAuthenticatedId, Pedido pedidoUpdateStatusCanceled){

        if(!userAuthenticatedId.equals(pedidoUpdateStatusCanceled.getClienteId())){
            throw new PedidoException(PedidoExceptionType.PEDIDO_NOT_OWNER_ERROR);
        }
    }

    public void validationStatusPedidoActualPendienteToCanceled(EstadoPedido estadoActualPedido){

        if(EstadoPedido.CANCELADO.equals(estadoActualPedido)){
            throw new PedidoException(PedidoExceptionType.PEDIDO_ENTREGADO_NOT_UPDATE);
        }

        if(!EstadoPedido.PENDIENTE.equals(estadoActualPedido)){
            throw new PedidoException(PedidoExceptionType.PEDIDO_IN_PREPARACION_NOT_CANCELED);
        }
    }

    public Pedido validarYObtenerPedido(Long findPedidoId){
        Long userAuthenticatedId = securityContextPort.getAuthenticatedUserId();

        Pedido pedidoUpdateToStatus = Optional.ofNullable(pedidoPersistencePort.findByIdPedido(findPedidoId))
                .orElseThrow(() -> new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO));

        this.validationUserAuthenticatedBelongsRestaurant(userAuthenticatedId, pedidoUpdateToStatus.getRestaurant().getId());
        this.validationPedidoEmpleadoAsignado(pedidoUpdateToStatus, userAuthenticatedId);

        return pedidoUpdateToStatus;
    }
}
