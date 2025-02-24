package com.example.plazoleta.infraestructure.input.rest;

import com.example.plazoleta.application.dto.DishRequest;
import com.example.plazoleta.application.dto.DishResponse;
import com.example.plazoleta.application.handler.DishHandler;
import com.example.plazoleta.infraestructure.constants.DishRestControllerConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = DishRestControllerConstants.DISH_TAG_NAME, description = DishRestControllerConstants.DISH_TAG_DESCRIPTION)
public class DishRestController {

    private final DishHandler dishHandler;

    @Operation(
            summary = DishRestControllerConstants.ADD_DISH_SUMMARY,
            description = DishRestControllerConstants.ADD_DISH_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = DishRestControllerConstants.RESPONSE_CODE_200,
                            description = DishRestControllerConstants.ADD_DISH_SUCCESS,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_400, description = DishRestControllerConstants.ADD_DISH_BAD_REQUEST),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_401, description = DishRestControllerConstants.ADD_DISH_UNAUTHORIZED),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_500, description = DishRestControllerConstants.ADD_DISH_INTERNAL_SERVER_ERROR)
            }
    )
    @PostMapping("/add-dish")
    public ResponseEntity<DishResponse> saveDish(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = DishRestControllerConstants.DISH_DATA_REGISTRATION_DESCRIPTION,
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishRequest.class))
            )
            @Valid @RequestBody DishRequest dishToCreate){
        DishResponse dish = dishHandler.saveDish(dishToCreate);
        return ResponseEntity.ok(dish);
    }

    @Operation(
            summary = DishRestControllerConstants.UPDATE_DISH_SUMMARY,
            description = DishRestControllerConstants.UPDATE_DISH_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = DishRestControllerConstants.RESPONSE_CODE_200,
                            description = DishRestControllerConstants.UPDATE_DISH_SUCCESS,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_400, description = DishRestControllerConstants.UPDATE_DISH_BAD_REQUEST),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_401, description = DishRestControllerConstants.UPDATE_DISH_UNAUTHORIZED),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_404, description = DishRestControllerConstants.UPDATE_DISH_NOT_FOUND),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_500, description = DishRestControllerConstants.UPDATE_DISH_INTERNAL_SERVER_ERROR)
            }
    )
    @PutMapping("/{dishId}/update-dish")
    @CacheEvict(value = "dishRestaurantCache", key = "#findDishId")
    public ResponseEntity<DishResponse> updateDish(
            @Parameter(description = DishRestControllerConstants.DISH_ID_UPDATE_DESCRIPTION, required = true)
            @PathVariable("dishId") Long findDishId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = DishRestControllerConstants.DISH_REQUEST_BODY_DESCRIPTION,
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishRequest.class))
            )
            @Valid @RequestBody DishRequest dishToUpdate){
        DishResponse dish = dishHandler.updateDish(findDishId, dishToUpdate);
        return ResponseEntity.ok(dish);
    }

    @Operation(
            summary = DishRestControllerConstants.UPDATE_STATUS_DISH_SUMMARY,
            description = DishRestControllerConstants.UPDATE_STATUS_DISH_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = DishRestControllerConstants.RESPONSE_CODE_200,
                            description = DishRestControllerConstants.UPDATE_STATUS_DISH_SUCCESS,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_400, description = DishRestControllerConstants.UPDATE_STATUS_DISH_BAD_REQUEST),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_401, description = DishRestControllerConstants.UPDATE_STATUS_DISH_UNAUTHORIZED),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_404, description = DishRestControllerConstants.UPDATE_STATUS_DISH_NOT_FOUND),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_500, description = DishRestControllerConstants.UPDATE_STATUS_DISH_INTERNAL_SERVER_ERROR)
            }
    )
    @PutMapping("/{dishId}/update-dish-status")
    public ResponseEntity<DishResponse> updateStatusDish(
            @Parameter(description = DishRestControllerConstants.UPDATE_DISH_STATUS_PARAM_DESCRIPTION, required = true)
            @PathVariable("dishId") Long findDishId){
        DishResponse dish = dishHandler.updateDishStatus(findDishId);
        return ResponseEntity.ok(dish);
    }

    @Operation(
            summary = DishRestControllerConstants.GET_DISH_RESTAURANT_SUMMARY,
            description = DishRestControllerConstants.GET_DISH_RESTAURANT_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = DishRestControllerConstants.RESPONSE_CODE_200,
                            description = DishRestControllerConstants.GET_DISH_RESTAURANT_SUCCESS,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_400, description = DishRestControllerConstants.GET_DISH_RESTAURANT_BAD_REQUEST),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_401, description = DishRestControllerConstants.GET_DISH_RESTAURANT_UNAUTHORIZED),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_404, description = DishRestControllerConstants.GET_DISH_RESTAURANT_NOT_FOUND),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_500, description = DishRestControllerConstants.GET_DISH_RESTAURANT_INTERNAL_SERVER_ERROR)
            }
    )
    @GetMapping("/{id}/dishRestaurant")
    @Cacheable(value = "dishRestaurantCache", key = "#findRestaurantId")
    public List<DishResponse> getDishRestaurant(
            @Parameter(description = DishRestControllerConstants.PARAMETER_DESCRIPTION_RESTAURANT_ID, required = true)
            @PathVariable("id") Long findRestaurantId){
        return dishHandler.getDishRestaurant(findRestaurantId);
    }

    @Operation(
            summary = DishRestControllerConstants.GET_ALL_DISH_SUMMARY,
            description = DishRestControllerConstants.GET_ALL_DISH_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = DishRestControllerConstants.RESPONSE_CODE_200,
                            description = DishRestControllerConstants.GET_ALL_DISH_SUCCESS,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_400, description = DishRestControllerConstants.GET_ALL_DISH_BAD_REQUEST),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_401, description = DishRestControllerConstants.GET_ALL_DISH_UNAUTHORIZED),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_500, description = DishRestControllerConstants.GET_ALL_DISH_INTERNAL_SERVER_ERROR)
            }
    )
    @GetMapping("/listAllDish")
    public ResponseEntity<List<DishResponse>> getAllDish(){
        return ResponseEntity.ok(dishHandler.getAllDish());
    }

    @Operation(
            summary = DishRestControllerConstants.GET_DISH_BY_ID_SUMMARY,
            description = DishRestControllerConstants.GET_DISH_BY_ID_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = DishRestControllerConstants.RESPONSE_CODE_200,
                            description = DishRestControllerConstants.GET_DISH_BY_ID_SUCCESS,
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DishResponse.class))
                    ),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_400, description = DishRestControllerConstants.GET_DISH_BY_ID_BAD_REQUEST),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_401, description = DishRestControllerConstants.GET_DISH_BY_ID_UNAUTHORIZED),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_404, description = DishRestControllerConstants.GET_DISH_BY_ID_NOT_FOUND),
                    @ApiResponse(responseCode = DishRestControllerConstants.RESPONSE_CODE_500, description = DishRestControllerConstants.GET_DISH_BY_ID_INTERNAL_SERVER_ERROR)
            }
    )
    @GetMapping("/{id}/findDish")
    public ResponseEntity<DishResponse> findByDishId(
            @Parameter(description = DishRestControllerConstants.GET_DISH_BY_ID_PARAM_DESCRIPTION, required = true)
            @PathVariable("id") Long findDishId){
        return ResponseEntity.ok(dishHandler.findById(findDishId));
    }
}
