package com.example.users.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos requeridos para iniciar sesión")
public class AuthRequest {

    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com", required = true)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123", required = true)
    private String password;
}
