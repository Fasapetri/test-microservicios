package com.example.plazoleta.infraestructure.output.jpa.repository;

import com.example.plazoleta.infraestructure.output.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    boolean existsByNit(String nit);

    Page<RestaurantEntity> findAllByOrderByNameAsc(Pageable pageable);

    List<RestaurantEntity> findByProprietaryId(Long proprietaryId);

}
