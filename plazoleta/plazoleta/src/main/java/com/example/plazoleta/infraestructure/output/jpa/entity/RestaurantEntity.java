package com.example.plazoleta.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "plazoleta")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nit", unique = true, nullable = false)
    private String nit;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "url_logo", nullable = false)
    private String url_logo;

    @Column(name = "id_proprietary", nullable = false)
    private Long proprietaryId;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DishEntity> dishes;
}
