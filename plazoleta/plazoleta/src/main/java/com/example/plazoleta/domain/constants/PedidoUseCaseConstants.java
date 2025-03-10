package com.example.plazoleta.domain.constants;

public class PedidoUseCaseConstants {

    public static final String ROLE_EMPLOYEE = "EMPLEADO";
    public static final String DISH_ACTIVE_KEY = "active";
    public static final String ORDER_STATUS_UPDATED_SUCCESS = "El estado del pedido se ha actualizado exitosamente";
    public static final String ORDER_UPDATED_TO_DELIVERED_SUCCESS = "El pedido ha sido modificado a ENTREGADO correctamente";
    public static final String ROLE_CLIENT = "CLIENTE";
    public static final String ORDER_CANCELED_SUCCESS = "El pedido fue cancelado con éxito";
    public static final String ROLE_PROPIETARIO = "PROPIETARIO";
    public static final String ORDER_READY_WITH_PIN = "Su pedido está listo. Use el PIN: %s para reclamarlo";
    public static final String PIN_FORMAT = "%04d";
    public static final int PIN_MAX_VALUE = 10000;
    public static final String ORDER_TOTAL_TIME = "Pedido %s: Tiempo total %d minutos";
    public static final String EMPLOYEE_AVERAGE_DELIVERY_TIME = "Empleado %d: Tiempo promedio de entrega %d minutos";
    public static final String ERROR_PREFIX = "ERROR: ";
    public static final String EMPLOYEE_AVERAGE_DELIVERY_TIME_SPLIT = ": Tiempo promedio de entrega ";
    public static final String MINUTES_CONSTANTS = " Minutos";

    private PedidoUseCaseConstants(){}


}
