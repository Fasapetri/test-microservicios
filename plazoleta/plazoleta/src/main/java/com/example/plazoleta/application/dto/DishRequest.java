package com.example.plazoleta.application.dto;

import com.example.plazoleta.application.constants.DishRequestConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = DishRequestConstants.DESC_DISH_REQUEST)
public class DishRequest {

    @Schema(description = DishRequestConstants.DESC_ID, example = DishRequestConstants.EXAMPLE_ID)
    private Long id;

    @Schema(description = DishRequestConstants.DESC_NAME, example = DishRequestConstants.EXAMPLE_NAME)
    private String name;

    @Schema(description = DishRequestConstants.DESC_PRICE, example = DishRequestConstants.EXAMPLE_PRICE)
    private Integer price;

    @Schema(description = DishRequestConstants.DESC_DESCRIPTION, example = DishRequestConstants.EXAMPLE_DESCRIPTION)
    private String description;

    @Schema(description = DishRequestConstants.DESC_IMAGE_URL, example = DishRequestConstants.EXAMPLE_IMAGE_URL)
    private String url_image;

    @Schema(description = DishRequestConstants.DESC_CATEGORY, example = DishRequestConstants.EXAMPLE_CATEGORY)
    private String category;

    @Schema(description = DishRequestConstants.DESC_RESTAURANT_ID, example = DishRequestConstants.EXAMPLE_RESTAURANT_ID)
    private Long id_restaurant;

}