package com.example.plazoleta.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {

    private Long restauranteId;

    private List<ItemPedidoRequest> items;
}
