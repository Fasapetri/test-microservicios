package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.AuthRequest;
import com.example.users.application.dto.AuthResponse;
import com.example.users.application.dto.TokenValidationResponse;
import com.example.users.application.handler.AuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints relacionados con la autenticacion del usuario")
public class AuthRestController {

    private final AuthHandler authHandler;

    @Operation(
            summary = "Iniciar Sesión",
            description = "Permite a los usuarios autenticarse en la plataforma proporcionando su correo electrónico y contraseña."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, se devuelve el token de acceso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos incorrectos",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado, credenciales incorrectas",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de acceso para autenticación",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthRequest.class))
    ) @RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authHandler.login(authRequest));
    }

    @Operation(
            summary = "Validar Token",
            description = "Permite validar un token de autenticación JWT y extraer información del usuario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token válido, se retorna la información del usuario",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenValidationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, token ausente o malformado",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authHandler.validateToken(token.replace("Bearer ", "")));
    }
}
