package com.example.plazoleta.domain.validations;

import com.example.plazoleta.domain.exception.PedidoException;
import com.example.plazoleta.domain.exception.PedidoExceptionType;
import com.example.plazoleta.domain.model.*;
import com.example.plazoleta.domain.spi.IEmpleadoRestaurantPersistencePort;
import com.example.plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.plazoleta.domain.spi.ISecurityContextPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PedidoUseCaseValidationTest {

    @Mock
    private IEmpleadoRestaurantPersistencePort empleadoRestaurantPersistencePort;

    @Mock
    private IPedidoPersistencePort pedidoPersistencePort;

    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private PedidoUseCaseValidation pedidoUseCaseValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validarItemsPedidoDeberiaLanzarExcepcionCuandoListaEsNula() {
        assertThatThrownBy(() -> pedidoUseCaseValidation.validarItemsPedido(null))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PEDIDO_NOT_ITEMS.getMessage());
    }

    @Test
    void validationItemDishToPedidoDeberiaLanzarExcepcionCuandoPlatoNoEstaEnElRestaurante() {
        Dish dish = new Dish(1L, "Plato 1", 5000, "plato",
                "http://imagen.com", "Sopa", true, new Restaurant());
        ItemPedido itemPedido = new ItemPedido(new Pedido(), dish, 2);
        List<Dish> dishsRestaurant = Collections.emptyList();

        PedidoException exception = assertThrows(PedidoException.class, ()->{
            pedidoUseCaseValidation.validationItemDishToPedido(dishsRestaurant, itemPedido);
        });

        assertThat(exception.getMessage())
                .contains(PedidoExceptionType.PLATO_NOT_AVAILABLE.getMessage())
                .contains("ID: 1");
    }

    @Test
    void validationItemDishToPedidoDeberiaLanzarExcepcionCuandoPlatoEstaDesactivado() {
        Dish dish = new Dish(1L, "Plato 1", 5000, "plato",
                "http://imagen.com", "Sopa", false, new Restaurant());
        List<Dish> restaurantDishes = List.of(dish);
        ItemPedido itemPedido = new ItemPedido(new Pedido(), dish, 2);

        PedidoException exception = assertThrows(PedidoException.class, ()->{
            pedidoUseCaseValidation.validationItemDishToPedido(restaurantDishes, itemPedido);
        });

        assertThat(exception.getMessage())
                .contains(PedidoExceptionType.PLATO_NOT_ACTIVE.getMessage());
    }

    @Test
    void validationItemDishToPedidoDeberiaRetornarItemPedidoCuandoPlatoEstaDisponibleYActivo() {
        Dish dish = new Dish(1L, "Plato 1", 5000, "plato",
                "http://imagen.com", "Sopa", true, new Restaurant());
        List<Dish> restaurantDishes = List.of(dish);
        ItemPedido itemPedido = new ItemPedido(new Pedido(), dish, 2);

        ItemPedido result = pedidoUseCaseValidation.validationItemDishToPedido(
                restaurantDishes, itemPedido);

        assertThat(result).isNotNull();
        assertThat(result.getPlato()).isEqualTo(dish);
    }

    @Test
    void validateListDishResturantDeberiaLanzarExcepcionCuandoListaEsNula() {
        assertThatThrownBy(() -> pedidoUseCaseValidation.validateListDishResturant(
                null))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.DISHS_NOT_DATA_TO_RESTAURANT.getMessage());
    }

    @Test
    void validateListDishResturantNoDebeLanzarExcepcionCuandoListaTieneElementos() {
        Dish dish = new Dish(1L, "Plato 1", 5000, "plato",
                "http://imagen.com", "Sopa", true, new Restaurant());
        List<Dish> restaurantDishes = List.of(dish);

        assertDoesNotThrow(() -> pedidoUseCaseValidation.validateListDishResturant(restaurantDishes));
    }

    @Test
    void validateListDishAvailableIsEmptyDeberiaLanzarExcepcionCuandoListaEstaVacia() {
        List<ItemPedido> listVacia = Collections.emptyList();

        PedidoException exception = assertThrows(PedidoException.class, ()->{
            pedidoUseCaseValidation.validateListDishAvailableIsEmpty(listVacia);
        });

        assertThat(exception.getMessage()).contains(PedidoExceptionType.PEDIDO_NOT_ITEMS.getMessage());
    }

    @Test
    void validateListDishAvailableIsEmptyNoDebeLanzarExcepcionCuandoListaTieneElementos() {
        ItemPedido itemPedido = new ItemPedido(new Pedido(), new Dish(), 2);
        List<ItemPedido> dishAvailableToPedido = List.of(itemPedido);

        assertDoesNotThrow(() -> pedidoUseCaseValidation.validateListDishAvailableIsEmpty(dishAvailableToPedido));
    }

    @Test
    void validationUserAuthenticatedBelongsRestaurantDeberiaLanzarExcepcionCuandoUsuarioNoPerteneceAlRestaurante() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 100L;

        when(empleadoRestaurantPersistencePort.existsByUserIdAndRestaurantId(
                userAuthenticatedId, restaurantId))
                .thenReturn(false);

        assertThatThrownBy(() -> pedidoUseCaseValidation.validationUserAuthenticatedBelongsRestaurant(
                userAuthenticatedId, restaurantId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.EMPLEADO_DOES_NOT_BELONGS_TO_RESTAURANT.getMessage());

        verify(empleadoRestaurantPersistencePort, times(1)).existsByUserIdAndRestaurantId(userAuthenticatedId, restaurantId);
    }

    @Test
    void validationUserAuthenticatedBelongsRestaurantNoDebeLanzarExcepcionCuandoUsuarioSiPerteneceAlRestaurante() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 100L;

        when(empleadoRestaurantPersistencePort.existsByUserIdAndRestaurantId(
                userAuthenticatedId, restaurantId))
                .thenReturn(true);

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationUserAuthenticatedBelongsRestaurant(userAuthenticatedId, restaurantId));

        verify(empleadoRestaurantPersistencePort, times(1))
                .existsByUserIdAndRestaurantId(userAuthenticatedId, restaurantId);
    }

    @Test
    void validationPedidoEmpleadoAsignadoDeberiaLanzarExcepcionCuandoPedidoYaEstaAsignadoAOtroEmpleado() {
        Long empleadoAsignadoId = 2L;
        Long userAuthenticatedId = 1L;
        Pedido pedido = new Pedido();
        pedido.setEmpleadoId(empleadoAsignadoId);

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationPedidoEmpleadoAsignado(pedido, userAuthenticatedId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.ORDER_ALREADY_ASSIGNED_TO_EMPLOYEE.getMessage());
    }

    @Test
    void validationPedidoEmpleadoAsignadoNoDebeLanzarExcepcionCuandoPedidoNoTieneEmpleadoAsignado() {
        Long userAuthenticatedId = 1L;
        Pedido pedido = new Pedido();
        pedido.setEmpleadoId(null);

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationPedidoEmpleadoAsignado(pedido, userAuthenticatedId));
    }

    @Test
    void validationPedidoEmpleadoAsignadoNoDebeLanzarExcepcionCuandoPedidoYaEstaAsignadoAlMismoEmpleado() {
        Long userAuthenticatedId = 1L;
        Pedido pedido = new Pedido();
        pedido.setEmpleadoId(userAuthenticatedId);

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationPedidoEmpleadoAsignado(pedido, userAuthenticatedId));
    }

    @Test
    void createTrazabilidadToUpdateStatusPedidoDeberiaCrearTrazabilidadCorrectamente() {
        Long pedidoId = 100L;
        Long clienteId = 200L;
        Long empleadoId = 300L;
        EstadoPedido estadoAnterior = EstadoPedido.EN_PREPARACION;
        EstadoPedido estadoNuevo = EstadoPedido.LISTO;

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setClienteId(clienteId);
        pedido.setEstado(estadoNuevo);

        TrazabilidadPedido trazabilidad = pedidoUseCaseValidation
                .createTrazabilidadToUpdateStatusPedido(
                pedido, empleadoId, estadoAnterior);

        assertThat(trazabilidad).isNotNull();
        assertThat(trazabilidad.getPedidoId()).isEqualTo(pedidoId);
        assertThat(trazabilidad.getClienteId()).isEqualTo(clienteId);
        assertThat(trazabilidad.getEmpleadoId()).isEqualTo(empleadoId);
        assertThat(trazabilidad.getEstadoAnterior()).isEqualTo(estadoAnterior);
        assertThat(trazabilidad.getEstadoNuevo()).isEqualTo(estadoNuevo);
        assertThat(trazabilidad.getFechaCambio()).isNotNull();
        assertThat(trazabilidad.getFechaCambio()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void validationStatusPedidoActualPendienteDeberiaLanzarExcepcionCuandoEstadoNoEsPendiente() {
        EstadoPedido estadoDiferenteDePendiente = EstadoPedido.EN_PREPARACION;

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualPendiente(estadoDiferenteDePendiente))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType
                        .ONLY_PENDING_ORDERS_CAN_BE_UPDATED_TO_PREPARATION.getMessage());
    }

    @Test
    void validationStatusPedidoActualPendienteNoDebeLanzarExcepcionCuandoEstadoEsPendiente() {
        EstadoPedido estadoPendiente = EstadoPedido.PENDIENTE;

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualPendiente(estadoPendiente));
    }

    @Test
    void validationStatusPedidoActualEnPreparacionDeberiaLanzarExcepcionCuandoEstadoNoEsEnPreparacion() {
        EstadoPedido estadoDiferenteDeEnPreparacion = EstadoPedido.PENDIENTE;

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualEnPreparacion(estadoDiferenteDeEnPreparacion))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType
                        .ONLY_PREPARATION_ORDERS_CAN_BE_UPDATED_TO_LISTO.getMessage());
    }

    @Test
    void validationStatusPedidoActualEnPreparacionNoDebeLanzarExcepcionCuandoEstadoEsEnPreparacion() {
        EstadoPedido estadoEnPreparacion = EstadoPedido.EN_PREPARACION;

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualEnPreparacion(estadoEnPreparacion));
    }

    @Test
    void validationStatusPedidoActualEnListoDeberiaLanzarExcepcionCuandoEstadoNoEsListo() {
        EstadoPedido estadoDiferenteDeListo = EstadoPedido.EN_PREPARACION;

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualEnListo(estadoDiferenteDeListo))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType
                        .ONLY_LISTO_ORDERS_CAN_BE_UPDATED_TO_ENTREGADO.getMessage());
    }

    @Test
    void validationStatusPedidoActualEnListoNoDebeLanzarExcepcionCuandoEstadoEsListo() {
        EstadoPedido estadoListo = EstadoPedido.LISTO;

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualEnListo(estadoListo));
    }

    @Test
    void validationPinSecurityPedidoDeberiaLanzarExcepcionCuandoPinEsIncorrecto() {
        Pedido pedido = new Pedido();
        pedido.setPinSeguridad("1234");

        String pinIngresadoIncorrecto = "5678";

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationPinSecurityPedido(pedido, pinIngresadoIncorrecto))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PIN_SEGURITY_INCORRECT.getMessage());
    }

    @Test
    void validationPinSecurityPedidoNoDebeLanzarExcepcionCuandoPinEsCorrecto() {
        Pedido pedido = new Pedido();
        pedido.setPinSeguridad("1234");

        String pinIngresadoCorrecto = "1234";

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationPinSecurityPedido(pedido, pinIngresadoCorrecto));
    }

    @Test
    void validationUserAuthenticatedBelongsClientePedidoDeberiaLanzarExcepcionCuandoUsuarioNoEsElClienteDelPedido() {
        Long userAuthenticatedId = 1L;
        Pedido pedido = new Pedido();
        pedido.setClienteId(2L);

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PEDIDO_NOT_OWNER_ERROR.getMessage());
    }

    @Test
    void validationUserAuthenticatedBelongsClientePedidoNoDebeLanzarExcepcionCuandoUsuarioEsElClienteDelPedido() {
        Long userAuthenticatedId = 1L;
        Pedido pedido = new Pedido();
        pedido.setClienteId(userAuthenticatedId);

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido));
    }

    @Test
    void validationStatusPedidoActualPendienteToCanceledDeberiaLanzarExcepcionCuandoEstadoEsCancelado() {
        EstadoPedido estadoCancelado = EstadoPedido.CANCELADO;

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualPendienteToCanceled(estadoCancelado))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PEDIDO_ENTREGADO_NOT_UPDATE.getMessage());
    }

    @Test
    void validationStatusPedidoActualPendienteToCanceledDeberiaLanzarExcepcionCuandoEstadoNoEsPendienteNiCancelado() {
        EstadoPedido estadoEnPreparacion = EstadoPedido.EN_PREPARACION;

        assertThatThrownBy(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualPendienteToCanceled(estadoEnPreparacion))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PEDIDO_IN_PREPARACION_NOT_CANCELED.getMessage());
    }

    @Test
    void validationStatusPedidoActualPendienteToCanceledNoDebeLanzarExcepcionCuandoEstadoEsPendiente() {
        EstadoPedido estadoPendiente = EstadoPedido.PENDIENTE;

        assertDoesNotThrow(() -> pedidoUseCaseValidation
                .validationStatusPedidoActualPendienteToCanceled(estadoPendiente));
    }

    @Test
    void validarYObtenerPedidoDeberiaLanzarExcepcionCuandoPedidoNoExiste() {
        Long pedidoId = 1L;

        when(pedidoPersistencePort.findByIdPedido(pedidoId)).thenReturn(null);

        assertThatThrownBy(() -> pedidoUseCaseValidation.validarYObtenerPedido(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO.getMessage());

        verify(pedidoPersistencePort, times(1)).findByIdPedido(pedidoId);
        verifyNoInteractions(empleadoRestaurantPersistencePort);
    }

    @Test
    void validarYObtenerPedidoDeberiaLanzarExcepcionCuandoUsuarioNoPerteneceAlRestaurante() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        Long restaurantId = 200L;
        Pedido pedido = new Pedido();
        pedido.setRestaurant(new Restaurant());
        pedido.getRestaurant().setId(restaurantId);

        when(pedidoPersistencePort.findByIdPedido(pedidoId)).thenReturn(pedido);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);

        doThrow(new PedidoException(PedidoExceptionType.EMPLEADO_DOES_NOT_BELONGS_TO_RESTAURANT))
                .when(empleadoRestaurantPersistencePort)
                .existsByUserIdAndRestaurantId(userAuthenticatedId, restaurantId);

        assertThatThrownBy(() -> pedidoUseCaseValidation.validarYObtenerPedido(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.EMPLEADO_DOES_NOT_BELONGS_TO_RESTAURANT.getMessage());
    }

    @Test
    void validarYObtenerPedidoDeberiaRetornarPedidoCuandoTodoEsValido() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        Long restaurantId = 200L;
        Pedido pedido = new Pedido();
        pedido.setRestaurant(new Restaurant());
        pedido.getRestaurant().setId(restaurantId);

        when(pedidoPersistencePort.findByIdPedido(pedidoId)).thenReturn(pedido);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(empleadoRestaurantPersistencePort.existsByUserIdAndRestaurantId(userAuthenticatedId, restaurantId))
                .thenReturn(true);

        Pedido resultado = pedidoUseCaseValidation.validarYObtenerPedido(pedidoId);

        assertThat(resultado).isNotNull()
                .isEqualTo(pedido);
    }
}