package com.example.plazoleta.infraestructure.input.rest;

import com.example.plazoleta.application.dto.RestaurantRequest;
import com.example.plazoleta.application.dto.RestaurantResponse;
import com.example.plazoleta.application.handler.RestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "API para la gestión de restaurantes")
public class RestaurantRestController {

    private final RestaurantHandler restaurantHandler;

    @Operation(
            summary = "Registrar un nuevo restaurante",
            description = "Este endpoint permite registrar un nuevo restaurante en el sistema. Se requiere autenticación mediante un token de autorización."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos faltantes o incorrectos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "No autorizado, el token de autenticación es inválido o ha expirado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantResponse> saveRestaurant(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del restaurante a registrar",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantRequest.class))
            )
            @RequestBody RestaurantRequest restaurantRequest,
            @Parameter(description = "Token de autenticación del usuario", required = true)
            @RequestHeader("Authorization") String token){
        RestaurantResponse restaurantResponse = restaurantHandler.saveRestaurant(restaurantRequest, token);
        return ResponseEntity.ok(restaurantResponse);
    }

    @Operation(
            summary = "Listar todos los restaurantes",
            description = "Este endpoint permite obtener una lista de todos los restaurantes registrados en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de restaurantes obtenida exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/listRestaurant")
    public ResponseEntity<List<RestaurantResponse>> listAllRestaurant(
            @Parameter(description = "Token de autenticación en formato Bearer", required = true)
            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(restaurantHandler.getAllrestaurant(token));
    }

    @Operation(
            summary = "Verificar existencia de un restaurante",
            description = "Este endpoint permite verificar si un restaurante existe en el sistema mediante su ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Devuelve `true` si el restaurante existe, `false` en caso contrario",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = "true"))
                    ),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/existsRestaurant/{id}")
    public ResponseEntity<Boolean> existsRestaurant(
            @Parameter(description = "ID del restaurante a verificar", required = true)
            @PathVariable("id") Long idRestaurant,
            @Parameter(description = "Token de autenticación en formato Bearer", required = true)
            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(restaurantHandler.existsRestaurant(idRestaurant, token));
    }

    @GetMapping("/existsNit/{nit}")
    public ResponseEntity<Boolean> existsRestaurant(@PathVariable("nit") String nit){
        return ResponseEntity.ok(restaurantHandler.existsByNit(nit));
    }

    @Operation(
            summary = "Buscar un restaurante por ID",
            description = "Este endpoint permite obtener la información de un restaurante en base a su identificador único.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Restaurante encontrado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Restaurante no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/findRestaurant/{id}")
    public ResponseEntity<RestaurantResponse> findByRestaurantId(
            @Parameter(description = "ID del restaurante a buscar", required = true)
            @PathVariable("id") Long idRestaurant){
        return ResponseEntity.ok(restaurantHandler.findById(idRestaurant));
    }

    @Operation(
            summary = "Obtener todos los restaurantes paginados y ordenados",
            description = "Este endpoint permite obtener una lista de restaurantes con paginación y ordenación ascendente por nombre.",
            parameters = {
                    @Parameter(name = "page", description = "Número de página (empezando desde 0)", example = "0"),
                    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
                    @Parameter(name = "sort", description = "Criterio de ordenación en formato `campo,asc|desc`", example = "name,asc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de restaurantes obtenida exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/listPageable")
    public ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(
            @Parameter(description = "Parámetros de paginación y ordenación")
            Pageable pageable) {
        return ResponseEntity.ok(restaurantHandler.findAllByOrderByNameAsc(pageable));
    }
}
