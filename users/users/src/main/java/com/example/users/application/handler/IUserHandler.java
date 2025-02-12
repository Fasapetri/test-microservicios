package com.example.users.application.handler;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;

import java.util.List;

public interface IUserHandler {

    UserResponse saveUser(UserRequest userRequest, String token);

    UserResponse findByEmailUser(String email, String token);

    UserResponse findByIdUser(Long id, String token);

    UserResponse updateUser(UserRequest user, String token);

    void deleteUser(Long userId, String token);

    List<UserResponse> getAllUser();
}
