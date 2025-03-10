package com.example.users.infraestructure.security;

import com.example.users.infraestructure.constants.SecurityContextAdapterConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().
            decode(SecurityContextAdapterConstants.SRC_SECRET_KEY));

    public String generateToken(String email, String role, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim(SecurityContextAdapterConstants.ROLE_CLAIM, role)
                .claim(SecurityContextAdapterConstants.USER_ID_CLAIM, userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +
                        SecurityContextAdapterConstants.EXPIRATION_TOKEN_MS *
                                SecurityContextAdapterConstants.EXPIRATION_TOKEN_SG *
                                SecurityContextAdapterConstants.EXPIRATION_TOKEN_MIN)) // 1 horas
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        try {

            if (token.startsWith(SecurityContextAdapterConstants.BEARER_PREFIX)) {
                token = token.substring(SecurityContextAdapterConstants.BEARER_PREFIX_LENGTH);
            }

            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException(SecurityContextAdapterConstants.INVALID_TOKEN
                    + e.getMessage());
        }
    }

    public String extractEmail(String token) {
        try {
            return extractClaims(token).getSubject();
        } catch (Exception e){
            throw new IllegalArgumentException(SecurityContextAdapterConstants.ERROR_EXTRACT_EMAIL
                    + extractClaims(token).getSubject());
        }
    }

    public String extractRole(String token) {
        return extractClaims(token).get(SecurityContextAdapterConstants.ROLE_CLAIM, String.class);
    }

    public Long extractUserId(String token) {
        try{
            Claims claims = extractClaims(token);
            Object userId = claims.get(SecurityContextAdapterConstants.USER_ID_CLAIM);

            if(userId instanceof Integer intUserId){
                return intUserId.longValue();
            } else if(userId instanceof Long longUserId){
                return longUserId;
            } else {
                throw new IllegalArgumentException(SecurityContextAdapterConstants.UNKNOWN_USER_ID_TYPE
                        + userId.getClass().getName());
            }
        }catch(Exception e){
            throw new IllegalArgumentException(SecurityContextAdapterConstants.ERROR_EXTRACT_ID
                    + e.getMessage());
        }
    }

    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public Date getExpirationDate(String token) {
        return extractClaims(token).getExpiration();
    }

    public long getExpirationTime(String token) {
        Date expirationDate = getExpirationDate(token);
        long currentTime = System.currentTimeMillis();
        return expirationDate.getTime() - currentTime;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractClaims(token);
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
