package com.example.users.infraestructure.output.jpa.adapter;

import com.example.users.domain.model.User;
import com.example.users.infraestructure.output.jpa.entity.UserEntity;
import com.example.users.infraestructure.output.jpa.mapper.UserEntityMapper;
import com.example.users.infraestructure.output.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserJpaAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser(){
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("securepassword");
        user.setRol("ADMIN");
        user.setName("Juan");
        user.setLast_name("Rodriguez");
        user.setDocument_number("123456789");
        user.setPhone("123456789");
        user.setDate_birth(LocalDate.of(1990, 1, 1));

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("securepassword");
        userEntity.setRol("ADMIN");
        userEntity.setName("Juan");
        userEntity.setLast_name("Rodriguez");
        userEntity.setDocument_number("123456789");
        userEntity.setPhone("123456789");
        userEntity.setDate_birth(LocalDate.of(1990, 1, 1));

        doAnswer(invocation -> {
            User param = invocation.getArgument(0);
            System.out.println("🔎 Se llamó a toUserEntity con: " + param);
            return userEntity;
        }).when(userEntityMapper).toUserEntity(any(User.class));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        lenient().doReturn(user).when(userEntityMapper).toUser(any(UserEntity.class));

        System.out.println("🔎 userJpaAdapter en prueba: " + userJpaAdapter);
        System.out.println("🔎 userEntityMapper en prueba: " + userEntityMapper);

        User resultSave = userJpaAdapter.saveUser(user);

        System.out.println("User guardado: " + resultSave);

        assertNotNull(resultSave, "El usuario guardado no debe ser null");
        assertEquals("test@example.com", resultSave.getEmail());

        verify(userEntityMapper, times(1)).toUser(any(UserEntity.class));

    }
}