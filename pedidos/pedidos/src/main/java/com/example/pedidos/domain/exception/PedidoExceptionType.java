package com.example.pedidos.domain.exception;

public enum PedidoExceptionType {

    NOT_ROL_EMPLEADO("Solo los empleados pueden realizar pedidos"),
    CLIENT_PEDIDO_NOT_EXISTS("El cliente no existe"),
    RESTAURANT_NOT_EXISTS("Restaurante no existe"),
    CLIENT_STATUS_PEDIDO_IN("Este cliente ya tiene un pedido en curso"),
    PEDIDO_NOT_ITEMS("El pedido debe contener al menos un item"),
    PLATO_NOT_AVAILABLE("El plato no está disponible en el restaurante"),
    PLATO_NOT_ACTIVE("El plato no está activo en el menú."),
    FIND_NOT_DATA_PEDIDOS("No hay datos de pedidos registrados"),
    FIND_NOT_EXISTS_PEDIDO("No existe el pedido"),
    ROL_INVALID("No tienes permiso para ver la información"),
    NOT_EXISTS_PEDIDOS_STATUS_IN_RESTAURANT("No existen pedidos con ese estado en el restaurante"),
    ERROR_UPDATE_STATUS_PEDIDO("Error al actualizar el estado del pedido"),
    STATUS_PEDIDO_NOT_LISTO("El pedido no está en estado LISTO"),
    PEDIDO_ENTREGADO_NOT_UPDATE("El pedido ya ha sido entregado y no puede modificarse"),
    PIN_SEGURITY_INCORRECT("El pin de seguridad es incorrecto"),
    PEDIDO_IN_PREPARACION_NOT_CANCELED("Lo sentimos, su pedido ya esta EN_PREPARACION y no puede ser cancelado"),
    ERROR_CANCELED_PEDIDO("Error al cancelar el pedido"),
    PEDIDO_NOT_LISTO_NOTIFICATION("El pedido no esta listo para notificarlo"),
    ERROR_NOT_LOGUEO("Necesitar loguearte para cumplir esta funcion"),
    PEDIDO_NOT_OWNER_ERROR("No eres dueño del pedido que intentas cancelar");


    private final String message;

    PedidoExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
