package com.example.plazoleta.application.dto;

import com.example.plazoleta.application.constants.RestaurantResponseConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = RestaurantResponseConstants.DESC_RESPONSE)
public class RestaurantResponse implements Serializable {

    @Schema(description = RestaurantResponseConstants.DESC_NAME, example = RestaurantResponseConstants.EXAMPLE_NAME)
    private String name;

    @Schema(description = RestaurantResponseConstants.DESC_NIT, example = RestaurantResponseConstants.EXAMPLE_NIT)
    private String nit;

    @Schema(description = RestaurantResponseConstants.DESC_ADDRESS, example = RestaurantResponseConstants.EXAMPLE_ADDRESS)
    private String address;

    @Schema(description = RestaurantResponseConstants.DESC_PHONE, example = RestaurantResponseConstants.EXAMPLE_PHONE)
    private String phone;

    @Schema(description = RestaurantResponseConstants.DESC_URL_LOGO, example = RestaurantResponseConstants.EXAMPLE_URL_LOGO)
    private String url_logo;

}
