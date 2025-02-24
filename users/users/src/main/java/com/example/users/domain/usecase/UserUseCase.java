package com.example.users.domain.usecase;

import com.example.users.domain.spi.IJwtServicePort;
import com.example.users.domain.spi.IPasswordEncodePort;
import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.exception.UserException;
import com.example.users.domain.exception.UserExceptionType;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.ISecurityContextPort;
import com.example.users.domain.spi.IUserPersistencePort;
import com.example.users.domain.validations.UserCaseUseValidation;
import com.example.users.infraestructure.exception.UserEntityException;
import com.example.users.infraestructure.exception.UserEntityExceptionType;
import com.example.users.infraestructure.output.jpa.entity.UserEntity;

import java.time.LocalDate;
import java.util.List;

import static com.example.users.domain.constants.UserConstants.*;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort iUserPersistencePort;
    private final IPasswordEncodePort iPasswordEncodePort;
    private final ISecurityContextPort iSecurityContextPort;
    private final UserCaseUseValidation userCaseUseValidation;

    public UserUseCase(IUserPersistencePort iUserPersistencePort, IPasswordEncodePort iPasswordEncodePort, ISecurityContextPort iSecurityContextPort, UserCaseUseValidation userCaseUseValidation) {
        this.iUserPersistencePort = iUserPersistencePort;
        this.iPasswordEncodePort = iPasswordEncodePort;
        this.iSecurityContextPort = iSecurityContextPort;
        this.userCaseUseValidation = userCaseUseValidation;
    }

    @Override
    public User saveUser(User userToCreate) {
        String userAuthenticatedRol = iSecurityContextPort.getUserAuthenticateRol();
        if(findByEmailUser(userToCreate.getEmail()) != null){
            throw new UserException(UserExceptionType.EMAIL_USER_EXISTS);
        }
        userCaseUseValidation.validateUserData(userToCreate);
        userCaseUseValidation.validateUserAuthenticatedRolPermissionUser(userToCreate, userAuthenticatedRol);
        userToCreate.setPassword(iPasswordEncodePort.encode(userToCreate.getPassword()));
        return iUserPersistencePort.saveUser(userToCreate);
    }

    @Override
    public User findByEmailUser(String email) {
        return iUserPersistencePort.findByEmailUser(email);
    }

    @Override
    public User findByIdUser(Long id) {
        return iUserPersistencePort.findByIdUser(id);
    }

    @Override
    public User updateUser(User userToUpdate) {
        String userAuthenticatedRol =  iSecurityContextPort.getUserAuthenticateRol();
        User oldUser = findByIdUser(userToUpdate.getId());
        if(oldUser == null){
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }
        userCaseUseValidation.validateUserData(userToUpdate);
        userCaseUseValidation.validateUserAuthenticatedRolPermissionUser(userToUpdate, userAuthenticatedRol);
        userToUpdate.setId(oldUser.getId());
        return iUserPersistencePort.updateUser(userToUpdate);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findByIdUser(userId);
        if(user == null){
            throw new UserException(UserExceptionType.USER_NOT_FOUND);
        }
        iUserPersistencePort.deleteUser(user.getId());
    }

    @Override
    public List<User> getAllUser() {
        List<User> listUser = iUserPersistencePort.getAllUser();
        if(listUser.isEmpty()){
            throw new UserException(UserExceptionType.USER_NOT_DATA);
        }
        return listUser;
    }

    }

