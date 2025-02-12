package com.example.users.service;

import com.example.users.application.UserService;
import com.example.users.domain.User;
import com.example.users.infraestructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUser() {

        User user = User.builder()
                .email("test@example.com")
                .password("123")
                .rol("PROPIETARIO")
                .name("John")
                .last_name("Doe")
                .document_number("123456789")
                .phone("+573005698325")
                .date_birth(LocalDate.of(1990, 1, 1))
                .build();

        when(passwordEncoder.encode("123")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock -> {
                User userArg = invocationOnMock.getArgument(0);
                userArg.setId(1L);
                return userArg;
    });

        User result = userServiceImpl.addUser(user);

        assertNotNull(result);
        assertEquals("encoded123", result.getPassword());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("PROPIETARIO", result.getRol());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("123");
    }

    @Test
    void findByEmail(){

        User user = User.builder()
                .email("test@example.com")
                .password("123")
                .rol("CLIENTE")
                .build();


        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));


        Optional<User> result = userRepository.findByEmail("test@example.com");


        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
}