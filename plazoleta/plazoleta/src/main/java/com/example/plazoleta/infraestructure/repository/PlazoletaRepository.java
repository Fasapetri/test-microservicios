package com.example.plazoleta.infraestructure.repository;

import com.example.plazoleta.domain.Plazoleta;
import com.example.plazoleta.infraestructure.dto.PlazoletaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlazoletaRepository extends JpaRepository<Plazoleta, Long> {

    boolean existsByNit(String nit);
    Page<Plazoleta> findAllByOrderByNameAsc(Pageable pageable);
}
