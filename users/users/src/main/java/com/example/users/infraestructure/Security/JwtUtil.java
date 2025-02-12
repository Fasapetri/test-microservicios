package com.example.users.infraestructure.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode("N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9"));

    public String generateToken(String email, String role, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        try {

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido o mal formado" + e.getMessage());
        }
    }

    public String extractEmail(String token) {
        try {
            return extractClaims(token).getSubject();
        } catch (Exception e){
            throw new IllegalArgumentException("Mess " + extractClaims(token).getSubject());
        }
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public Long extractUserId(String token) {
        try{
            Claims claims = extractClaims(token);
            Object userId = claims.get("userId");

            if(userId instanceof Integer){
                return ((Integer) userId).longValue();
            } else if(userId instanceof Long){
                return (Long) userId;
            } else {
                throw new IllegalArgumentException("Tipo de userId desconocido: " + userId.getClass().getName());
            }
        }catch(Exception e){
            throw new IllegalArgumentException("Error al extraer el userid del token: " + e.getMessage());
        }
    }

    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
