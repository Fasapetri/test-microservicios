package com.example.users.domain.api;


import com.example.users.domain.model.User;

import java.util.List;


public interface IUserServicePort {

    User saveUser(User user, String token);

    User findByEmailUser(String email, String token);

    User findByIdUser(Long id, String token);

    User updateUser(User user, String token);

    void deleteUser(Long userId, String token);

    List<User> getAllUser();

}
