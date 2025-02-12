package com.example.users.application;

import com.example.users.domain.User;
import com.example.users.domain.UserPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserPort userPort, PasswordEncoder passwordEncoder) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User user, String authenticatedUserRole) {

        if ("ADMIN".equalsIgnoreCase(authenticatedUserRole)) {
            if (!"PROPIETARIO".equalsIgnoreCase(user.getRol())) {
                if (!"CLIENTE".equalsIgnoreCase(user.getRol())) {
                    throw new IllegalArgumentException("Solo los usuarios con rol PROPIETARIO pueden crear usuarios EMPLEADOS.");
                }
            }
            validateUser(user);
        } else if ("PROPIETARIO".equalsIgnoreCase(authenticatedUserRole)) {
            if (!"EMPLEADO".equalsIgnoreCase(user.getRol())) {
                if (!"CLIENTE".equalsIgnoreCase(user.getRol())) {
                    throw new IllegalArgumentException("Solo los usuarios con rol ADMIN pueden crear usuarios que no sean EMPLEADOS.");
                }
            }
            validateUser(user);
        } else if("EMPLEADO".equalsIgnoreCase(authenticatedUserRole)){
            if (!"CLIENTE".equalsIgnoreCase(user.getRol())) {
                throw new IllegalArgumentException("No tienes permisos para crear este usuario con ese rol.");
            }
            validateUser(user);
        }else {
            throw new IllegalArgumentException("No tienes permisos para crear usuarios.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRol(user.getRol().toUpperCase());
        return userPort.save(user);
    }

    private void validateUser(User user){
        if (user.getDate_birth() == null || !isAdult(user.getDate_birth())){
            throw new IllegalArgumentException("El usuario debe ser mayor de edad");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            throw new IllegalArgumentException("El correo no tiene un formato valido");
        }

        if (!user.getDocument_number().matches("\\d+")){
            throw new IllegalArgumentException("El documento debe ser númerico");
        }

        if (!user.getPhone().matches("^\\+?\\d{1,13}$")){
            throw new IllegalArgumentException("El celular debe tener un maximos de 13 caracteres y puede contener el simbolo +");
        }
    }

    private boolean isAdult(LocalDate date_birth){
        return LocalDate.now().minusYears(18).isAfter(date_birth);
    }

    public Optional<User> findByEmail(String email) {
        return userPort.findByEmail(email);
    }

    public Optional<User> findById(Long id, String rol) {

        if(!"CLIENTE".equalsIgnoreCase(rol)){
            return userPort.findById(id);
        } else {
            throw new IllegalArgumentException("No tienes permisos para esta acción");
        }

    }
}
