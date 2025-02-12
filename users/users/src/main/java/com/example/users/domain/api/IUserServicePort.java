package com.example.users.domain.api;


import com.example.users.domain.model.User;

import java.util.List;


public interface IUserServicePort {

    User saveUser(User user, String rol);

    User findByEmailUser(String email, String rol);

    User findByIdUser(Long id, String rol);

    User updateUser(User user, String rol);

    void deleteUser(Long userId, String rol);

    List<User> getAllUser();

}
