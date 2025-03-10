package com.example.plazoleta.infraestructure.output.jpa.repository;

import com.example.plazoleta.infraestructure.output.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    List<DishEntity> findAllByRestaurantIdAndCategory(Long findRestaurantId, String dishCategory);

    List<DishEntity> findAllByRestaurantId(Long findRestaurantId);

}
