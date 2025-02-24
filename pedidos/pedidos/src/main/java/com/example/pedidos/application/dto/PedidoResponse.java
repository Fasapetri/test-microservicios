package com.example.pedidos.application.dto;

import com.example.pedidos.domain.model.EstadoPedido;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {

    private Long clienteId;

    private Long empleadoId;

    private Long restauranteId;

    private List<ItemPedidoResponse> items;

    private EstadoPedido estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;
}
