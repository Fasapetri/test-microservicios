package com.example.pedidos.application.dto;

import lombok.Data;

@Data
public class ClientResponse {

    private Long id;

    private String email;

    private String rol;

    private String name;

    private String lastName;

    private String phone;
}
