package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.exception.RestaurantException;
import com.example.plazoleta.domain.exception.RestaurantExceptionType;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.IUserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort iRestaurantPersistencePort;

    @Mock
    private IUserClientPort iUserClientPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant testRestaurant;
    private User testUserAdmin;
    private User testUserPropietario;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setId(1L);
        testRestaurant.setName("Restaurante Prueba");
        testRestaurant.setNit("1234567890");
        testRestaurant.setAddress("Calle Falsa 123");
        testRestaurant.setPhone("+573001234567");
        testRestaurant.setUrl_logo("http://example.com/logo.png");
        testRestaurant.setId_proprietary(2L);

        testUserAdmin = new User(1L, "admin@example.com", "ADMIN", "test", "test",
                "+57123456789");
        testUserPropietario = new User(2L, "owner@example.com", "PROPIETARIO", "testpropietario",
                "test", "+57123456789");

    }

    @Test
    void testSaveRestaurantSuccess(){
        when(iUserClientPort.getUserById(testRestaurant.getId_proprietary())).thenReturn(testUserPropietario);
        when(iRestaurantPersistencePort.existsByNit(testRestaurant.getNit())).thenReturn(false);
        when(iRestaurantPersistencePort.saveRestaurant(testRestaurant)).thenReturn(testRestaurant);

        Restaurant savedRestaurant = restaurantUseCase.saveRestaurant(testRestaurant);

        assertNotNull(savedRestaurant);
        assertEquals("1234567890", savedRestaurant.getNit());
        verify(iRestaurantPersistencePort, times(1)).saveRestaurant(testRestaurant);
    }

    @Test
    void testSaveRestaurantInvalidRolException(){
        RestaurantException exception = assertThrows(RestaurantException.class, () -> {
            restaurantUseCase.saveRestaurant(testRestaurant);
        });

        assertEquals(RestaurantExceptionType.INVALID_ROL_CREATED_RESTAURANT, exception.getRestaurantType());
        verify(iRestaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    void testSaveRestaurantInvalidPropietarioException(){
        User testUserNotPropietario = new User(4L, "test4@example.com", "CLIENTE", "testcliente",
                "test", "+57123456789");

        when(iUserClientPort.getUserById(testRestaurant.getId_proprietary())).thenReturn(testUserNotPropietario);

        RestaurantException exception = assertThrows(RestaurantException.class, () ->{
            restaurantUseCase.saveRestaurant(testRestaurant);
        });

        assertEquals(RestaurantExceptionType.INVALID_ROL_PROPIETARIO, exception.getRestaurantType());
        verify(iRestaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    void testSaveRestaurantInvalidNitExistsException(){
        when(iUserClientPort.getUserById(testRestaurant.getId_proprietary())).thenReturn(testUserPropietario);
        when(iRestaurantPersistencePort.existsByNit(testRestaurant.getNit())).thenReturn(true);

        RestaurantException exception =  assertThrows(RestaurantException.class, ()->{
            restaurantUseCase.saveRestaurant(testRestaurant);
        });

        assertEquals(RestaurantExceptionType.NIT_RESTAURANT_ALREADY_EXISTS, exception.getRestaurantType());
        verify(iRestaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    void testFindAllrestaurantSuccess(){

        Restaurant testRestaurant2 = new Restaurant();
        testRestaurant2.setId(2L);
        testRestaurant2.setName("Restaurante Prueba 2");
        testRestaurant2.setNit("12345678852");
        testRestaurant2.setAddress("Calle Falsa 1234");
        testRestaurant2.setPhone("+573001234570");
        testRestaurant2.setUrl_logo("http://example.com/logo2.png");
        testRestaurant2.setId_proprietary(2L);

        List<Restaurant> listRestaurant = List.of(testRestaurant, testRestaurant2);

        when(iRestaurantPersistencePort.getAllrestaurant()).thenReturn(listRestaurant);

        List<Restaurant> result = restaurantUseCase.getAllrestaurant();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(iRestaurantPersistencePort, times(1)).getAllrestaurant();
    }

}