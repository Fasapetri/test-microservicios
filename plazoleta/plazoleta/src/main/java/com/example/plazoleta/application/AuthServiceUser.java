package com.example.plazoleta.application;

import com.example.plazoleta.infraestructure.user.AuthClient;
import com.example.plazoleta.infraestructure.user.UserClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceUser {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceUser.class);

    @Autowired
    private UserClient userClient;

    @Autowired
    private AuthClient authClient;


    public AuthClient.AuthenticateUser getUserRoleFromToken(String token) {
        try {
            Map<String, Object> response = authClient.validateToken("Bearer " + token.trim());

            Object userIdObject = response.get("userId");
            Long userId = null;
            if (userIdObject instanceof Integer) {
                userId = ((Integer) userIdObject).longValue();
            } else if (userIdObject instanceof Long) {
                userId = (Long) userIdObject;
            } else {
                throw new IllegalArgumentException("Tipo de userId desconocido: " + userIdObject.getClass().getName());
            }
            return new AuthClient.AuthenticateUser(
                     userId,
                    (String) response.get("email"),
                    (String) response.get("rol")
            );
        } catch (Exception e) {
            log.error("Error al validar token: " + e.getMessage());
            throw new IllegalArgumentException("Token inválido: " + e.getMessage());
        }
    }


    public UserClient.UserResponse userByIdProprietary(Long id, String token){
        try {
            return userClient.getUserByIdProprietary(id, "Bearer " + token.trim());
        } catch (Exception e) {
            log.error("Error al obtener usuario por ID: {}", e.getMessage());
            throw new IllegalArgumentException("Usuario no encontrado o token inválido");
        }
    }
}
