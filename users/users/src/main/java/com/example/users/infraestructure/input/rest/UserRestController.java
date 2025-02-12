package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
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

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable("email") String email, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userHandler.findByEmailUser(email, token));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userHandler.findByIdUser(id, token));
    }

    @PutMapping("/editUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(userHandler.updateUser(userRequest, token));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        userHandler.deleteUser(id, token);
        return ResponseEntity.noContent().build();
    }
}
