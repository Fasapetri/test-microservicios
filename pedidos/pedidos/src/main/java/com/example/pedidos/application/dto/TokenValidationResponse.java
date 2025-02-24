package com.example.pedidos.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenValidationResponse {

    private Long userId;

    private String email;

    private String role;

}