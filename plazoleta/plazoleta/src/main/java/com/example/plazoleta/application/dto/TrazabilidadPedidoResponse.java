package com.example.plazoleta.application.dto;

import com.example.plazoleta.domain.model.EstadoPedido;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrazabilidadPedidoResponse {

    private Long pedidoId;

    private Long empleadoId;

    private EstadoPedido estadoAnterior;

    private EstadoPedido estadoNuevo;

    private LocalDateTime fechaCambio;
}
