package com.example.plazoleta.infraestructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlazoletaResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String urlLogo;
}
