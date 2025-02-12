package com.example.plazoleta.infraestructure.controller;

import com.example.plazoleta.application.DishService;
import com.example.plazoleta.domain.Dish;
import com.example.plazoleta.infraestructure.dto.DishRequest;
import com.example.plazoleta.infraestructure.dto.DishUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @Operation(summary = "Crear un plato", description = "Crea un plato asociado a un restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato creado con éxito."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @PostMapping("/add-dish")
    public ResponseEntity<Dish> responseEntity(@Valid @RequestBody DishRequest dishRequest, @RequestHeader("Authorization") String token){
        Dish dish = dishService.addDish(dishRequest, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(summary = "Actualizar un plato", description = "Actualiza un plato ya registrado a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{dishId}/update-dish")
    public ResponseEntity<Dish> updateDish(@PathVariable("dishId") Long id_dish, @Valid @RequestBody DishUpdateRequest dishUpdateRequest, @RequestHeader("Authorization") String token){
        Dish dish = dishService.updateDish(id_dish, dishUpdateRequest, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(summary = "Actualizar estado de un plato", description = "Actualiza el estado un plato (Activar/Desactivar) ya registrado a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del plato actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{dishId}/update-dish-status")
    public ResponseEntity<Dish> updateStatusDish(@PathVariable("dishId") Long id_dish, @RequestHeader("Authorization") String token){
        Dish dish = dishService.updateDishStatus(id_dish, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(summary = "Obtener platos de un restaurante", description = "Obtiene un listado de todos los platos de un restaurante en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de platos obtenidos con exito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/platos")
    public ResponseEntity<Map<String, Object>> obtenerPlatosRestaurante(@PathVariable("id") Long id_restaurante, @RequestHeader("Authorization") String token){
        Map<String, Object> listaPlatos = dishService.obtenerPlatos(id_restaurante, token);
        return ResponseEntity.ok(listaPlatos);
    }
}
