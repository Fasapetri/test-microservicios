package com.example.users.application.dto;

import com.example.users.application.constants.UserRequestConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = UserRequestConstants.USER_REQUEST_DESCRIPTION)
public class UserRequest {

    @Schema(description = UserRequestConstants.ID_DESCRIPTION, example = UserRequestConstants.ID_EXAMPLE, required = true)
    private Long id;

    @Schema(description = UserRequestConstants.EMAIL_DESCRIPTION, example = UserRequestConstants.EMAIL_EXAMPLE, required = true)
    private String email;

    @Schema(description = UserRequestConstants.PASSWORD_DESCRIPTION, example = UserRequestConstants.PASSWORD_EXAMPLE, required = true)
    private String password;

    @Schema(description = UserRequestConstants.NAME_DESCRIPTION, example = UserRequestConstants.NAME_EXAMPLE, required = true)
    private String name;

    @Schema(description = UserRequestConstants.LAST_NAME_DESCRIPTION, example = UserRequestConstants.LAST_NAME_EXAMPLE)
    private String last_name;

    @Schema(description = UserRequestConstants.DOCUMENT_NUMBER_DESCRIPTION, example = UserRequestConstants.DOCUMENT_NUMBER_EXAMPLE, required = true)
    private String document_number;

    @Schema(description = UserRequestConstants.PHONE_DESCRIPTION, example = UserRequestConstants.PHONE_EXAMPLE, required = true)
    private String phone;

    @Schema(description = UserRequestConstants.DATE_BIRTH_DESCRIPTION, example = UserRequestConstants.DATE_BIRTH_EXAMPLE)
    private LocalDate date_birth;
}
