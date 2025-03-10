package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.infraestructure.constants.SecurityContextAdapterConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(SecurityContextAdapterConstants.CONTENT_TYPE_JSON);
        response.getWriter().write(SecurityContextAdapterConstants.FORBIDDEN_OPERATION_MESSAGE);
    }
}


