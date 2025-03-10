package com.example.pedidos.domain.validations;

import com.example.pedidos.domain.constants.PedidoUseCaseConstants;
import com.example.pedidos.domain.exception.PedidoException;
import com.example.pedidos.domain.exception.PedidoExceptionType;
import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.ItemPedido;
import com.example.pedidos.domain.model.Pedido;
import com.example.pedidos.domain.model.TrazabilidadPedido;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PedidoUseCaseValidation {

    public TrazabilidadPedido createTrazabilidadToUpdateStatusPedido(Pedido updatedStatusPedido, Long userAuthenticatedId, EstadoPedido statusPedidoPrevious){

        TrazabilidadPedido newTrazabilidadUpdateStatusPedido = new TrazabilidadPedido();
        newTrazabilidadUpdateStatusPedido.setPedidoId(updatedStatusPedido.getId());
        newTrazabilidadUpdateStatusPedido.setClienteId(updatedStatusPedido.getClienteId());
        newTrazabilidadUpdateStatusPedido.setEmpleadoId(userAuthenticatedId);
        newTrazabilidadUpdateStatusPedido.setEstadoNuevo(updatedStatusPedido.getEstado());
        newTrazabilidadUpdateStatusPedido.setEstadoAnterior(statusPedidoPrevious);
        newTrazabilidadUpdateStatusPedido.setFechaCambio(LocalDateTime.now());

        return newTrazabilidadUpdateStatusPedido;
    }

    public Mono<Void> validationUpdateStatusEntregadoPedido(Pedido pedidoUpdateStatusToEntregado, String smsPinSecurityRetirePedido){
        if(!EstadoPedido.LISTO.equals(pedidoUpdateStatusToEntregado.getEstado())){
            return Mono.error(new PedidoException(PedidoExceptionType.STATUS_PEDIDO_NOT_LISTO));
        }

        if(EstadoPedido.ENTREGADO.equals(pedidoUpdateStatusToEntregado.getEstado())){
            return Mono.error(new PedidoException(PedidoExceptionType.PEDIDO_ENTREGADO_NOT_UPDATE));
        }

        if(!smsPinSecurityRetirePedido.equals(pedidoUpdateStatusToEntregado.getPinSeguridad())){
            return Mono.error(new PedidoException(PedidoExceptionType.PIN_SEGURITY_INCORRECT));
        }

        return Mono.empty();
    }

    public Mono<ItemPedido> validationItemDishToPedido(List<Object> listDishRestaurant, ItemPedido dishToPedido){

        String convertIdDishToString = String.valueOf(dishToPedido.getPlatoId());

        Optional<Object> obtainDishToPedidoOpt = listDishRestaurant.stream()
                .filter(dish -> {

                    if (!(dish instanceof Map)) {
                            return false;
                    }

                    Map<String, Object> dishMap = (Map<String, Object>) dish;
                    return convertIdDishToString.equals(String.valueOf(dishMap.get("id")));

                })
                .findFirst();

        if (obtainDishToPedidoOpt.isEmpty()) {
            return Mono.error(new PedidoException(PedidoExceptionType.PLATO_NOT_AVAILABLE, "ID: " + dishToPedido.getPlatoId()));
        }

        Map<String, Object> obtainDishToPedido = (Map<String, Object>) obtainDishToPedidoOpt.get();

        if (!Boolean.TRUE.equals(obtainDishToPedido.get(PedidoUseCaseConstants.DISH_ACTIVE_KEY))) {
            return Mono.error(new PedidoException(PedidoExceptionType.PLATO_NOT_ACTIVE, "ID: " + convertIdDishToString));
        }
        return Mono.just(dishToPedido);
    }

    public Mono<Void> validarRolEmpleado(String userAuthenticatedRol) {
        if (!PedidoUseCaseConstants.ROLE_EMPLOYEE.equalsIgnoreCase(userAuthenticatedRol)) {
            return Mono.error(new PedidoException(PedidoExceptionType.NOT_ROL_EMPLEADO));
        }
        return Mono.empty();
    }

    public Mono<Void> validarExistenciaRestaurante(boolean existsRestaurantToPedido) {
        return existsRestaurantToPedido
                ? Mono.empty()
                : Mono.error(new PedidoException(PedidoExceptionType.RESTAURANT_NOT_EXISTS));
    }

    public Mono<Void> validarItemsPedido(List<ItemPedido> items) {
        return (items == null || items.isEmpty())
                ? Mono.error(new PedidoException(PedidoExceptionType.PEDIDO_NOT_ITEMS))
                : Mono.empty();
    }
}
