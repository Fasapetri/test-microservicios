package com.example.pedidos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemPedido {

    private Long platoId;
    private int cantidad;
}
