package com.example.plazoleta.infraestructure.repository;

import com.example.plazoleta.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByPlazoletaId(Long id);
}
