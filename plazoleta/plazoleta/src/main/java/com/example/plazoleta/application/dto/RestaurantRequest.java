package com.example.plazoleta.application.dto;

import com.example.plazoleta.application.constants.RestaurantRequestConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = RestaurantRequestConstants.DESC_RESTAURANT_REQUEST)
public class RestaurantRequest {

    @Schema(description = RestaurantRequestConstants.DESC_ID, example = RestaurantRequestConstants.EXAMPLE_ID)
    private Long id;

    @Schema(description = RestaurantRequestConstants.DESC_NAME, example = RestaurantRequestConstants.EXAMPLE_NAME)
    private String name;

    @Pattern(regexp = RestaurantRequestConstants.REGEX_NIT, message = RestaurantRequestConstants.MSG_NIT_VALIDATION)
    @Schema(description = RestaurantRequestConstants.DESC_NIT, example = RestaurantRequestConstants.EXAMPLE_NIT)
    private String nit;

    @Schema(description = RestaurantRequestConstants.DESC_ADDRESS, example = RestaurantRequestConstants.EXAMPLE_ADDRESS)
    private String address;

    @Pattern(regexp = RestaurantRequestConstants.REGEX_PHONE, message = RestaurantRequestConstants.MSG_PHONE_VALIDATION)
    @Schema(description = RestaurantRequestConstants.DESC_PHONE, example = RestaurantRequestConstants.EXAMPLE_PHONE)
    private String phone;

    @Schema(description = RestaurantRequestConstants.DESC_URL_LOGO, example = RestaurantRequestConstants.EXAMPLE_URL_LOGO)
    private String url_logo;

    @Schema(description = RestaurantRequestConstants.DESC_ID_PROPRIETARY, example = RestaurantRequestConstants.EXAMPLE_ID_PROPRIETARY)
    private Long id_proprietary;

}