package com.example.plazoleta.application.dto;

import com.example.plazoleta.domain.model.EstadoPedido;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {

    private Long pedidoId;

    private Long clienteId;

    private Long empleadoId;

    private Long restauranteId;

    private String pinSeguridad;

    private List<ItemPedidoResponse> items;

    private EstadoPedido estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;
}
