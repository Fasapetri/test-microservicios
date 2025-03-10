package com.example.plazoleta.infraestructure.constants;

public class PedidoRestControllerConstants {

    public static final String PEDIDOS_BASE = "api/pedidos";
    public static final String PEDIDO_SAVE = "/save";
    public static final String PEDIDO_LIST_STATUS = "/list-status";
    public static final String PEDIDO_UPDATE_STATUS_PREPARACION = "/update-status-preparacion";
    public static final String PEDIDO_UPDATE_STATUS_LISTO = "/update-status-listo";
    public static final String PEDIDO_UPDATE_STATUS_ENTREGADO = "/update-status-entregado";
    public static final String PEDIDO_UPDATE_STATUS_CANCELADO = "/update-status-cancelado";
    public static final String PEDIDO_EFFICIENCY = "/efficiency";
    public static final String PEDIDO_RANKING_EMPLOYEE = "/ranking-employee";
    public static final String PEDIDO_TRAZABILIDAD = "/trazabilidad";
    public static final String PEDIDO_HAS_AUTHORITY_EMPLEADO = "hasAuthority('EMPLEADO')";
    public static final String PEDIDO_HAS_AUTHORITY_CLIENTE = "hasAuthority('CLIENTE')";
    public static final String PEDIDO_HAS_AUTHORITY_PROPIETARIO = "hasAuthority('PROPIETARIO')";


    private PedidoRestControllerConstants() {} //
}
