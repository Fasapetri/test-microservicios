package com.example.pedidos.infraestructure.security;

import com.example.pedidos.infraestructure.constants.SecurityContextAdapterConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public Long extractUserId(String token) {
        Object userId = getClaims(token).get(SecurityContextAdapterConstants.USER_ID_CLAIM);
        if(userId instanceof Integer){
            return ((Integer) userId).longValue();
        } else if(userId instanceof Long){
            return (Long) userId;
        } else {
            throw new IllegalArgumentException(SecurityContextAdapterConstants.UNKNOWN_USER_ID_TYPE + userId.getClass().getName());
        }
    }

    public String extractRole(String token) {
        return (String) getClaims(token).get(SecurityContextAdapterConstants.ROLE_CLAIM);
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        String role = claims.get(SecurityContextAdapterConstants.ROLE_CLAIM, String.class);
        Long userId = claims.get(SecurityContextAdapterConstants.USER_ID_CLAIM, Long.class);

        List<SimpleGrantedAuthority> authorities = role != null
                ? Collections.singletonList(new SimpleGrantedAuthority(role))
                : Collections.emptyList();

        CustomUserDetails user = new CustomUserDetails(email, "", authorities, userId);

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }
}
