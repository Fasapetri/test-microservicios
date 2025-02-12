package com.example.users.infraestructure.controller;

import com.example.users.infraestructure.dto.UserRequest;
import com.example.users.infraestructure.dto.UserResponse;
import com.example.users.domain.User;
import com.example.users.application.UserService;
import com.example.users.infraestructure.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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

        String role = jwtUtil.extractRole(token.substring(7));

        User user = User.builder()
                .email(userRequest.getEmail())
                .rol(userRequest.getRol())
                .name(userRequest.getName())
                .last_name(userRequest.getLast_name())
                .phone(userRequest.getPhone())
                .password(userRequest.getPassword())
                .document_number(userRequest.getDocument_number())
                .date_birth(userRequest.getDate_birth())
                .build();

        User savedUser = userService.addUser(user, role);

        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .last_name(savedUser.getLast_name())
                .name(savedUser.getName())
                .build();

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable("email") String email){
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token){
        String rol = jwtUtil.extractRole(token.substring(7));
        return userService.findById(id, rol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
