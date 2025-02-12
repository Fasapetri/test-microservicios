package com.example.plazoleta.infraestructure.repository;

import com.example.plazoleta.domain.Plazoleta;
import com.example.plazoleta.domain.PlazoletaPort;
import com.example.plazoleta.infraestructure.dto.PlazoletaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlazoletaRepositoryAdapter implements PlazoletaPort {

    @Autowired
    private PlazoletaRepository jpaPlazoletaRepository;


    @Override
    public Plazoleta save(Plazoleta plazoleta) {
        return jpaPlazoletaRepository.save(plazoleta);
    }

    @Override
    public boolean existsByNit(String nit) {
        return jpaPlazoletaRepository.existsByNit(nit);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaPlazoletaRepository.existsById(id);
    }

    @Override
    public Optional<Plazoleta> findById(Long id) {
        return jpaPlazoletaRepository.findById(id);
    }

    @Override
    public Page<Plazoleta> findAllByOrderByNameAsc(Pageable pageable) {
        return jpaPlazoletaRepository.findAllByOrderByNameAsc(pageable);
    }
}
