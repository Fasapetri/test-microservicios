package com.example.pedidos.domain.model;

import com.example.pedidos.model.EstadoPedido;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrazabilidadPedido {

    private String id;
    private String pedidoId;
    private Long clienteId;
    private Long empleadoId;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;
    private LocalDateTime fechaCambio;
}
