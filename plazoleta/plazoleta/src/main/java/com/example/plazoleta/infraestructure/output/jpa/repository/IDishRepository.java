package com.example.plazoleta.infraestructure.output.jpa.repository;

import com.example.plazoleta.infraestructure.output.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    List<DishEntity> findAllByRestaurantId(Long id);

    DishEntity save(Long id);
}
