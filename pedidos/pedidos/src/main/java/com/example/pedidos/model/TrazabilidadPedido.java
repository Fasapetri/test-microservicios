package com.example.pedidos.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "trazabilidad_pedidos")
public class TrazabilidadPedido {

    @Id
    private String id;
    private String pedidoId;
    private Long clienteId;
    private Long empleadoId;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;
    private LocalDateTime fechaCambio;
}
