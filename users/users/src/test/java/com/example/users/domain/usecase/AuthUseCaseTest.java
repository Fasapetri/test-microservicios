package com.example.users.domain.usecase;

import com.example.users.domain.api.IJwtServicePort;
import com.example.users.domain.api.IPasswordEncodePort;
import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private IUserPersistencePort iUserPersistencePort;

    @Mock
    private IPasswordEncodePort iPasswordEncodePort;

    @Mock
    private IJwtServicePort iJwtServicePort;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$UOzI1bTZ4sp2D.wC0rH07uYs6WlKKyAgAw4Ir7CkxkhKnj7wpPLXS");
        testUser.setRol("CLIENTE");
    }

    @Test
    void testAuthenticateSuccess(){
        when(iUserPersistencePort.findByEmailUser("test@example.com")).thenReturn(testUser);
        when(iPasswordEncodePort.matches("1234", "$2a$10$UOzI1bTZ4sp2D.wC0rH07uYs6WlKKyAgAw4Ir7CkxkhKnj7wpPLXS")).thenReturn(true);
        when(iJwtServicePort.generarToken("test@example.com", "CLIENTE", 1L)).thenReturn("mockedToken");

        String token = authUseCase.authenticate("test@example.com", "1234");

        assertNotNull(token);
        assertEquals("mockedToken", token);
        verify(iUserPersistencePort, times(1)).findByEmailUser("test@example.com");
        verify(iPasswordEncodePort, times(1)).matches("1234", "$2a$10$UOzI1bTZ4sp2D.wC0rH07uYs6WlKKyAgAw4Ir7CkxkhKnj7wpPLXS");
        verify(iJwtServicePort, times(1)).generarToken("test@example.com", "CLIENTE", 1L);

    }

    @Test
    void testAuthenticateUserNotFoundException(){
        when(iUserPersistencePort.findByEmailUser("test@example.com")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, ()->{
            authUseCase.authenticate("test@example.com", "1234");
        });

        assertTrue(exception.getMessage().contains("Credenciales inválidas"));
        verify(iUserPersistencePort, times(1)).findByEmailUser("test@example.com");
        verifyNoInteractions(iPasswordEncodePort, iJwtServicePort);
    }

    @Test
    void testAuthenticateWrongPasswordException(){
        when(iUserPersistencePort.findByEmailUser("test@example.com")).thenReturn(testUser);
        when(iPasswordEncodePort.matches(anyString(), anyString())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, ()->{
            authUseCase.authenticate("test@example.com", "1234444");
        });

        assertTrue(exception.getMessage().contains("Credenciales inválidas"));
        verify(iUserPersistencePort, times(1)).findByEmailUser("test@example.com");
        verify(iPasswordEncodePort, times(1)).matches(anyString(), anyString());
        verifyNoInteractions(iJwtServicePort);
    }
}