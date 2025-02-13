package com.example.plazoleta.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos para registrar un nuevo plato")
public class DishRequest {

    @Schema(description = "identificador del plato", example = "1")
    private Long id;

    @Schema(description = "Nombre del plato", example = "Spaghetti Bolognese")
    private String name;

    @Schema(description = "Precio del plato", example = "20000")
    private Integer price;

    @Schema(description = "Descripción del plato", example = "Pasta con salsa boloñesa y queso parmesano")
    private String description;

    @Schema(description = "URL de la imagen del plato", example = "https://example.com/spaghetti.png")
    private String urlImage;

    @Schema(description = "Categoría del plato", example = "Italiana")
    private String category;

    @Schema(description = "ID del restaurante al que pertenece", example = "1")
    private Long restaurantId;
}
