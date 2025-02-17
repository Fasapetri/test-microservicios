package com.example.pedidos.domain.model;

import com.example.pedidos.model.EstadoPedido;
import com.example.pedidos.model.ItemPedido;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Pedido {

    private String id;
    private Long clienteId;
    private Long empleadoId;
    private Long restauranteId;
    private List<ItemPedido> items;
    private EstadoPedido estado;
    private String pinSeguridad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;
}
