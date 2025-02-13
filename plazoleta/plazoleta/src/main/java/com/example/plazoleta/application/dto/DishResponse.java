package com.example.plazoleta.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de respuesta de un plato")
public class DishResponse {

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

    @Schema(description = "Estado del plato (activo o inactivo)", example = "true")
    private Boolean active;
}