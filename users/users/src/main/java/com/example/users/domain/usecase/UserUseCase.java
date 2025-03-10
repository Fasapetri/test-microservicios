package com.example.users.domain.usecase;

import com.example.users.domain.constants.UserConstants;
import com.example.users.domain.spi.IPasswordEncodePort;
import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.exception.UserException;
import com.example.users.domain.exception.UserExceptionType;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;
import com.example.users.domain.validations.UserCaseUseValidation;

import java.util.List;
import java.util.Optional;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncodePort passwordEncodePort;
    private final UserCaseUseValidation userCaseUseValidation;

    public UserUseCase(IUserPersistencePort iUserPersistencePort, IPasswordEncodePort iPasswordEncodePort, UserCaseUseValidation userCaseUseValidation) {
        this.userPersistencePort = iUserPersistencePort;
        this.passwordEncodePort = iPasswordEncodePort;
        this.userCaseUseValidation = userCaseUseValidation;
    }

    @Override
    public User saveUserPropietario(User userPropietarioToCreate) {

        userCaseUseValidation.validateUserData(userPropietarioToCreate);

        if(userPersistencePort.findByEmailUser(userPropietarioToCreate.getEmail()) != null){
            throw new UserException(UserExceptionType.EMAIL_USER_EXISTS);
        }

        userPropietarioToCreate.setPassword(passwordEncodePort.encode(userPropietarioToCreate.getPassword()));
        userPropietarioToCreate.setRol(UserConstants.ROLE_PROPIETARIO);
        return userPersistencePort.saveUser(userPropietarioToCreate);
    }

    @Override
    public User saveUserEmpleado(User userEmpleadoToCreate) {

        userCaseUseValidation.validateUserData(userEmpleadoToCreate);

        if(userPersistencePort.findByEmailUser(userEmpleadoToCreate.getEmail()) != null){
            throw new UserException(UserExceptionType.EMAIL_USER_EXISTS);
        }

        userEmpleadoToCreate.setPassword(passwordEncodePort.encode(userEmpleadoToCreate.getPassword()));
        userEmpleadoToCreate.setRol(UserConstants.ROLE_EMPLEADO);
        return userPersistencePort.saveUser(userEmpleadoToCreate);
    }

    @Override
    public User saveUserCliente(User userClienteToCreate) {

        userCaseUseValidation.validateUserData(userClienteToCreate);

        if(userPersistencePort.findByEmailUser(userClienteToCreate.getEmail()) != null){
            throw new UserException(UserExceptionType.EMAIL_USER_EXISTS);
        }

        userClienteToCreate.setPassword(passwordEncodePort.encode(userClienteToCreate.getPassword()));
        userClienteToCreate.setRol(UserConstants.ROLE_CLIENTE);
        return userPersistencePort.saveUser(userClienteToCreate);
    }


    @Override
    public User findByIdUser(Long findUserId) {
        return Optional.ofNullable(userPersistencePort.findByIdUser(findUserId))
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }

    @Override
    public User updateUser(User userToUpdate) {
        userCaseUseValidation.validateUserData(userToUpdate);

        User oldUser = findByIdUser(userToUpdate.getId());

        userToUpdate.setId(oldUser.getId());
        return userPersistencePort.updateUser(userToUpdate);
    }

    @Override
    public void deleteUser(Long findUserId) {
        User foundUserToDelete = findByIdUser(findUserId);
        userPersistencePort.deleteUser(foundUserToDelete.getId());
    }

    @Override
    public List<User> getAllUser() {

        return Optional.ofNullable(userPersistencePort.getAllUser())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_DATA));

    }

    @Override
    public boolean existsUserById(Long findUserId) {
        return userPersistencePort.existsUserById(findUserId);
    }


}

