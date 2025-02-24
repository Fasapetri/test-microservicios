package com.example.users.application.dto;

import com.example.users.application.constants.UserResponseConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = UserResponseConstants.USER_RESPONSE_DESCRIPTION)
public class UserResponse {

    @Schema(description = UserResponseConstants.ID_DESCRIPTION, example = UserResponseConstants.ID_EXAMPLE)
    private Long id;

    @Schema(description = UserResponseConstants.EMAIL_DESCRIPTION, example = UserResponseConstants.EMAIL_EXAMPLE)
    private String email;

    @Schema(description = UserResponseConstants.ROLE_DESCRIPTION, example = UserResponseConstants.ROLE_EXAMPLES)
    private String rol;

    @Schema(description = UserResponseConstants.NAME_DESCRIPTION, example = UserResponseConstants.NAME_EXAMPLE)
    private String name;

    @Schema(description = UserResponseConstants.LAST_NAME_DESCRIPTION, example = UserResponseConstants.LAST_NAME_EXAMPLE)
    private String last_name;

    @Schema(description = UserResponseConstants.PHONE_DESCRIPTION, example = UserResponseConstants.PHONE_EXAMPLE)
    private String phone;
}
