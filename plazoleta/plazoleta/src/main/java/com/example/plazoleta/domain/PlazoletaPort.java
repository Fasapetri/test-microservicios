package com.example.plazoleta.domain;


import com.example.plazoleta.infraestructure.dto.PlazoletaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PlazoletaPort {

    Plazoleta save(Plazoleta plazoleta);
    boolean existsByNit(String nit);
    boolean existsById(Long id);
    Optional<Plazoleta> findById(Long id);
    Page<Plazoleta> findAllByOrderByNameAsc(Pageable pageable);
}
