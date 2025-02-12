package com.example.users.infraestructure.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String rol;
    private String name;
    private String last_name;
}
