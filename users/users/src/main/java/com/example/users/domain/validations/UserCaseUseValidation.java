package com.example.users.domain.validations;

import com.example.users.domain.exception.UserException;
import com.example.users.domain.exception.UserExceptionType;
import com.example.users.domain.model.User;

import java.time.LocalDate;

import static com.example.users.domain.constants.UserConstants.*;

public class UserCaseUseValidation {

    public void validateUserData(User userCreateData){
        if (userCreateData.getDate_birth() == null || !validateIsAdult(userCreateData.getDate_birth())){
            throw new UserException(UserExceptionType.INVALID_AGE);
        }

        if (!userCreateData.getEmail().matches(EMAIL_FORMAT_REGEX)){
            throw new UserException(UserExceptionType.INVALID_EMAIL);
        }

        if (!userCreateData.getDocument_number().matches(ONLY_DIGITS_REGEX)){
            throw new UserException(UserExceptionType.INVALID_DOCUMENT);
        }

        if (!userCreateData.getPhone().matches(PHONE_NUMBER_REGEX)){
            throw new UserException(UserExceptionType.INVALID_PHONE);
        }
    }

    public boolean validateIsAdult(LocalDate dateBirthUserToCreate){
        return LocalDate.now().minusYears(MIN_ADULT_AGE).isAfter(dateBirthUserToCreate);
    }


}
