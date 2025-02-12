package com.example.plazoleta.infraestructure.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishRequest {

    @NotBlank(message = "El nombre del plato es obligatorio.")
    private String name;

    @NotNull(message = "El precio del plato es obligatorio.")
    @Min(value = 1, message = "El precio debe ser mayor a 0.")
    private Integer price;

    @NotBlank(message = "La descripción del plato es obligatoria.")
    private String description;

    @NotBlank(message = "La URL de la imagen del plato es obligatoria.")
    private String url_image;

    @NotBlank(message = "La categoría del plato es obligatoria.")
    private String category;

    @NotNull(message = "El ID del restaurante es obligatorio.")
    private Long id_restaurant;

    @NotNull(message = "El ID del propietario es obligatorio.")
    private Long id_proprietary;
}
