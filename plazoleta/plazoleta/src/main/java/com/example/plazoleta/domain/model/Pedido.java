package com.example.plazoleta.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Pedido {

    private Long id;

    private Long clienteId;

    private Long empleadoId;

    private Restaurant restaurant;

    private List<ItemPedido> items;

    private EstadoPedido estado;

    private String pinSeguridad;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;
}
