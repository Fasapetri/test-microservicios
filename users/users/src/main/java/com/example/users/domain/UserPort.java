package com.example.users.domain;

import java.util.Optional;

public interface UserPort {

    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
