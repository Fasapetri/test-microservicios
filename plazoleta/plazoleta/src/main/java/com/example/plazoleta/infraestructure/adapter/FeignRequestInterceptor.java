package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.infraestructure.constants.SecurityContextAdapterConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();
            template.header(SecurityContextAdapterConstants.AUTHORIZATION_HEADER, SecurityContextAdapterConstants.BEARER_PREFIX + token);
            System.out.println("Token agregado: " + token);

        } else {
            System.out.println("No se encontró el token en el contexto de seguridad.");
        }
    }
}
