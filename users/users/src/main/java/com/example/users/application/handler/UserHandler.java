package com.example.users.application.handler;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.mapper.UserMapper;
import com.example.users.domain.api.IJwtServicePort;
import com.example.users.domain.api.IPasswordEncodePort;
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

    private final IUserServicePort iUserServicePort;
    private final UserMapper userMapper;

    @Override
    public UserResponse saveUser(UserRequest userRequest, String token) {
        User user = userMapper.userRequestToUser(userRequest);
        iUserServicePort.saveUser(user, token);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse findByEmailUser(String email, String token) {
        User user = iUserServicePort.findByEmailUser(email, token);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse findByIdUser(Long id, String token) {
        User user = iUserServicePort.findByIdUser(id, token);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, String token) {
        User newUser = userMapper.userRequestToUser(userRequest);
        iUserServicePort.updateUser(newUser, token);
        return userMapper.userToUserResponse(newUser);
    }

    @Override
    public void deleteUser(Long userId, String token) {
        iUserServicePort.deleteUser(userId, token);
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> listUser = iUserServicePort.getAllUser();
        return userMapper.listUserToUserResponse(listUser);
    }
}
