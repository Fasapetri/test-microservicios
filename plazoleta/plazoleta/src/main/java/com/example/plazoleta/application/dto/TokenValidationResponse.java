package com.example.plazoleta.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Respuesta con la información del token validado")
public class TokenValidationResponse {

    @Schema(description = "ID del usuario asociado al token", example = "1")
    private Long userId;

    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
    private String email;

    @Schema(description = "Rol del usuario en la plataforma", example = "ADMIN")
    private String role;
}