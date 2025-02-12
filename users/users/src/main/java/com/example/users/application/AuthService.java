package com.example.users.application;

import com.example.users.domain.User;
import com.example.users.domain.UserPort;
import com.example.users.infraestructure.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserPort userPort;

    public String login(String email, String password){

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(email, password));

        User user = userPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        if (user.getRol() == null || user.getId() == null) {
            throw new IllegalStateException("El usuario no tiene rol o ID definido");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRol(), user.getId());
    }
}
