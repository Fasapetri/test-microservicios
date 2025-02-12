package com.example.users.application.dto;

import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String password;
}
