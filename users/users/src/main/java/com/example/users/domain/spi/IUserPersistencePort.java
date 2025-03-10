package com.example.users.domain.spi;

import com.example.users.domain.model.User;

import java.util.List;

public interface IUserPersistencePort {

    User saveUser(User user);

    User findByEmailUser(String email);

    User findByIdUser(Long id);

    User updateUser(User user);

    void deleteUser(Long userId);

    List<User> getAllUser();

    boolean existsUserById(Long findUserId);
}
