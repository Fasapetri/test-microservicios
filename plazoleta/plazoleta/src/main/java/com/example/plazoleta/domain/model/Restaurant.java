package com.example.plazoleta.domain.model;

import com.example.plazoleta.domain.model.Dish;
import lombok.Data;

import java.util.List;

@Data
public class Restaurant {

    private Long id;

    private String name;

    private String nit;

    private String address;

    private String phone;

    private String url_logo;

    private Long id_proprietary;

    private List<Dish> dishes;
}
