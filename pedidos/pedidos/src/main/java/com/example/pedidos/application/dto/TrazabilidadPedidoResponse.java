package com.example.pedidos.application.dto;

import com.example.pedidos.domain.model.EstadoPedido;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrazabilidadPedidoResponse {

    private String pedidoId;

    private Long clienteId;

    private Long empleadoId;

    private EstadoPedido estadoAnterior;

    private EstadoPedido estadoNuevo;

    private LocalDateTime fechaCambio;
}
