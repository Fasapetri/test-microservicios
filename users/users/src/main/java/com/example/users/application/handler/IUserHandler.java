package com.example.users.application.handler;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;

public interface IUserHandler {

    UserResponse saveUserPropietario(UserRequest userPropietarioToCreate);

    UserResponse saveUserEmpleado(UserRequest userEmpleadoToCreate);

    UserResponse saveUserCliente(UserRequest userClienteToCreate);

    UserResponse findByIdUser(Long id);

    UserResponse updateUser(UserRequest user);

    void deleteUser(Long userId);

    boolean existsUserById(Long findUserId);

}
