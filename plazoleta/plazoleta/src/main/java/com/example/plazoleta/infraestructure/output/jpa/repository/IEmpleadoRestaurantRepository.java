package com.example.plazoleta.infraestructure.output.jpa.repository;

import com.example.plazoleta.infraestructure.output.jpa.entity.EmpleadoRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpleadoRestaurantRepository extends JpaRepository<EmpleadoRestaurantEntity, Long> {

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);

    boolean existsByUserId(Long userId);
}
