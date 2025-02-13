package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints relacionados con la gestión de usuarios")
public class UserRestController {

    private final UserHandler userHandler;

    @Operation(summary = "Crear un usuario", description = "Crea un nuevo usuario con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String token){
        UserResponse user = userHandler.saveUser(userRequest, token);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Consultar Usuario por email", description = "Permite consultar un usuario ingresando su correo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Formato de email invalido",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado, token inválido o ausente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail( @Parameter(
            name = "email",
            description = "Correo electrónico del usuario a buscar. Debe tener un formato válido.",
            example = "usuario@example.com",
            required = true
    ) @PathVariable("email") String email, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userHandler.findByEmailUser(email, token));
    }

    @Operation(summary = "Consultar Usuario por id", description = "Permite consultar un usuario ingresando su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "ID invalido",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado, token inválido o ausente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> findById(@Parameter(
            name = "id",
            description = "Identificador único del usuario a buscar.",
            example = "1",
            required = true
    ) @PathVariable("id") Long id, @Parameter(
            name = "Authorization",
            description = "Token de autenticación en formato Bearer.",
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            required = true
    ) @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userHandler.findByIdUser(id, token));
    }

    @Operation(
            summary = "Actualizar Usuario",
            description = "Permite modificar la información de un usuario existente. Se requiere autenticación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado, token inválido o ausente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PutMapping("/editUser")
    public ResponseEntity<UserResponse> updateUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del usuario a actualizar",
            required = true,
            content = @Content(schema = @Schema(implementation = UserRequest.class))
    ) @RequestBody UserRequest userRequest, @Parameter(
            name = "Authorization",
            description = "Token de autenticación en formato Bearer.",
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            required = true
    ) @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userHandler.updateUser(userRequest, token));
    }

    @Operation(
            summary = "Eliminar Usuario",
            description = "Permite eliminar un usuario existente por su ID. Se requiere autenticación con un token válido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado, token inválido o ausente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(
            name = "id",
            description = "Identificador único del usuario a eliminar.",
            example = "1",
            required = true
    ) @PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        userHandler.deleteUser(id, token);
        return ResponseEntity.noContent().build();
    }
}
