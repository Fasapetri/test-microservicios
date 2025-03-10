package com.example.plazoleta.infraestructure.input.rest;

import com.example.plazoleta.application.dto.EmpleadoRestaurantRequest;
import com.example.plazoleta.application.dto.RestaurantRequest;
import com.example.plazoleta.application.dto.RestaurantResponse;
import com.example.plazoleta.application.handler.RestaurantHandler;
import com.example.plazoleta.infraestructure.constants.RestaurantRestControllerConstants;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestaurantRestControllerConstants.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = RestaurantRestControllerConstants.RESTAURANT_API_TAG_NAME, description = RestaurantRestControllerConstants.RESTAURANT_API_DESCRIPTION)
public class RestaurantRestController {

    private final RestaurantHandler restaurantHandler;

    @Operation(
            summary = RestaurantRestControllerConstants.REGISTER_RESTAURANT_SUMMARY,
            description = RestaurantRestControllerConstants.REGISTER_RESTAURANT_DESCRIPTION
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_200, description = RestaurantRestControllerConstants.RESPONSE_CODE_200_DESCRIPTION,
                    content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON, schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_400, description = RestaurantRestControllerConstants.RESPONSE_CODE_400_DESCRIPTION,
                    content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON)),
            @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_401, description = RestaurantRestControllerConstants.RESPONSE_CODE_401_DESCRIPTION,
                    content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON)),
            @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_500, description = RestaurantRestControllerConstants.RESPONSE_CODE_500_DESCRIPTION,
                    content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON))
    })
    @PostMapping(RestaurantRestControllerConstants.ADD_RESTAURANT)
    @PreAuthorize(RestaurantRestControllerConstants.HAS_AUTHORITY_ADMIN)
    public ResponseEntity<RestaurantResponse> saveRestaurant(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = RestaurantRestControllerConstants.REQUEST_BODY_DESCRIPTION,
                    required = RestaurantRestControllerConstants.REQUEST_BODY_REQUIRED,
                    content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON, schema = @Schema(implementation = RestaurantRequest.class))
            )
            @RequestBody RestaurantRequest restaurantToCreate){
        RestaurantResponse restaurantResponse = restaurantHandler.saveRestaurant(restaurantToCreate);
        return ResponseEntity.ok(restaurantResponse);
    }

    @Operation(
            summary = RestaurantRestControllerConstants.OPERATION_LIST_RESTAURANTS_SUMMARY,
            description = RestaurantRestControllerConstants.OPERATION_LIST_RESTAURANTS_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_200,
                            description = RestaurantRestControllerConstants.RESPONSE_DESCRIPTION_200,
                            content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON, schema = @Schema(implementation = RestaurantResponse.class))
                    ),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_401, description = RestaurantRestControllerConstants.RESPONSE_CODE_401_DESCRIPTION),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_500, description = RestaurantRestControllerConstants.RESPONSE_CODE_500_DESCRIPTION)
            }
    )
    @GetMapping(RestaurantRestControllerConstants.LIST_RESTAURANTS)
    public ResponseEntity<List<RestaurantResponse>> listAllRestaurant(){
        return ResponseEntity.ok(restaurantHandler.getAllrestaurant());
    }

    @Operation(
            summary = RestaurantRestControllerConstants.VERIFY_RESTAURANT_EXISTENCE_SUMMARY,
            description = RestaurantRestControllerConstants.VERIFY_RESTAURANT_EXISTENCE_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_200,
                            description = RestaurantRestControllerConstants.CHECK_RESTAURANT_EXISTS_DESCRIPTION,
                            content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON, schema = @Schema(example = "true"))
                    ),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_401, description = RestaurantRestControllerConstants.RESPONSE_CODE_401_DESCRIPTION),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_404,  description = RestaurantRestControllerConstants.RESTAURANT_NOT_FOUND_DESCRIPTION),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_500, description = RestaurantRestControllerConstants.RESPONSE_CODE_500_DESCRIPTION)
            }
    )
    @GetMapping(RestaurantRestControllerConstants.EXISTS_RESTAURANT_BY_ID)
    public ResponseEntity<Boolean> existsRestaurantById(
            @Parameter(description = RestaurantRestControllerConstants.PARAMETER_RESTAURANT_ID_DESCRIPTION, required = true)
            @PathVariable(RestaurantRestControllerConstants.PATH_VARIABLE_ID) Long findRestaurantId){
        return ResponseEntity.ok(restaurantHandler.existsRestaurant(findRestaurantId));
    }

    @Operation(
            summary = RestaurantRestControllerConstants.VERIFY_RESTAURANT_EXISTENCE_SUMMARY,
            description = RestaurantRestControllerConstants.VERIFY_RESTAURANT_BY_NIT_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_200,
                            description = RestaurantRestControllerConstants.RESTAURANT_FOUND_DESCRIPTION,
                            content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON, schema = @Schema(implementation = Boolean.class))
                    ),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_400, description = RestaurantRestControllerConstants.RESPONSE_CODE_400_DESCRIPTION),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_404,  description = RestaurantRestControllerConstants.RESTAURANT_NOT_FOUND_DESCRIPTION),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_500, description = RestaurantRestControllerConstants.RESPONSE_CODE_500_DESCRIPTION)
            }
    )
    @GetMapping(RestaurantRestControllerConstants.EXISTS_RESTAURANT_BY_NIT)
    public ResponseEntity<Boolean> existsRestaurantByNit(@PathVariable(RestaurantRestControllerConstants.PATH_VARIABLE_NIT) String findRestaurantNit){
        return ResponseEntity.ok(restaurantHandler.existsByNit(findRestaurantNit));
    }

    @Operation(
            summary = RestaurantRestControllerConstants.SEARCH_RESTAURANT_BY_ID_SUMMARY,
            description = RestaurantRestControllerConstants.SEARCH_RESTAURANT_BY_ID_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_200,
                            description = RestaurantRestControllerConstants.RESTAURANT_FOUND_DESCRIPTION,
                            content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON, schema = @Schema(implementation = RestaurantResponse.class))
                    ),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_404,  description = RestaurantRestControllerConstants.RESTAURANT_NOT_FOUND_DESCRIPTION),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_500, description = RestaurantRestControllerConstants.RESPONSE_CODE_500_DESCRIPTION)
            }
    )
    @GetMapping(RestaurantRestControllerConstants.FIND_RESTAURANT_BY_ID)
    @PreAuthorize(RestaurantRestControllerConstants.HAS_AUTHORITY_ADMIN)
    public ResponseEntity<RestaurantResponse> findByRestaurantId(
            @Parameter(description = RestaurantRestControllerConstants.PARAMETER_RESTAURANT_ID_DESCRIPTION, required = true)
            @PathVariable(RestaurantRestControllerConstants.PATH_VARIABLE_ID) Long findRestaurantId){
        return ResponseEntity.ok(restaurantHandler.findById(findRestaurantId));
    }

    @Operation(
            summary = RestaurantRestControllerConstants.GET_ALL_RESTAURANTS_PAGINATED_SUMMARY,
            description = RestaurantRestControllerConstants.GET_ALL_RESTAURANTS_PAGINATED_DESCRIPTION,
            parameters = {
                    @Parameter(name = RestaurantRestControllerConstants.PAGE_NAME, description = RestaurantRestControllerConstants.PAGE_DESCRIPTION, example = RestaurantRestControllerConstants.PAGE_EXAMPLE),
                    @Parameter(name = RestaurantRestControllerConstants.SIZE_NAME, description = RestaurantRestControllerConstants.SIZE_DESCRIPTION, example = RestaurantRestControllerConstants.SIZE_EXAMPLE),
                    @Parameter(name = RestaurantRestControllerConstants.SORT_NAME, description = RestaurantRestControllerConstants.SORT_DESCRIPTION, example = RestaurantRestControllerConstants.SORT_EXAMPLE),
            },
            responses = {
                    @ApiResponse(
                            responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_200,
                            description = RestaurantRestControllerConstants.GET_RESTAURANTS_SUCCESS_DESCRIPTION,
                            content = @Content(mediaType = RestaurantRestControllerConstants.MEDIA_TYPE_JSON, schema = @Schema(implementation = Page.class))
                    ),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_400, description = RestaurantRestControllerConstants.RESPONSE_CODE_400_DESCRIPTION),
                    @ApiResponse(responseCode = RestaurantRestControllerConstants.RESPONSE_CODE_500, description = RestaurantRestControllerConstants.RESPONSE_CODE_500_DESCRIPTION)
            }
    )
    @GetMapping(RestaurantRestControllerConstants.LIST_RESTAURANTS_PAGEABLE)
    public ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(
            @Parameter(description = RestaurantRestControllerConstants.PAGINATION_AND_SORTING_DESCRIPTION)
            Pageable pageable) {
        return ResponseEntity.ok(restaurantHandler.findAllByOrderByNameAsc(pageable));
    }

    @PostMapping(RestaurantRestControllerConstants.ASIGNED_EMPLEADO_RESTAURANT)
    @PreAuthorize(RestaurantRestControllerConstants.HAS_AUTHORITY_PROPIETARIO)
    public ResponseEntity<String> asignarEmpleadoRestaurant(@RequestBody EmpleadoRestaurantRequest empleadoRestaurantRequest){
        return ResponseEntity.ok(restaurantHandler.saveEmpleadoRestaurant(empleadoRestaurantRequest));
    }
}
