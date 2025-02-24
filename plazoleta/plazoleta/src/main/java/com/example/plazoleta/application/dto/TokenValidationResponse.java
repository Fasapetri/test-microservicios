package com.example.plazoleta.application.dto;

import com.example.plazoleta.application.constants.TokenValidationResponseConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = TokenValidationResponseConstants.DESC_RESPONSE)
public class TokenValidationResponse implements Serializable {

    @Schema(description = TokenValidationResponseConstants.DESC_USER_ID, example = TokenValidationResponseConstants.EXAMPLE_USER_ID)
    private Long userId;

    @Schema(description = TokenValidationResponseConstants.DESC_EMAIL, example = TokenValidationResponseConstants.EXAMPLE_EMAIL)
    private String email;

    @Schema(description = TokenValidationResponseConstants.DESC_ROLE, example = TokenValidationResponseConstants.EXAMPLE_ROLE)
    private String role;

}