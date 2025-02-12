package com.example.pedidos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "pedidos")
public class Pedido {

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
