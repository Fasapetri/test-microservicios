package com.example.users.application.dto;

import com.example.users.application.constants.AuthRequestConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = AuthRequestConstants.AUTH_REQUEST_DESCRIPTION)
public class AuthRequest {

    @Schema(description = AuthRequestConstants.EMAIL_DESCRIPTION, example = AuthRequestConstants.EMAIL_EXAMPLE, required = true)
    private String email;

    @Schema(description = AuthRequestConstants.PASSWORD_DESCRIPTION, example = AuthRequestConstants.PASSWORD_EXAMPLE, required = true)
    private String password;
}