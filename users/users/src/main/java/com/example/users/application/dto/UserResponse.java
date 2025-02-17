package com.example.users.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@Schema(description = "Respuesta con los datos del usuario")
public class UserResponse {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Correo del usuario", example = "test@example.com")
    private String email;

    @Schema(description = "Rol que tendra el usuario en la plataforma", examples = {"ADMIN", "PROPIETARIO", "EMPLEADO", "CLIENTE"})
    private String rol;

    @Schema(description = "nombre del usuario", example = "Juan")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Perez")
    private String last_name;
}
