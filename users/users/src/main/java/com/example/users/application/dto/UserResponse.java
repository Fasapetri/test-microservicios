package com.example.users.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String email;
    private String rol;
    private String name;
    private String last_name;
}
