package com.example.users.application.handler;

import com.example.users.application.dto.UserRequest;
import com.example.users.application.dto.UserResponse;
import com.example.users.application.mapper.UserMapper;
import com.example.users.domain.api.IUserServicePort;
import com.example.users.domain.exception.UserException;
import com.example.users.domain.exception.UserExceptionType;
import com.example.users.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserHandler userHandler;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setName("Juan");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Juan");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@example.com");
        userResponse.setName("Juan");
    }

    @Test
    void testSaveUserPropietarioSuccess() {
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userServicePort.saveUserPropietario(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userHandler.saveUserPropietario(userRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Juan", result.getName());

        verify(userMapper).userRequestToUser(userRequest);
        verify(userServicePort).saveUserPropietario(user);
        verify(userMapper).userToUserResponse(user);
    }

    @Test
    void testSaveUserPropietarioFailure() {
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userServicePort.saveUserPropietario(user)).thenThrow(new UserException(UserExceptionType.EMAIL_USER_EXISTS));

        UserException exception = assertThrows(UserException.class, () -> {
            userHandler.saveUserPropietario(userRequest);
        });

        assertEquals(UserExceptionType.EMAIL_USER_EXISTS, exception.getType());

        verify(userMapper).userRequestToUser(userRequest);
        verify(userServicePort).saveUserPropietario(user);
        verifyNoMoreInteractions(userMapper); // No debe llamar a `userToUserResponse`
    }

    @Test
    void testSaveUserEmpleadoSuccess() {
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userServicePort.saveUserEmpleado(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userHandler.saveUserEmpleado(userRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Juan", result.getName());

        verify(userMapper).userRequestToUser(userRequest);
        verify(userServicePort).saveUserEmpleado(user);
        verify(userMapper).userToUserResponse(user);
    }

    @Test
    void testSaveUserEmpleadoFailure() {
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userServicePort.saveUserEmpleado(user)).thenThrow(new UserException(UserExceptionType.EMAIL_USER_EXISTS));

        UserException exception = assertThrows(UserException.class, () -> {
            userHandler.saveUserEmpleado(userRequest);
        });

        assertEquals(UserExceptionType.EMAIL_USER_EXISTS, exception.getType());

        verify(userMapper).userRequestToUser(userRequest);
        verify(userServicePort).saveUserEmpleado(user);
        verifyNoMoreInteractions(userMapper); // No debe llamar a `userToUserResponse`
    }

    @Test
    void testSaveUserClienteSuccess() {
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userServicePort.saveUserCliente(user)).thenReturn(user);
        when(userMapper.userToUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userHandler.saveUserCliente(userRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Juan", result.getName());

        verify(userMapper).userRequestToUser(userRequest);
        verify(userServicePort).saveUserCliente(user);
        verify(userMapper).userToUserResponse(user);
    }

    @Test
    void testSaveUserClienteFailure() {
        when(userMapper.userRequestToUser(userRequest)).thenReturn(user);
        when(userServicePort.saveUserCliente(user)).thenThrow(new UserException(UserExceptionType.EMAIL_USER_EXISTS));

        UserException exception = assertThrows(UserException.class, () -> {
            userHandler.saveUserCliente(userRequest);
        });

        assertEquals(UserExceptionType.EMAIL_USER_EXISTS, exception.getType());

        verify(userMapper).userRequestToUser(userRequest);
        verify(userServicePort).saveUserCliente(user);
        verifyNoMoreInteractions(userMapper); // No debe llamar a `userToUserResponse`
    }
}