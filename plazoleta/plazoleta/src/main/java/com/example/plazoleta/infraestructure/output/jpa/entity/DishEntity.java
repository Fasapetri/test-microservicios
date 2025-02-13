package com.example.plazoleta.infraestructure.output.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "dish")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @Column(name = "url_image", nullable = false)
    private String url_image;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "id_restaurant", nullable = false)
    @JsonBackReference
    private RestaurantEntity restaurant;
}
