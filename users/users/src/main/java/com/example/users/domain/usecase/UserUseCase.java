package com.example.users.domain.usecase;

import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.exception.UserCreationException;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.util.List;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort iUserPersistencePort;

    public UserUseCase(IUserPersistencePort iUserPersistencePort) {
        this.iUserPersistencePort = iUserPersistencePort;
    }

    @Override
    public User saveUser(User user, String rol) {

        validateUser(user);
        validateUserRol(user, rol);
        return iUserPersistencePort.saveUser(user);
    }

    @Override
    public User findByEmailUser(String email, String rol) {
        return iUserPersistencePort.findByEmailUser(email);
    }

    @Override
    public User findByIdUser(Long id, String rol) {
        return iUserPersistencePort.findByIdUser(id);
    }

    @Override
    public User updateUser(User user, String rol) {

        validateUser(user);
        validateUserRol(user, rol);
        return iUserPersistencePort.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId, String rol) {
        iUserPersistencePort.deleteUser(userId);
    }

    @Override
    public List<User> getAllUser() {
        return iUserPersistencePort.getAllUser();
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

    private void validateUserRol(User user, String rol){
        if ("ADMIN".equalsIgnoreCase(rol)) {
            if (!"PROPIETARIO".equalsIgnoreCase(user.getRol()) && !"CLIENTE".equalsIgnoreCase(user.getRol())) {
                throw new UserCreationException("Solo los usuarios con rol PROPIETARIO pueden crear usuarios EMPLEADOS.");
            }
        } else if ("PROPIETARIO".equalsIgnoreCase(rol)) {
            if (!"EMPLEADO".equalsIgnoreCase(user.getRol()) && !"CLIENTE".equalsIgnoreCase(user.getRol())) {
                throw new UserCreationException("Solo los usuarios con rol ADMIN pueden crear usuarios que no sean EMPLEADOS." + user.getRol());
            }
        } else if ("EMPLEADO".equalsIgnoreCase(rol)) {
            if (!"CLIENTE".equalsIgnoreCase(user.getRol())) {
                throw new UserCreationException("No tienes permisos para crear este usuario con ese rol.");
            }
        } else {
            throw new UserCreationException("No tienes permisos para crear usuarios.");
        }
    }
    }

