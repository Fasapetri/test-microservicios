package com.example.users.domain.api;


import com.example.users.domain.model.User;

import java.util.List;


public interface IUserServicePort {

    User saveUserPropietario(User userPropietarioToCreate);

    User saveUserEmpleado(User userEmpleadoToCreate);

    User saveUserCliente(User userClienteToCreate);

    User findByIdUser(Long findUserId);

    User updateUser(User userToUpdate);

    void deleteUser(Long findUserId);

    List<User> getAllUser();

    boolean existsUserById(Long findUserId);

}
