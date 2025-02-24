package com.example.users.application.handler;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;

import java.util.List;

public interface IUserHandler {

    UserResponse saveUser(UserRequest userRequest);

    UserResponse findByEmailUser(String email);

    UserResponse findByIdUser(Long id);

    UserResponse updateUser(UserRequest user);

    void deleteUser(Long userId);

    List<UserResponse> getAllUser();
}
