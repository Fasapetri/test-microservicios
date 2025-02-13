package com.example.plazoleta.infraestructure.input.rest;

import com.example.plazoleta.application.dto.RestaurantRequest;
import com.example.plazoleta.application.dto.RestaurantResponse;
import com.example.plazoleta.application.handler.RestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final RestaurantHandler restaurantHandler;

    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantResponse> saveRestaurant(@RequestBody RestaurantRequest restaurantRequest, @RequestHeader("Authorization") String token){
        RestaurantResponse restaurantResponse = restaurantHandler.saveRestaurant(restaurantRequest, token);
        return ResponseEntity.ok(restaurantResponse);
    }

    @GetMapping("/listRestaurant")
    public ResponseEntity<List<RestaurantResponse>> listAllRestaurant(){
        return ResponseEntity.ok(restaurantHandler.getAllrestaurant());
    }

    @GetMapping("/existsRestaurant/{id}")
    public ResponseEntity<Boolean> existsRestaurant(@PathVariable("id") Long idRestaurant, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(restaurantHandler.existsRestaurant(idRestaurant, token));
    }

    @GetMapping("/existsNit/{nit}")
    public ResponseEntity<Boolean> existsRestaurant(@PathVariable("nit") String nit){
        return ResponseEntity.ok(restaurantHandler.existsByNit(nit));
    }

    @GetMapping("/findRestaurant/{id}")
    public ResponseEntity<RestaurantResponse> findByRestaurantId(@PathVariable("id") Long idRestaurant){
        return ResponseEntity.ok(restaurantHandler.findById(idRestaurant));
    }

    @Operation(
            summary = "Obtener lista de restaurantes ordenados por nombre",
            description = "Devuelve una lista paginada de restaurantes ordenados por nombre en orden ascendente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de restaurantes obtenida con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listPageable")
    public ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(
            @Parameter(description = "Parámetros de paginación y ordenación")
            Pageable pageable) {
        return ResponseEntity.ok(restaurantHandler.findAllByOrderByNameAsc(pageable));
    }
}
