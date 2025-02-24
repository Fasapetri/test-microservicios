package com.example.users.application.handler;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.mapper.UserMapper;
import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler{

    private final IUserServicePort userServicePort;
    private final UserMapper userMapper;

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        User user = userMapper.userRequestToUser(userRequest);
        User savedUser = userServicePort.saveUser(user);
        return userMapper.userToUserResponse(savedUser);
    }

    @Override
    public UserResponse findByEmailUser(String email) {
        User user = userServicePort.findByEmailUser(email);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse findByIdUser(Long id) {
        User user = userServicePort.findByIdUser(id);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        User newUser = userMapper.userRequestToUser(userRequest);
        userServicePort.updateUser(newUser);
        return userMapper.userToUserResponse(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userServicePort.deleteUser(userId);
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> listUser = userServicePort.getAllUser();
        return userMapper.listUserToUserResponse(listUser);
    }
}
