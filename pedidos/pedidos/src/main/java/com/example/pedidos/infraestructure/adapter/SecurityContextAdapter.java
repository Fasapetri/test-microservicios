package com.example.pedidos.infraestructure.adapter;

import com.example.pedidos.domain.spi.ISecurityContextPort;
import com.example.pedidos.infraestructure.constants.SecurityContextAdapterConstants;
import com.example.pedidos.infraestructure.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextAdapter implements ISecurityContextPort {

    @Override
    public Mono<String> getUserAuthenticateRol() {
        return ReactiveSecurityContextHolder.getContext()
                .map(authentication -> {
                    if (authentication != null && authentication.getAuthentication().getAuthorities() != null) {
                        return authentication.getAuthentication().getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .findFirst()
                                .orElse(SecurityContextAdapterConstants.ROLE_ANONYMOUS);
                    }
                    return SecurityContextAdapterConstants.ROLE_ANONYMOUS;
                });
    }

    @Override
    public Mono<Long> getAuthenticatedUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(authentication -> {
                    if (authentication != null && authentication.getAuthentication().getPrincipal() instanceof CustomUserDetails) {
                        CustomUserDetails userDetails = (CustomUserDetails) authentication.getAuthentication().getPrincipal();
                        return userDetails.getUserId();
                    }
                    return null;
                });
    }

    @Override
    public Mono<String> getAuthenticatedUserEmail() {
        return ReactiveSecurityContextHolder.getContext()
                .map(authentication -> {
                    if (authentication != null && authentication.getAuthentication().getPrincipal() instanceof CustomUserDetails) {
                        CustomUserDetails userDetails = (CustomUserDetails) authentication.getAuthentication().getPrincipal();
                        return userDetails.getUsername();
                    }
                    return null;
                });
    }

    @Override
    public Mono<String> getToken() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> Mono.justOrEmpty(
                        context.getAuthentication().getCredentials().toString()
                ));
    }
}
