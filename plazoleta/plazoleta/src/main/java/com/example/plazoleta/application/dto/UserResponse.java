package com.example.plazoleta.application.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String email;

    private String rol;

    private String name;

    private String last_name;
}
