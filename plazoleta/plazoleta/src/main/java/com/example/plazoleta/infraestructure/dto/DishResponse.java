package com.example.plazoleta.infraestructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {

    private Long id;
    private String name;
    private Integer price;
    private String category;
    private boolean active;
    private String description;
    private String url_image;
}
