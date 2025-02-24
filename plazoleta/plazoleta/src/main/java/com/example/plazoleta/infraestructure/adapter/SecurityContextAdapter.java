package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.domain.spi.ISecurityContextPort;
import com.example.plazoleta.infraestructure.constants.SecurityContextAdapterConstants;
import com.example.plazoleta.infraestructure.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextAdapter implements ISecurityContextPort {

    private final HttpServletRequest request;

    public SecurityContextAdapter(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getUserAuthenticateRol() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getAuthorities() != null){
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(SecurityContextAdapterConstants.ROLE_ANONYMOUS);
        }
        return SecurityContextAdapterConstants.ROLE_ANONYMOUS;
    }

    @Override
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserId();
        }
        return null;
    }

    @Override
    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    @Override
    public String getToken() {
        String authorizationHeader = request.getHeader(SecurityContextAdapterConstants.AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityContextAdapterConstants.BEARER_PREFIX)) {
            return authorizationHeader.substring(SecurityContextAdapterConstants.BEARER_PREFIX_LENGTH);
        }
        return null;
    }
}