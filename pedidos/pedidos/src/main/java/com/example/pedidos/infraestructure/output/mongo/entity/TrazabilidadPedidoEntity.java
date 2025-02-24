package com.example.pedidos.infraestructure.output.mongo.entity;

import com.example.pedidos.domain.model.EstadoPedido;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "trazabilidad")
@Data
public class TrazabilidadPedidoEntity {

    @Id
    private String id;
    private String pedidoId;
    private Long clienteId;
    private Long empleadoId;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;
    private LocalDateTime fechaCambio;
}
