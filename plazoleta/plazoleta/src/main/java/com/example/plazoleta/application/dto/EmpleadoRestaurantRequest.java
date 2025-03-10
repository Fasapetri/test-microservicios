package com.example.plazoleta.application.dto;

import lombok.Data;

@Data
public class EmpleadoRestaurantRequest {

    private Long id;

    private Long userId;

    private Long restaurantId;
}
