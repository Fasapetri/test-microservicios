package com.example.users.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Objeto que representa los datos para crear o modificar un usuario")
public class UserRequest {

    @Schema(description = "ID del usuario", example = "1", required =  true)
    private Long id;

    @Schema(description = "correo del usuario", example = "test@example.com", required =  true)
    private String email;

    @Schema(description = "ROL que manejará el usuario en la plataforma", examples = {"ADMIN", "PROPIETARIO", "CLIENTE", "EMPLEADO"}, required =  true)
    private String rol;

    @Schema(description = "Contraseña del usuario para su ingreso a la plataforma", example = "xxxxx", required =  true)
    private String password;

    @Schema(description = "nombre del usuario", example = "Juan", required =  true)
    private String name;

    @Schema(description = "apellido del usuario", example = "Perez")
    private String last_name;

    @Schema(description = "documento de identidad del usuario", example = "123456789", required =  true)
    private String document_number;

    @Schema(description = "numero del celular del usuario", example = "+57123456789", required =  true)
    private String phone;

    @Schema(description = "fecha de nacimiento del usuario", example = "1996-05-05")
    private LocalDate date_birth;
}
