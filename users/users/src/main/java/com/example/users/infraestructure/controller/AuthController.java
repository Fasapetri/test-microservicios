package com.example.users.infraestructure.controller;

import com.example.users.application.AuthService;
import com.example.users.infraestructure.dto.AuthRequest;
import com.example.users.infraestructure.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest){
        String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String token) {
            Long userId = jwtUtil.extractUserId(token.substring(7));
            String email = jwtUtil.extractEmail(token.substring(7));
            String role = jwtUtil.extractRole(token.substring(7));
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("email", email);
            response.put("rol", role);
            return ResponseEntity.ok(response);
    }
}
