package com.example.users.infraestructure.security;

import com.example.users.infraestructure.adapter.TokenBlackListAdapter;
import com.example.users.infraestructure.constants.SecurityContextAdapterConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlackListAdapter tokenBlackListAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(
                SecurityContextAdapterConstants.AUTHORIZATION_HEADER);
        String token = null;
        String email = null;

        try{
        if (authorizationHeader != null && authorizationHeader.startsWith
                (SecurityContextAdapterConstants.BEARER_PREFIX)) {
            token = authorizationHeader.substring(7).trim();
            email = jwtUtil.extractEmail(token);

            logger.info(SecurityContextAdapterConstants.PROCESSING_TOKEN_SUCCESS + email);
        }

        if (tokenBlackListAdapter.isTokenInvalidated(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    SecurityContextAdapterConstants.INVALID_TOKEN);
            return;
        }

        if (email != null
                && SecurityContextHolder.getContext().getAuthentication() == null
        && jwtUtil.isTokenValid(token, email)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    } catch (Exception e) {
        logger.error(SecurityContextAdapterConstants.ERROR_EXTRACT_EMAIL + e.getMessage());
    }
        filterChain.doFilter(request, response);
    }

}
