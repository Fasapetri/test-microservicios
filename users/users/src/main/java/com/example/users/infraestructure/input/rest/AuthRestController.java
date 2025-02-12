package com.example.users.infraestructure.input.rest;

import com.example.users.application.dto.AuthRequest;
import com.example.users.application.dto.AuthResponse;
import com.example.users.application.dto.TokenValidationResponse;
import com.example.users.application.handler.AuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthHandler authHandler;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authHandler.login(authRequest));
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authHandler.validateToken(token.replace("Bearer ", "")));
    }
}
