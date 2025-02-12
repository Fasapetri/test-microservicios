package com.example.users.application.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

    private Long id;
    private String email;
    private String rol;
    private String password;
    private String name;
    private String last_name;
    private String document_number;
    private String phone;
    private LocalDate date_birth;
}
