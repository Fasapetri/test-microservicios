package com.example.plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Dish {

    private Long id;

    private String name;

    private Integer price;

    private String description;

    private String url_image;

    private String category;

    private Boolean active;

    private Restaurant restaurant;
}

