package com.example.users.domain.usecase;

import com.example.users.domain.exception.UserException;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort iUserPersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("securepassword");
        testUser.setRol("CLIENTE");
        testUser.setName("John");
        testUser.setLast_name("Doe");
        testUser.setDocument_number("12345678");
        testUser.setPhone("+1234567890");
        testUser.setDate_birth(LocalDate.of(2000, 1, 1));
    }

    @Test
    void testSaveUserSuccess(){
        when(iUserPersistencePort.saveUser(any(User.class))).thenReturn(testUser);

        User savedUser = userUseCase.saveUser(testUser);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        verify(iUserPersistencePort, times(1)).findByEmailUser("test@example.com");
        verify(iUserPersistencePort, times(1)).saveUser(any(User.class));

    }

    @Test
    void testSaveUserInvalidRoleShouldThrowException() {
        testUser.setRol("INVALID_ROLE");

        Exception exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUser(testUser);
        });

        assertTrue(exception.getMessage().contains("Solo los usuarios con rol ADMIN pueden crear usuarios"));
    }

    @Test
    void testFindByEmailUser_Success() {
        when(iUserPersistencePort.findByEmailUser("test@example.com")).thenReturn(testUser);

        User foundUser = userUseCase.findByEmailUser("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(iUserPersistencePort, times(1)).findByEmailUser("test@example.com");
    }

    @Test
    void testFindByIdUserSuccess() {
        when(iUserPersistencePort.findByIdUser(1L)).thenReturn(testUser);

        User foundUser = userUseCase.findByIdUser(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        verify(iUserPersistencePort, times(1)).findByIdUser(1L);
    }

    @Test
    void testUpdateUserSuccess() {
        when(iUserPersistencePort.updateUser(any(User.class))).thenReturn(testUser);

        User updatedUser = userUseCase.updateUser(testUser);

        assertNotNull(updatedUser);
        assertEquals("test@example.com", updatedUser.getEmail());
        verify(iUserPersistencePort, times(1)).updateUser(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        doNothing().when(iUserPersistencePort).deleteUser(1L);

        assertDoesNotThrow(() -> userUseCase.deleteUser(1L));

        verify(iUserPersistencePort, times(1)).deleteUser(1L);
    }

    @Test
    void testValidateUserInvalidEmailShouldThrowException() {
        testUser.setEmail("invalid-email");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.saveUser(testUser);
        });

        assertTrue(exception.getMessage().contains("El correo no tiene un formato valido"));
    }

    @Test
    void testValidateUserUnderageShouldThrowException() {
        testUser.setDate_birth(LocalDate.now().minusYears(16));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.saveUser(testUser);
        });

        assertTrue(exception.getMessage().contains("El usuario debe ser mayor de edad"));
    }

}