package com.example.plazoleta.application;

import com.example.plazoleta.domain.Plazoleta;
import com.example.plazoleta.domain.PlazoletaPort;
import com.example.plazoleta.infraestructure.user.AuthClient;
import com.example.plazoleta.infraestructure.user.UserClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlazoletaServiceTest {

    @Mock
    private PlazoletaPort plazoletaPort;

    @Mock
    private UserClient userClient;


    @InjectMocks
    private PlazoletaService plazoletaService;

    @Mock
    private AuthClient authClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPlazoleta() {
        Plazoleta plazoleta = Plazoleta.builder()
                .nit("12134567")
                .phone("+573115211945")
                .address("Av 6 calle 15-78A")
                .url_Logo("C:/Users/karen/OneDrive/Imágenes")
                .name("cocina oculta 2.0")
                .id_proprietary(1L)
                .build();

        SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode("N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9"));
        Long userId = 1L;
        String email = "test@example.com";
        String role = "PROPIETARIO";

        String token = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();



        when(userClient.getUserByIdProprietary(userId, "Bearer " + token)).thenReturn(
                new UserClient.UserResponse(userId, email, role)
        );

        when(authClient.validateToken(token)).thenReturn(Map.of(
                "userId", 1L,
                "email", "test@example.com",
                "role", "PROPIETARIO"
        ));

        when(plazoletaPort.save(any(Plazoleta.class))).thenAnswer(invocationOnMock -> {
            Plazoleta plazoletaArg = invocationOnMock.getArgument(0);
            plazoletaArg.setId(1L);
            return plazoletaArg;
        });



        Plazoleta test = plazoletaService.addPlazoleta(plazoleta, token);

        assertNotNull(test);
        assertEquals("12134567", test.getNit());
        verify(plazoletaPort, times(1)).save(any(Plazoleta.class));
        verify(userClient, times(1)).getUserByIdProprietary(userId, token);
    }
}