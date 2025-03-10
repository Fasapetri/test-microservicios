package com.example.plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemPedido {

    private Pedido pedido;

    private Dish plato;

    private int cantidad;
}
