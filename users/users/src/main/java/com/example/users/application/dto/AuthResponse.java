package com.example.users.application.dto;

import com.example.users.application.constants.AuthResponseConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = AuthResponseConstants.AUTH_RESPONSE_DESCRIPTION)
public class AuthResponse {

    @Schema(description = AuthResponseConstants.EMAIL_DESCRIPTION, example = AuthResponseConstants.EMAIL_EXAMPLE)
    private String email;

    @Schema(description = AuthResponseConstants.TOKEN_DESCRIPTION, example = AuthResponseConstants.TOKEN_EXAMPLE)
    private String token;
}
