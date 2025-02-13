package com.example.plazoleta.infraestructure.input.rest;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.application.handler.DishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishRestController {

    private final DishHandler dishHandler;

    @Operation(summary = "Crear un plato", description = "Crea un plato asociado a un restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato creado con éxito."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @PostMapping("/add-dish")
    public ResponseEntity<DishResponse> saveDish(@Valid @RequestBody DishRequest dishRequest, @RequestHeader("Authorization") String token){
        DishResponse dish = dishHandler.saveDish(dishRequest, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(summary = "Actualizar un plato", description = "Actualiza un plato ya registrado a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/update-dish")
    public ResponseEntity<DishResponse> updateDish(@Valid @RequestBody DishRequest dishUpdateRequest, @RequestHeader("Authorization") String token){
        DishResponse dish = dishHandler.updateDish(dishUpdateRequest, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(summary = "Actualizar estado de un plato", description = "Actualiza el estado un plato (Activar/Desactivar) ya registrado a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del plato actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{dishId}/update-dish-status")
    public ResponseEntity<DishResponse> updateStatusDish(@PathVariable("dishId") Long id_dish, @RequestHeader("Authorization") String token){
        DishResponse dish = dishHandler.updateDishStatus(id_dish, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(summary = "Obtener platos de un restaurante", description = "Obtiene un listado de todos los platos de un restaurante en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de platos obtenidos con exito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/dish")
    public ResponseEntity<List<DishResponse>> getDishRestaurant(@PathVariable("id") Long id_restaurante, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(dishHandler.getDishRestaurant(id_restaurante, token));
    }

    @GetMapping("/listAllDish")
    public ResponseEntity<List<DishResponse>> getAllDish(){
        return ResponseEntity.ok(dishHandler.getAllDish());
    }

    @GetMapping("/{id}/findDish")
    public ResponseEntity<DishResponse> findByDishId(@PathVariable("id") Long idDish){
        return ResponseEntity.ok(dishHandler.findById(idDish));
    }
}
