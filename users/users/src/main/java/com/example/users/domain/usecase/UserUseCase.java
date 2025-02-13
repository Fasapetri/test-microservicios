package com.example.users.domain.usecase;

import com.example.users.domain.api.IJwtServicePort;
import com.example.users.domain.api.IPasswordEncodePort;
import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.exception.UserException;
import com.example.users.domain.exception.UserExceptionType;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.util.List;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort iUserPersistencePort;
    private final IJwtServicePort iJwtServicePort;
    private final IPasswordEncodePort iPasswordEncodePort;

    public UserUseCase(IUserPersistencePort iUserPersistencePort, IJwtServicePort iJwtServicePort, IPasswordEncodePort iPasswordEncodePort) {
        this.iUserPersistencePort = iUserPersistencePort;
        this.iJwtServicePort = iJwtServicePort;
        this.iPasswordEncodePort = iPasswordEncodePort;
    }

    @Override
    public User saveUser(User user, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        validateUser(user);
        validateUserRol(user, rol);
        user.setPassword(iPasswordEncodePort.encode(user.getPassword()));
        return iUserPersistencePort.saveUser(user);
    }

    @Override
    public User findByEmailUser(String email, String token) {
        return iUserPersistencePort.findByEmailUser(email);
    }

    @Override
    public User findByIdUser(Long id, String token) {
        return iUserPersistencePort.findByIdUser(id);
    }

    @Override
    public User updateUser(User user, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        User oldUser = findByIdUser(user.getId(), rol);
        if(oldUser == null){
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }
        validateUser(user);
        validateUserRol(user, rol);
        user.setId(oldUser.getId());
        return iUserPersistencePort.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        User user = findByIdUser(userId, rol);
        if(user == null){
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }
        iUserPersistencePort.deleteUser(user.getId());
    }

    @Override
    public List<User> getAllUser() {
        return iUserPersistencePort.getAllUser();
    }

    private void validateUser(User user){
        if (user.getDate_birth() == null || !isAdult(user.getDate_birth())){
            throw new UserException(UserExceptionType.INVALID_AGE);
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            throw new UserException(UserExceptionType.INVALID_EMAIL);
        }

        if (!user.getDocument_number().matches("\\d+")){
            throw new UserException(UserExceptionType.INVALID_DOCUMENT);
        }

        if (!user.getPhone().matches("^\\+?\\d{1,13}$")){
            throw new UserException(UserExceptionType.INVALID_PHONE);
        }
    }

    private boolean isAdult(LocalDate date_birth){
        return LocalDate.now().minusYears(18).isAfter(date_birth);
    }

    private void validateUserRol(User user, String rol){
        if ("ADMIN".equalsIgnoreCase(rol)) {
            if (!"PROPIETARIO".equalsIgnoreCase(user.getRol()) && !"CLIENTE".equalsIgnoreCase(user.getRol())) {
                throw new UserException(UserExceptionType.INVALID_ROL_ADMIN_CREATED_USER);
            }
        } else if ("PROPIETARIO".equalsIgnoreCase(rol)) {
            if (!"EMPLEADO".equalsIgnoreCase(user.getRol()) && !"CLIENTE".equalsIgnoreCase(user.getRol())) {
                throw new UserException(UserExceptionType.INVALID_ROL_PROPIETARIO_CREATED_USER);
            }
        } else if ("EMPLEADO".equalsIgnoreCase(rol)) {
            if (!"CLIENTE".equalsIgnoreCase(user.getRol())) {
                throw new UserException(UserExceptionType.ROLE_NOT_ALLOWED);
            }
        } else {
            throw new UserException(UserExceptionType.ROLE_NOT_ALLOWED);
        }
    }
    }

