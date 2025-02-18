package com.example.plazoleta.infraestructure.input.rest;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.application.handler.DishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "API para gestionar los platos del restaurante")
public class DishRestController {

    private final DishHandler dishHandler;

    @Operation(
            summary = "Agregar un nuevo plato",
            description = "Este endpoint permite agregar un nuevo plato a un restaurante. Se requiere autenticación.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Plato creado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado. Token no proporcionado o inválido"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping("/add-dish")
    public ResponseEntity<DishResponse> saveDish(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del plato a registrar",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishRequest.class))
            )
            @Valid @RequestBody DishRequest dishRequest,
            @Parameter(description = "Token de autenticación", required = true)
            @RequestHeader("Authorization") String token){
        DishResponse dish = dishHandler.saveDish(dishRequest, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(
            summary = "Actualizar un plato existente",
            description = "Este endpoint permite actualizar la información de un plato específico dentro de un restaurante. Se requiere autenticación.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Plato actualizado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado. Token no proporcionado o inválido"),
                    @ApiResponse(responseCode = "404", description = "Plato no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PutMapping("/{dishId}/update-dish")
    @CacheEvict(value = "dishRestaurantCache", key = "#id_dish")
    public ResponseEntity<DishResponse> updateDish(
            @Parameter(description = "ID del plato a actualizar", required = true)
            @PathVariable("dishId") Long id_dish,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del plato a registrar",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishRequest.class))
            )
            @Valid @RequestBody DishRequest dishUpdateRequest,
            @Parameter(description = "Token de autenticación", required = true)
            @RequestHeader("Authorization") String token){
        DishResponse dish = dishHandler.updateDish(id_dish, dishUpdateRequest, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(
            summary = "Actualizar el estado de un plato",
            description = "Este endpoint permite actualizar el estado de un plato dentro del restaurante. Se requiere autenticación.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Estado del plato actualizado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado. Token no proporcionado o inválido"),
                    @ApiResponse(responseCode = "404", description = "Plato no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PutMapping("/{dishId}/update-dish-status")
    public ResponseEntity<DishResponse> updateStatusDish(
            @Parameter(description = "ID del plato cuyo estado será actualizado", required = true)
            @PathVariable("dishId") Long id_dish,
            @Parameter(description = "Token de autenticación", required = true)
            @RequestHeader("Authorization") String token){
        DishResponse dish = dishHandler.updateDishStatus(id_dish, token);
        return ResponseEntity.ok(dish);
    }

    @Operation(
            summary = "Obtener todos los platos de un restaurante",
            description = "Este endpoint devuelve una lista de platos que pertenecen a un restaurante específico. Se requiere autenticación.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de platos obtenida exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. Parámetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado. Token no proporcionado o inválido"),
                    @ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}/dish")
    @Cacheable(value = "dishRestaurantCache", key = "#id_restaurante")
    public List<DishResponse> getDishRestaurant(
            @Parameter(description = "ID del restaurante cuyos platos se desean obtener", required = true)
            @PathVariable("id") Long id_restaurante,
            @Parameter(description = "Token de autenticación", required = true)
            @RequestHeader("Authorization") String token){
        return dishHandler.getDishRestaurant(id_restaurante, token);
    }

    @Operation(
            summary = "Obtener todos los platos",
            description = "Este endpoint devuelve una lista de todos los platos disponibles en la plataforma. Se requiere autenticación.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de platos obtenida exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. Parámetros inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado. Token no proporcionado o inválido"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/listAllDish")
    public ResponseEntity<List<DishResponse>> getAllDish(
            @Parameter(description = "Token de autenticación", required = true)
            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(dishHandler.getAllDish(token));
    }

    @Operation(
            summary = "Obtener un plato por ID",
            description = "Este endpoint permite obtener los detalles de un plato específico a partir de su ID. Se requiere autenticación.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Plato obtenido exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta. ID inválido"),
                    @ApiResponse(responseCode = "401", description = "No autorizado. Token no proporcionado o inválido"),
                    @ApiResponse(responseCode = "404", description = "Plato no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/{id}/findDish")
    public ResponseEntity<DishResponse> findByDishId(
            @Parameter(description = "ID del plato a buscar", required = true)
            @PathVariable("id") Long idDish,
            @Parameter(description = "Token de autenticación", required = true)
            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(dishHandler.findById(idDish, token));
    }
}
