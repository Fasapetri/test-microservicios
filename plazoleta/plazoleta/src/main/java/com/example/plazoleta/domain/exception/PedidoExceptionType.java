package com.example.plazoleta.domain.exception;

public enum PedidoExceptionType {

    RESTAURANT_NOT_EXISTS("Restaurante no existe"),
    CLIENT_STATUS_PEDIDO_IN("Este cliente ya tiene un pedido en curso"),
    PEDIDO_NOT_ITEMS("El pedido debe contener al menos un item"),
    DISHS_NOT_DATA_TO_RESTAURANT("No existen platos registrados en este restaurante"),
    PLATO_NOT_AVAILABLE("El plato no está disponible en el restaurante"),
    PLATO_NOT_ACTIVE("El plato no está activo en el menú."),
    FIND_NOT_EXISTS_PEDIDO("No existe el pedido"),
    NOT_EXISTS_PEDIDOS_STATUS_IN_RESTAURANT("No existen pedidos con ese estado en el restaurante"),
    ERROR_UPDATE_STATUS_PEDIDO("Error al actualizar el estado del pedido"),
    PEDIDO_ENTREGADO_NOT_UPDATE("El pedido ya ha sido entregado y no puede modificarse"),
    PIN_SEGURITY_INCORRECT("El pin de seguridad es incorrecto"),
    PEDIDO_IN_PREPARACION_NOT_CANCELED("Lo sentimos, su pedido ya esta EN_PREPARACION y no puede ser cancelado"),
    ERROR_CANCELED_PEDIDO("Error al cancelar el pedido"),
    PEDIDO_NOT_OWNER_ERROR("Acción cancelada, el pedido no le pertenece"),
    EMPLEADO_DOES_NOT_BELONGS_TO_RESTAURANT("El empleado no pertenece a ese restaurante"),
    ORDER_ALREADY_ASSIGNED_TO_EMPLOYEE("El pedido ya tiene asociado a un empleado y no puede modificarlo"),
    ONLY_PENDING_ORDERS_CAN_BE_UPDATED_TO_PREPARATION("Solo los pedidos con estado PENDIENTE se puede modificar a estado EN_PREPARACION"),
    ONLY_PREPARATION_ORDERS_CAN_BE_UPDATED_TO_LISTO("Solo los pedidos con estado EN_PREPARACION se puede modificar a estado LISTO"),
    ONLY_LISTO_ORDERS_CAN_BE_UPDATED_TO_ENTREGADO("Solo los pedidos con estado LISTO se puede modificar a estado ENTREGADO");










    private final String message;

    PedidoExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
