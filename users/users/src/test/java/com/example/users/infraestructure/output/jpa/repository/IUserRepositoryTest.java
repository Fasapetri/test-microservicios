package com.example.users.infraestructure.output.jpa.repository;

import com.example.users.infraestructure.output.jpa.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class IUserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();
        userRepository.flush();

        userEntity = new UserEntity();
        userEntity.setEmail("test2@example.com");
        userEntity.setPassword("securepassword");
        userEntity.setRol("ADMIN");
        userEntity.setName("Juan");
        userEntity.setLast_name("Rodriguez");
        userEntity.setDocument_number("123456789");
        userEntity.setPhone("123456789");
        userEntity.setDate_birth(LocalDate.of(1990, 1, 1));

        userRepository.save(userEntity);
    }

    @Test
    void testFindByEmailUserExists() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("test2@example.com");

        assertTrue(foundUser.isPresent(), "El usuario debería existir en la BD");
        assertEquals("test2@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("notfound@example.com");

        assertFalse(foundUser.isPresent(), "No debería encontrar un usuario inexistente");
    }
}