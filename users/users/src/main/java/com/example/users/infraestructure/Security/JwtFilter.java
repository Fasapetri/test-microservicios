package com.example.users.infraestructure.Security;

import com.example.users.infraestructure.adapter.TokenBlackListAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlackListAdapter tokenBlackListAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        try{
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7).trim();
            email = jwtUtil.extractEmail(token);

            logger.info("Token procesado correctamente. Email extraído: {}" + email);
        }

        if (tokenBlackListAdapter.isTokenInvalidated(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = new User(email, "", authorities);

            if (jwtUtil.isTokenValid(token, email)) {

                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

 //               UsernamePasswordAuthenticationToken authenticationToken =
   //                     new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
     //           SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    } catch (Exception e) {
        logger.error("Failed to extract email from token: {}" + e.getMessage());
    }
        filterChain.doFilter(request, response);
    }

}
