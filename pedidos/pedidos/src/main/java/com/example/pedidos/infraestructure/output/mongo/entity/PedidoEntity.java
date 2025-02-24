package com.example.pedidos.infraestructure.output.mongo.entity;

import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.ItemPedido;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "pedidos")
@Data
public class PedidoEntity {

    @Id
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
