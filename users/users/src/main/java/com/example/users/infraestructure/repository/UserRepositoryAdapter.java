package com.example.users.infraestructure.repository;

import com.example.users.domain.User;
import com.example.users.domain.UserPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserPort {

    @Autowired
    private UserRepository jpaRepository;

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id);
    }
}
