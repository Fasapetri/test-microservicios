package com.example.plazoleta.application;

import com.example.plazoleta.domain.Dish;
import com.example.plazoleta.domain.Plazoleta;
import com.example.plazoleta.infraestructure.dto.DishUpdateRequest;
import com.example.plazoleta.infraestructure.repository.DishRepository;
import com.example.plazoleta.infraestructure.user.AuthClient;
import com.example.plazoleta.infraestructure.user.UserClient;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static java.lang.Long.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private DishService dishService;

    @InjectMocks
    private AuthServiceUser authServiceUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateDish() {
        Long id_dish = 1L;
        Long userId = 1L;
        String email = "test@example.com";
        String role = "PROPIETARIO";

        Dish existingDish = Dish.builder()
                .id(id_dish)
                .name("Plato original")
                .price(1500)
                .description("Descripción original")
                .url_image("http://imagen.com")
                .active(true)
                .plazoleta(Plazoleta.builder().id(1L).id_proprietary(userId).build())
                .build();

        DishUpdateRequest dishUpdateRequest = DishUpdateRequest.builder()
                .price(2000)
                .description("Descripcion modificada")
                .build();

        String secret = System.getenv("JWT_SECRET");
        if (secret == null) {
            secret = "N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9";
        }

        SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

        String token = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        when(authServiceUser.getUserRoleFromToken(token)).thenReturn(
                new AuthClient.AuthenticateUser(userId, email, role)
        );

        when(dishRepository.save(any(Dish.class))).thenAnswer(invocationOnMock -> {
            return invocationOnMock.getArgument(0);
        });

        when(dishRepository.findById(id_dish)).thenReturn(Optional.of(existingDish));

        when(authClient.validateToken(token)).thenReturn(Map.of(
                "userId", Long.valueOf(1),
                "email", "test@example.com",
                "role", "PROPIETARIO"
        ));



        Dish updateDish = dishService.updateDish(id_dish, dishUpdateRequest, token);


        assertNotNull(updateDish);
        assertEquals(2000, updateDish.getPrice());
        assertEquals("Descripcion modificada", updateDish.getDescription());
        assertEquals("Plato original", updateDish.getName());

        verify(dishRepository, times(1)).findById(id_dish);
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    void testUpdateDish_UserNotOwner() {
        Long dishId = 1L;
        Dish existingDish = Dish.builder()
                .id(dishId)
                .name("Plato original")
                .price(1500)
                .description("Descripción original")
                .url_image("http://imagen.com")
                .active(true)
                .plazoleta(Plazoleta.builder().id(1L).id_proprietary(1L).build())
                .build();

        DishUpdateRequest updateRequest = DishUpdateRequest.builder()
                .price(2000)
                .description("Nueva descripción")
                .build();

        SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode("N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9"));
        Long userId = 3L;
        String email = "test2@example.com";
        String role = "PROPIETARIO";

        String token = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        when(authClient.validateToken(token)).thenReturn(Map.of(
                "userId", 1L,
                "email", "test@example.com",
                "role", "PROPIETARIO"
        ));

        when(authServiceUser.getUserRoleFromToken(token)).thenReturn(
                new AuthClient.AuthenticateUser(userId, email, role)
        );

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(existingDish));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dishService.updateDish(dishId, updateRequest, token);
        });

        assertEquals("El restaurante no pertenece al propietario", exception.getMessage());
        verify(dishRepository, times(1)).findById(dishId);
        verify(dishRepository, never()).save(any(Dish.class));
    }

    @Test
    void testUpdateDish_NotFound() {
        // Datos de prueba
        Long dishId = 1L;
        SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode("N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9N4pL3X9qkU8rMw4JfK2xvZ5pQ1rY6wT9"));
        Long userId = 3L;
        String email = "test2@example.com";
        String role = "PROPIETARIO";

        String token = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();

        DishUpdateRequest updateRequest = DishUpdateRequest.builder()
                .price(2000)
                .description("Nueva descripción")
                .build();

        when(authClient.validateToken(token)).thenReturn(Map.of(
                "userId", 1L,
                "email", "test@example.com",
                "role", "PROPIETARIO"
        ));

        when(authServiceUser.getUserRoleFromToken(token)).thenReturn(
                new AuthClient.AuthenticateUser(userId, email, role)
        );
        // Mock del repositorio
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        // Verificar excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dishService.updateDish(dishId, updateRequest, token);
        });

        // Validar mensaje de error
        assertEquals("Plato no encontrado.", exception.getMessage());
        verify(dishRepository, times(1)).findById(dishId);
        verify(dishRepository, never()).save(any(Dish.class));
    }
}