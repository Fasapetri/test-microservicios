package com.example.plazoleta.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    // Cargar la clave secreta desde application.properties y decodificarla
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    // Extraer el usuario (ID) desde el token
    public Long extractUserId(String token) {
        Object userId = getClaims(token).get("userId");
        if(userId instanceof Integer){
            return ((Integer) userId).longValue();
        } else if(userId instanceof Long){
            return (Long) userId;
        } else {
            throw new IllegalArgumentException("Tipo de userId desconocido: " + userId.getClass().getName());
        }
    }

    // Extraer los roles del usuario
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Validar si el token es válido
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Decodificar el token y obtener los Claims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
