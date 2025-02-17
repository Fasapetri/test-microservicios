package com.example.plazoleta.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de respuesta de un restaurante")
public class RestaurantResponse {

    @Schema(description = "Nombre del restaurante", example = "La Casa de la Pasta")
    private String name;

    @Schema(description = "Número de Identificación Tributaria (NIT)", example = "1234567890")
    private String nit;

    @Schema(description = "Dirección del restaurante", example = "Calle 123 #45-67")
    private String address;

    @Schema(description = "Número de teléfono", example = "+573001234567")
    private String phone;

    @Schema(description = "URL del logo del restaurante", example = "https://example.com/logo.png")
    private String url_logo;
}
