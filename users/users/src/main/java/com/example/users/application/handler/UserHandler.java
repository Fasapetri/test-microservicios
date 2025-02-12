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
    private final IJwtServicePort iJwtServicePort;
    private final IPasswordEncodePort iPasswordEncodePort;

    @Override
    public UserResponse saveUser(UserRequest userRequest, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        User user = userMapper.userRequestToUser(userRequest);
        user.setPassword(iPasswordEncodePort.encode(user.getPassword()));
        iUserServicePort.saveUser(user, rol);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse findByEmailUser(String email, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        User user = iUserServicePort.findByEmailUser(email, rol);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse findByIdUser(Long id, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        User user = iUserServicePort.findByIdUser(id, rol);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        User oldUser = iUserServicePort.findByIdUser(userRequest.getId(), rol);
        User newUser = userMapper.userRequestToUser(userRequest);
        newUser.setId(oldUser.getId());
        iUserServicePort.updateUser(newUser, rol);
        return userMapper.userToUserResponse(newUser);
    }

    @Override
    public void deleteUser(Long userId, String token) {
        String rol = iJwtServicePort.extractRoleFromToken(token);
        User user = iUserServicePort.findByIdUser(userId, rol);
        iUserServicePort.deleteUser(user.getId(), rol);
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> listUser = iUserServicePort.getAllUser();
        return userMapper.listUserToUserResponse(listUser);
    }
}
