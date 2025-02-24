package com.example.users.application.dto;

import com.example.users.application.constants.TokenValidationResponseConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = TokenValidationResponseConstants.TOKEN_VALIDATION_RESPONSE_DESCRIPTION)
public class TokenValidationResponse {

    @Schema(description = TokenValidationResponseConstants.USER_ID_DESCRIPTION, example = TokenValidationResponseConstants.USER_ID_EXAMPLE)
    private Long userId;

    @Schema(description = TokenValidationResponseConstants.EMAIL_DESCRIPTION, example = TokenValidationResponseConstants.EMAIL_EXAMPLE)
    private String email;

    @Schema(description = TokenValidationResponseConstants.ROLE_DESCRIPTION, example = TokenValidationResponseConstants.ROLE_EXAMPLE)
    private String role;
}
