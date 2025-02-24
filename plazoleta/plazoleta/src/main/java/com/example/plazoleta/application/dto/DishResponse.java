package com.example.plazoleta.application.dto;

import com.example.plazoleta.application.constants.DishResponseConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = DishResponseConstants.DESC_DISH_RESPONSE)
public class DishResponse implements Serializable {

    @Schema(description = DishResponseConstants.DESC_ID, example = DishResponseConstants.EXAMPLE_ID)
    private Long id;

    @Schema(description = DishResponseConstants.DESC_NAME, example = DishResponseConstants.EXAMPLE_NAME)
    private String name;

    @Schema(description = DishResponseConstants.DESC_PRICE, example = DishResponseConstants.EXAMPLE_PRICE)
    private Integer price;

    @Schema(description = DishResponseConstants.DESC_DESCRIPTION, example = DishResponseConstants.EXAMPLE_DESCRIPTION)
    private String description;

    @Schema(description = DishResponseConstants.DESC_IMAGE_URL, example = DishResponseConstants.EXAMPLE_IMAGE_URL)
    private String url_image;

    @Schema(description = DishResponseConstants.DESC_CATEGORY, example = DishResponseConstants.EXAMPLE_CATEGORY)
    private String category;

    @Schema(description = DishResponseConstants.DESC_ACTIVE, example = DishResponseConstants.EXAMPLE_ACTIVE)
    private Boolean active;
}