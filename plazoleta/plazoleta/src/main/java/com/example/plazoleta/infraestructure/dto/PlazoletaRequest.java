package com.example.plazoleta.infraestructure.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlazoletaRequest {

    private String name;
    private String nit;
    private String address;
    private String phone;
    private String urlLogo;
    private Long id_proprietary;
}
