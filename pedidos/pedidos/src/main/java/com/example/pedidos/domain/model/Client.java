package com.example.pedidos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client {

    private Long id;

    private String name;

    private String lastName;

    private String phone;
}
