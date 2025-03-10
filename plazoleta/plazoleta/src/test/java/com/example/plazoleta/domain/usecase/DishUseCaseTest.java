package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.exception.DishException;
import com.example.plazoleta.domain.exception.DishExceptionType;
import com.example.plazoleta.domain.model.Dish;
import com.example.plazoleta.domain.model.Restaurant;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IDishPersistencePort;
import com.example.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort iDishPersistencePort;

    @Mock
    private IRestaurantPersistencePort iRestaurantPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Restaurant testRestaurant;
    private Dish testDish;
    private User testPropietario;

    @BeforeEach
    void setUp() {
        testPropietario = new User(1L, "propietario@example.com", "PROPIETARIO",
                "testpropietario", "test", "+57123456789");

        testRestaurant = new Restaurant();
        testRestaurant.setId(1L);
        testRestaurant.setId_proprietary(testPropietario.getId());

        testDish = new Dish();
        testDish.setId(1L);
        testDish.setName("Pizza");
        testDish.setPrice(100);
        testDish.setRestaurant(testRestaurant);
        testDish.setActive(true);
        testDish.setCategory("Premium");
        testDish.setDescription("Es una pizza hawaiana");
        testDish.setUrl_image("htttp://mi_imagen/pizza");

    }

    @Test
    void testSaveDishSuccess(){
        when(iRestaurantPersistencePort.findById(testRestaurant.getId())).thenReturn(testRestaurant);
        when(iDishPersistencePort.saveDish(testDish)).thenReturn(testDish);

        Dish dish = dishUseCase.saveDish(testDish);

        assertThat(dish).isNotNull();
        assertNotNull(dish);
        assertEquals(dish.getRestaurant(), testRestaurant);
        verify(iRestaurantPersistencePort, times(1)).findById(testRestaurant.getId());
        verify(iDishPersistencePort, times(1)).saveDish(testDish);
    }

    @Test
    void testSaveDishInvalidRoleException(){
        DishException exception = assertThrows(DishException.class, () ->{
            dishUseCase.saveDish(testDish);
        });

        assertEquals(DishExceptionType.INVALID_ROL_CREATED_DISH, exception.getDishType());
        verify(iDishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testSaveDishNotExistsRestaurantException(){
        when(iRestaurantPersistencePort.findById(testRestaurant.getId())).thenReturn(null);

        DishException exception = assertThrows(DishException.class, ()->{
            dishUseCase.saveDish(testDish);
        });

        assertEquals(DishExceptionType.NOT_EXISTS_RESTAURANT, exception.getDishType());
        verify(iDishPersistencePort, never()).saveDish(any());
    }

    @Test
    void testUpdateDishSuccess(){
        Dish testUpdatedish = new Dish();
        testUpdatedish.setName("Pizza");
        testUpdatedish.setPrice(150);
        testUpdatedish.setRestaurant(testRestaurant);
        testUpdatedish.setActive(true);

        Long id_dish = 1L;

        when(iDishPersistencePort.findById(testDish.getId())).thenReturn(testDish);
        when(iDishPersistencePort.updateDish(any(Dish.class))).thenReturn(testUpdatedish);

        Dish dish = dishUseCase.updateDish(id_dish, testUpdatedish);

        assertNotNull(dish);
        assertEquals(dish.getPrice(), 150);
        verify(iDishPersistencePort, times(1)).findById(testDish.getId());
        verify(iDishPersistencePort, times(1)).updateDish(dish);

    }

    @Test
    void testUpdateDishInvalidRolException(){
        Long id_dish = 1L;

        DishException exception = assertThrows(DishException.class, ()->{
            dishUseCase.updateDish(id_dish, testDish);
        });

        assertEquals(DishExceptionType.INVALID_ROL_UPDATE_DISH, exception.getDishType());
        verify(iDishPersistencePort, never()).updateDish(any());
    }

    @Test
    void testUpdateDishNotExistsDishException(){
        Long id_dish = 1L;
        when(iDishPersistencePort.findById(id_dish)).thenReturn(null);

        DishException exception = assertThrows(DishException.class, ()->{
            dishUseCase.updateDish(id_dish, testDish);
        });

        assertEquals(DishExceptionType.NOT_EXISTS_DISH, exception.getDishType());
        verify(iDishPersistencePort, never()).updateDish(any());
    }

    @Test
    void testGetAllDishSuccess(){
        when(iDishPersistencePort.getAllDish()).thenReturn(List.of(testDish));

        List<Dish> listDish = dishUseCase.getAllDish();

        assertNotNull(listDish);
        assertEquals(listDish.size(), 1);
        assertEquals(listDish.get(0).getName(), "Pizza");
    }

    @Test
    void testUpdateStatusDishSuccess(){
        when(iDishPersistencePort.findById(testDish.getId())).thenReturn(testDish);
        when(iDishPersistencePort.updateDishStatus(testDish.getId())).thenReturn(testDish);

        Dish updateDish = dishUseCase.updateDishStatus(testDish.getId());

        assertNotNull(updateDish);
        assertThat(updateDish.getActive()).isFalse();

    }

    @Test
    void testUpdateStatusDishNotExistsDishException(){
        when(iDishPersistencePort.findById(testDish.getId())).thenReturn(null);

        DishException exception = assertThrows(DishException.class, ()->{
            dishUseCase.updateDishStatus(testDish.getId());
        });

        assertEquals(DishExceptionType.NOT_EXISTS_DISH, exception.getDishType());
        verify(iDishPersistencePort, never()).updateDishStatus(any());
    }
}