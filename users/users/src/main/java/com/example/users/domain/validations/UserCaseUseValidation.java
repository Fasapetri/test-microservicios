package com.example.users.domain.validations;

import com.example.users.domain.exception.UserException;
import com.example.users.domain.exception.UserExceptionType;
import com.example.users.domain.model.User;

import java.time.LocalDate;

import static com.example.users.domain.constants.UserConstants.*;
import static com.example.users.domain.constants.UserConstants.ROLE_CLIENTE;

public class UserCaseUseValidation {

    public void validateUserData(User userData){
        if (userData.getDate_birth() == null || !validateIsAdult(userData.getDate_birth())){
            throw new UserException(UserExceptionType.INVALID_AGE);
        }

        if (!userData.getEmail().matches(EMAIL_FORMAT_REGEX)){
            throw new UserException(UserExceptionType.INVALID_EMAIL);
        }

        if (!userData.getDocument_number().matches(ONLY_DIGITS_REGEX)){
            throw new UserException(UserExceptionType.INVALID_DOCUMENT);
        }

        if (!userData.getPhone().matches(PHONE_NUMBER_REGEX)){
            throw new UserException(UserExceptionType.INVALID_PHONE);
        }
    }

    public boolean validateIsAdult(LocalDate dateBirthUserToCreate){
        return LocalDate.now().minusYears(MIN_ADULT_AGE).isAfter(dateBirthUserToCreate);
    }

    public void validateUserAuthenticatedRolPermissionUser(User userData, String userAuthenticateRol){
        if (ROLE_ADMIN.equalsIgnoreCase(userAuthenticateRol)) {
            if (!ROLE_PROPIETARIO.equalsIgnoreCase(userData.getRol()) && !ROLE_CLIENTE.equalsIgnoreCase(userData.getRol())) {
                throw new UserException(UserExceptionType.INVALID_ROL_ADMIN_CREATED_USER);
            }
        } else if (ROLE_PROPIETARIO.equalsIgnoreCase(userAuthenticateRol)) {
            if (!ROLE_EMPLEADO.equalsIgnoreCase(userData.getRol()) && !ROLE_CLIENTE.equalsIgnoreCase(userData.getRol())) {
                throw new UserException(UserExceptionType.INVALID_ROL_PROPIETARIO_CREATED_USER);
            }
        } else if (ROLE_EMPLEADO.equalsIgnoreCase(userAuthenticateRol)) {
            if (!ROLE_CLIENTE.equalsIgnoreCase(userData.getRol())) {
                throw new UserException(UserExceptionType.ROLE_NOT_ALLOWED);
            }
        } else {
            throw new UserException(UserExceptionType.ROLE_NOT_ALLOWED);
        }
    }
}
