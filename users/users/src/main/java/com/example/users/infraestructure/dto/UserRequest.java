package com.example.users.infraestructure.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

    private String email;
    private String rol;
    private String password;
    private String name;
    private String last_name;
    private String document_number;
    private String phone;
    private LocalDate date_birth;
}
