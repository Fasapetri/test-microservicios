package com.example.pedidos.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {

    private Long clienteId;

    private Long restauranteId;

    private List<ItemPedidoRequest> items;
}
