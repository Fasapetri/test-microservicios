package com.example.plazoleta.infraestructure.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishUpdateRequest {

    @Min(value = 1, message = "El precio debe ser un numero entero mayor a 0")
    private Integer price;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String description;
}
