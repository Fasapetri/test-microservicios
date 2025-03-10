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
    public UserResponse saveUserPropietario(UserRequest userPropietarioToCreate) {
        User mapperUserPropietario = userMapper.userRequestToUser(userPropietarioToCreate);
        User savedUserPropietario = userServicePort.saveUserPropietario(mapperUserPropietario);
        return userMapper.userToUserResponse(savedUserPropietario);
    }

    @Override
    public UserResponse saveUserEmpleado(UserRequest userEmpleadoToCreate) {
        User mapperUserEmpleado = userMapper.userRequestToUser(userEmpleadoToCreate);
        User savedUserEmpleado = userServicePort.saveUserEmpleado(mapperUserEmpleado);
        return userMapper.userToUserResponse(savedUserEmpleado);
    }

    @Override
    public UserResponse saveUserCliente(UserRequest userClienteToCreate) {
        User mapperUserCliente = userMapper.userRequestToUser(userClienteToCreate);
        User savedUserCliente = userServicePort.saveUserCliente(mapperUserCliente);
        return userMapper.userToUserResponse(savedUserCliente);
    }

    @Override
    public UserResponse findByIdUser(Long findUserId) {
        User foundUser = userServicePort.findByIdUser(findUserId);
        return userMapper.userToUserResponse(foundUser);
    }

    @Override
    public UserResponse updateUser(UserRequest userToUpdate) {
        User mapperUser = userMapper.userRequestToUser(userToUpdate);
        userServicePort.updateUser(mapperUser);
        return userMapper.userToUserResponse(mapperUser);
    }

    @Override
    public void deleteUser(Long findUserId) {
        userServicePort.deleteUser(findUserId);
    }

    @Override
    public boolean existsUserById(Long findUserId) {
        return userServicePort.existsUserById(findUserId);
    }

}
