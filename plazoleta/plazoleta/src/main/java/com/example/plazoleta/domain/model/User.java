package com.example.plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Long id;

    private String email;

    private String rol;

    private String name;

    private String lastName;

    private String phone;
}
