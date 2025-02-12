package com.example.pedidos.model;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;
    private String name;
    private String lastName;
    private String phone;
}
