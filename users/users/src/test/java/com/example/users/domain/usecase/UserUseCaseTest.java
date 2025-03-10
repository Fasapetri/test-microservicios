package com.example.users.domain.usecase;

import com.example.users.domain.constants.UserConstants;
import com.example.users.domain.exception.UserException;
import com.example.users.domain.exception.UserExceptionType;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.IPasswordEncodePort;
import com.example.users.domain.spi.IUserPersistencePort;
import com.example.users.domain.validations.UserCaseUseValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncodePort passwordEncodePort;

    @Mock
    private UserCaseUseValidation userCaseUseValidation;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;
    private User updatedUser;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Juan");
        user.setLast_name("Pérez");
        user.setDate_birth(LocalDate.of(2000, 1, 1)); // Mayor de edad
        user.setDocument_number("12345678");
        user.setPhone("987654321");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setPassword("password123");
        user2.setName("Miguel");
        user2.setLast_name("Fernandez");
        user2.setDate_birth(LocalDate.of(1998, 1, 1)); // Mayor de edad
        user2.setDocument_number("987654321");
        user2.setPhone("1235648975");

        userList = Arrays.asList(user, user2);

        updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setName("Updated Name");
        updatedUser.setPhone("987654321");
    }

    @Test
    void testSaveUserPropietarioSuccess(){
        when(userPersistencePort.findByEmailUser(user.getEmail())).thenReturn(null);
        when(passwordEncodePort.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        User result = userUseCase.saveUserPropietario(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(UserConstants.ROLE_PROPIETARIO, result.getRol());

        verify(userCaseUseValidation).validateUserData(user);
        verify(userPersistencePort).findByEmailUser(user.getEmail());
        verify(passwordEncodePort).encode(anyString());
        verify(userPersistencePort).saveUser(user);

    }

    @Test
    void testSaveUserPropietarioEmailAlreadyExists() {
        when(userPersistencePort.findByEmailUser(user.getEmail())).thenReturn(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserPropietario(user);
        });

        assertEquals(UserExceptionType.EMAIL_USER_EXISTS, exception.getType());

        verify(userPersistencePort).findByEmailUser(user.getEmail());
        verifyNoInteractions(passwordEncodePort);
        verifyNoMoreInteractions(userPersistencePort);
    }

    @Test
    void testSaveUserPropietarioInvalidEmailFormat() {
        user.setEmail("invalid-email");

        doThrow(new UserException(UserExceptionType.INVALID_EMAIL))
                .when(userCaseUseValidation).validateUserData(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserPropietario(user);
        });

        assertEquals(UserExceptionType.INVALID_EMAIL, exception.getType());

        verify(userCaseUseValidation).validateUserData(user);
        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(passwordEncodePort);
    }

    @Test
    void testSaveUserPropietarioInvalidAge() {
        user.setDate_birth(LocalDate.of(2015, 1, 1));

        doThrow(new UserException(UserExceptionType.INVALID_AGE))
                .when(userCaseUseValidation).validateUserData(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserPropietario(user);
        });

        assertEquals(UserExceptionType.INVALID_AGE, exception.getType());

        verify(userCaseUseValidation).validateUserData(user);
        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(passwordEncodePort);
    }

    @Test
    void testSaveUserEmpleadoSuccess(){
        when(userPersistencePort.findByEmailUser(user.getEmail())).thenReturn(null);
        when(passwordEncodePort.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        User result = userUseCase.saveUserEmpleado(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(UserConstants.ROLE_EMPLEADO, result.getRol());

        verify(userCaseUseValidation).validateUserData(user);
        verify(userPersistencePort).findByEmailUser(user.getEmail());
        verify(passwordEncodePort).encode(anyString());
        verify(userPersistencePort).saveUser(user);

    }

    @Test
    void testSaveUserEmpleadoEmailAlreadyExists() {
        when(userPersistencePort.findByEmailUser(user.getEmail())).thenReturn(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserEmpleado(user);
        });

        assertEquals(UserExceptionType.EMAIL_USER_EXISTS, exception.getType());

        verify(userPersistencePort).findByEmailUser(user.getEmail());
        verifyNoInteractions(passwordEncodePort);
        verifyNoMoreInteractions(userPersistencePort);
    }

    @Test
    void testSaveUserEmpleadoInvalidEmailFormat() {
        user.setEmail("invalid-email");

        doThrow(new UserException(UserExceptionType.INVALID_EMAIL))
                .when(userCaseUseValidation).validateUserData(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserEmpleado(user);
        });

        assertEquals(UserExceptionType.INVALID_EMAIL, exception.getType());

        verify(userCaseUseValidation).validateUserData(user);
        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(passwordEncodePort);
    }

    @Test
    void testSaveUserEmpleadoInvalidAge() {
        user.setDate_birth(LocalDate.of(2015, 1, 1));

        doThrow(new UserException(UserExceptionType.INVALID_AGE))
                .when(userCaseUseValidation).validateUserData(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserEmpleado(user);
        });

        assertEquals(UserExceptionType.INVALID_AGE, exception.getType());

        verify(userCaseUseValidation).validateUserData(user);
        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(passwordEncodePort);
    }

    @Test
    void testSaveUserClienteSuccess(){
        when(userPersistencePort.findByEmailUser(user.getEmail())).thenReturn(null);
        when(passwordEncodePort.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        User result = userUseCase.saveUserCliente(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(UserConstants.ROLE_CLIENTE, result.getRol());

        verify(userCaseUseValidation).validateUserData(user);
        verify(userPersistencePort).findByEmailUser(user.getEmail());
        verify(passwordEncodePort).encode(anyString());
        verify(userPersistencePort).saveUser(user);

    }

    @Test
    void testSaveUserClienteEmailAlreadyExists() {
        when(userPersistencePort.findByEmailUser(user.getEmail())).thenReturn(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserCliente(user);
        });

        assertEquals(UserExceptionType.EMAIL_USER_EXISTS, exception.getType());

        verify(userPersistencePort).findByEmailUser(user.getEmail());
        verifyNoInteractions(passwordEncodePort);
        verifyNoMoreInteractions(userPersistencePort);
    }

    @Test
    void testSaveUserClienteInvalidEmailFormat() {
        user.setEmail("invalid-email");

        doThrow(new UserException(UserExceptionType.INVALID_EMAIL))
                .when(userCaseUseValidation).validateUserData(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserCliente(user);
        });

        assertEquals(UserExceptionType.INVALID_EMAIL, exception.getType());

        verify(userCaseUseValidation).validateUserData(user);
        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(passwordEncodePort);
    }

    @Test
    void testSaveUserClienteInvalidAge() {
        user.setDate_birth(LocalDate.of(2015, 1, 1));

        doThrow(new UserException(UserExceptionType.INVALID_AGE))
                .when(userCaseUseValidation).validateUserData(user);

        UserException exception = assertThrows(UserException.class, () -> {
            userUseCase.saveUserCliente(user);
        });

        assertEquals(UserExceptionType.INVALID_AGE, exception.getType());

        verify(userCaseUseValidation).validateUserData(user);
        verifyNoInteractions(userPersistencePort);
        verifyNoInteractions(passwordEncodePort);
    }

    @Test
    void testFindByIdUserSuccess(){
        when(userPersistencePort.findByIdUser(1L)).thenReturn(user);

        User result = userUseCase.findByIdUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userPersistencePort).findByIdUser(1L);

    }

    @Test
    void testFindByIdUserNotFounUser(){
        when(userPersistencePort.findByIdUser(2L)).thenReturn(null);

        UserException exception = assertThrows(UserException.class, () -> {
           userUseCase.findByIdUser(2L);
        });

        assertEquals(UserExceptionType.USER_NOT_FOUND, exception.getType());

        verify(userPersistencePort).findByIdUser(2L);
    }

    @Test
    void testUpdateUserSuccess(){
        when(userPersistencePort.findByIdUser(1L)).thenReturn(user);
        when(userPersistencePort.updateUser(updatedUser)).thenReturn(updatedUser);

        User result = userUseCase.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("987654321", result.getPhone());

        verify(userCaseUseValidation).validateUserData(updatedUser);
        verify(userPersistencePort).findByIdUser(1L);
        verify(userPersistencePort).updateUser(updatedUser);
    }

    @Test
    void testDeleteUserSuccess() {
        when(userPersistencePort.findByIdUser(1L)).thenReturn(user);

        assertDoesNotThrow(() -> userUseCase.deleteUser(1L));

        verify(userPersistencePort).findByIdUser(1L);
        verify(userPersistencePort).deleteUser(1L);
    }

    @Test
    void testGetAllUserSuccess() {
        when(userPersistencePort.getAllUser()).thenReturn(userList);

        List<User> result = userUseCase.getAllUser();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());

        verify(userPersistencePort).getAllUser();
    }
}