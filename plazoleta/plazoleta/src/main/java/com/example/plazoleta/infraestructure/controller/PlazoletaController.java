package com.example.plazoleta.infraestructure.controller;

import com.example.plazoleta.application.AuthServiceUser;
import com.example.plazoleta.application.PlazoletaService;
import com.example.plazoleta.domain.Plazoleta;
import com.example.plazoleta.infraestructure.dto.PlazoletaRequest;
import com.example.plazoleta.infraestructure.dto.PlazoletaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plazoletas")
public class PlazoletaController {

    private final PlazoletaService plazoletaService;

    public PlazoletaController(PlazoletaService plazoletaService){
        this.plazoletaService = plazoletaService;
    }

    @PostMapping("/create-plazoleta")
    public ResponseEntity<PlazoletaResponse> addPlazoleta(@RequestBody PlazoletaRequest plazoletaRequest, @RequestHeader("Authorization") String token){

        Plazoleta plazoleta = Plazoleta.builder()
                .phone(plazoletaRequest.getPhone())
                .nit(plazoletaRequest.getNit())
                .name(plazoletaRequest.getName())
                .address(plazoletaRequest.getAddress())
                .url_Logo(plazoletaRequest.getUrlLogo())
                .id_proprietary(plazoletaRequest.getId_proprietary())
                .build();

        Plazoleta addPlazoleta = plazoletaService.addPlazoleta(plazoleta, token);

        PlazoletaResponse plazoletaResponse = new PlazoletaResponse();

        plazoletaResponse.setAddress(addPlazoleta.getAddress());
        plazoletaResponse.setName(addPlazoleta.getName());
        plazoletaResponse.setPhone(addPlazoleta.getPhone());
        plazoletaResponse.setUrlLogo(addPlazoleta.getUrl_Logo());
        plazoletaResponse.setId(addPlazoleta.getId());

        return ResponseEntity.ok(plazoletaResponse);
    }

    @GetMapping
    public Page<PlazoletaResponse> listPlazoleta(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestHeader("Authorization") String token){
        Pageable pageable = PageRequest.of(page, size);
        return plazoletaService.getPlazoletas(pageable, token);
    }

    @GetMapping("/{id}/existe")
    public boolean existeRestaurante(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        return plazoletaService.existeRestaurante(id, token);
    }
}
