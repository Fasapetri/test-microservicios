package com.example.plazoleta.domain.usecase;

import com.example.plazoleta.domain.constants.PedidoUseCaseConstants;
import com.example.plazoleta.domain.exception.PedidoException;
import com.example.plazoleta.domain.exception.PedidoExceptionType;
import com.example.plazoleta.domain.model.*;
import com.example.plazoleta.domain.spi.*;
import com.example.plazoleta.domain.validations.PedidoUseCaseValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PedidoUseCaseTest {

    @Mock
    private IPedidoPersistencePort pedidoPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private ISecurityContextPort securityContextPort;

    @Mock
    private PedidoUseCaseValidation pedidoUseCaseValidation;

    @Mock
    private IItemPedidoPersistencePort itemPedidoPersistencePort;

    @Mock
    private ITrazabilidadPersistencePort trazabilidadPersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @Mock
    private IMensajeriaSmsPersistencePort smsPersistencePort;


    private PedidoUseCase pedidoUseCase;

    @Spy
    private Random randomSpy = new Random();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pedidoUseCase = spy(new PedidoUseCase(
                restaurantPersistencePort, pedidoPersistencePort, securityContextPort,
                dishPersistencePort, pedidoUseCaseValidation, itemPedidoPersistencePort,
                trazabilidadPersistencePort, userClientPort, smsPersistencePort));

    }

    @Test
    void savePedidoDeberiaLanzarExcepcionCuandoRestauranteNoExiste() {
        Pedido pedido = new Pedido();
        pedido.setRestaurant(new Restaurant());
        pedido.getRestaurant().setId(1L);

        when(restaurantPersistencePort.existsRestaurant(1L)).thenReturn(false);

        assertThatThrownBy(() -> pedidoUseCase.savePedido(pedido))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.RESTAURANT_NOT_EXISTS.getMessage());
    }

    @Test
    void savePedidoDeberiaLanzarExcepcionCuandoClienteTienePedidoPendiente() {
        Pedido pedido = new Pedido();
        pedido.setRestaurant(new Restaurant());
        pedido.getRestaurant().setId(1L);

        Long userId = 10L;
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userId);
        when(restaurantPersistencePort.existsRestaurant(1L)).thenReturn(true);
        when(restaurantPersistencePort.findById(1L)).thenReturn(pedido.getRestaurant());
        when(pedidoPersistencePort.findByClienteIdAndEstadoIn(eq(userId), any()))
                .thenReturn(new Pedido());

        assertThatThrownBy(() -> pedidoUseCase.savePedido(pedido))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.CLIENT_STATUS_PEDIDO_IN.getMessage());
    }

    @Test
    void savePedidoDeberiaLanzarExcepcionCuandoListaDePlatosRestauranteEstaVacia() {
        Pedido pedido = new Pedido();
        pedido.setRestaurant(new Restaurant());
        pedido.getRestaurant().setId(1L);

        when(restaurantPersistencePort.existsRestaurant(1L)).thenReturn(true);
        when(restaurantPersistencePort.findById(1L)).thenReturn(pedido.getRestaurant());
        when(pedidoPersistencePort.findByClienteIdAndEstadoIn(anyLong(), any())).thenReturn(null);
        when(dishPersistencePort.getDishRestaurant(1L)).thenReturn(Collections.emptyList());

        doThrow(new PedidoException(PedidoExceptionType.DISHS_NOT_DATA_TO_RESTAURANT))
                .when(pedidoUseCaseValidation).validateListDishResturant(anyList());

        assertThatThrownBy(() -> pedidoUseCase.savePedido(pedido))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.DISHS_NOT_DATA_TO_RESTAURANT.getMessage());
    }

    @Test
    void savePedidoDeberiaLanzarExcepcionCuandoListaDeItemsPedidoEstaVacia() {
        Pedido pedido = new Pedido();
        pedido.setRestaurant(new Restaurant());
        pedido.getRestaurant().setId(1L);
        pedido.setItems(Collections.emptyList());

        when(restaurantPersistencePort.existsRestaurant(1L)).thenReturn(true);
        when(restaurantPersistencePort.findById(1L)).thenReturn(pedido.getRestaurant());
        when(pedidoPersistencePort.findByClienteIdAndEstadoIn(anyLong(), any())).thenReturn(null);
        when(dishPersistencePort.getDishRestaurant(1L)).thenReturn(List.of(new Dish()));

        doThrow(new PedidoException(PedidoExceptionType.PEDIDO_NOT_ITEMS))
                .when(pedidoUseCaseValidation).validarItemsPedido(anyList());

        assertThatThrownBy(() -> pedidoUseCase.savePedido(pedido))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PEDIDO_NOT_ITEMS.getMessage());
    }

    @Test
    void savePedidoDeberiaGuardarPedidoCuandoTodosLosDatosSonValidos() {
        Pedido pedido = new Pedido();
        pedido.setRestaurant(new Restaurant());
        pedido.getRestaurant().setId(1L);
        pedido.setItems(new ArrayList<>());

        ItemPedido itemPedido = new ItemPedido(pedido, new Dish(), 2);
        pedido.getItems().add(itemPedido);

        when(restaurantPersistencePort.existsRestaurant(1L)).thenReturn(true);
        when(restaurantPersistencePort.findById(1L)).thenReturn(pedido.getRestaurant());
        when(pedidoPersistencePort.findByClienteIdAndEstadoIn(anyLong(), any())).thenReturn(null);
        when(dishPersistencePort.getDishRestaurant(1L)).thenReturn(List.of(new Dish()));

        when(pedidoPersistencePort.savePedido(any())).thenReturn(pedido);
        when(pedidoUseCaseValidation.validationItemDishToPedido(anyList(), any())).thenReturn(itemPedido);

        Pedido savedPedido = pedidoUseCase.savePedido(pedido);

        assertThat(savedPedido).isNotNull();
        verify(pedidoPersistencePort, times(1)).savePedido(pedido);
        verify(itemPedidoPersistencePort, times(1)).saveAll(anyList());
    }

    @Test
    void findByStatusPedidoDeberiaLanzarExcepcionCuandoUsuarioNoPerteneceAlRestaurante() {
        Long userId = 1L;
        Long restaurantId = 2L;
        EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userId);
        doThrow(new PedidoException(PedidoExceptionType.EMPLEADO_DOES_NOT_BELONGS_TO_RESTAURANT))
                .when(pedidoUseCaseValidation)
                .validationUserAuthenticatedBelongsRestaurant(userId, restaurantId);

        assertThatThrownBy(() -> pedidoUseCase.findByStatusPedido(estadoPedido, restaurantId, 1, 5))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.EMPLEADO_DOES_NOT_BELONGS_TO_RESTAURANT.getMessage());
    }

    @Test
    void findByStatusPedidoDeberiaLanzarExcepcionCuandoNoExistenPedidosConEstadoDado() {
        Long userId = 1L;
        Long restaurantId = 2L;
        EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;
        Pageable pageable = PageRequest.of(0, 5);
        Page<Pedido> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userId);
        doNothing().when(pedidoUseCaseValidation)
                .validationUserAuthenticatedBelongsRestaurant(userId, restaurantId);
        when(pedidoPersistencePort
                .findByEstadoAndRestaurantId(estadoPedido, restaurantId, pageable))
                .thenReturn(emptyPage);

        assertThatThrownBy(() -> pedidoUseCase.findByStatusPedido(estadoPedido, restaurantId, 1, 5))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.NOT_EXISTS_PEDIDOS_STATUS_IN_RESTAURANT.getMessage());
    }

    @Test
    void findByStatusPedidoDeberiaRetornarListaDePedidosCuandoExistenPedidos() {
        Long userId = 1L;
        Long restaurantId = 2L;
        EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;
        Pageable pageable = PageRequest.of(0, 5);

        Pedido pedido = new Pedido();
        pedido.setId(100L);
        pedido.setItems(Collections.emptyList());

        Page<Pedido> pedidoPage = new PageImpl<>(List.of(pedido), pageable, 1);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userId);
        doNothing().when(pedidoUseCaseValidation).validationUserAuthenticatedBelongsRestaurant(userId, restaurantId);
        when(pedidoPersistencePort.findByEstadoAndRestaurantId(estadoPedido, restaurantId, pageable)).thenReturn(pedidoPage);
        when(itemPedidoPersistencePort.findByPedidoId(pedido.getId())).thenReturn(Collections.emptyList());

        Page<Pedido> result = pedidoUseCase.findByStatusPedido(estadoPedido, restaurantId, 1, 5);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(100L);

        verify(pedidoPersistencePort, times(1)).findByEstadoAndRestaurantId(estadoPedido, restaurantId, pageable);
        verify(itemPedidoPersistencePort, times(1)).findByPedidoId(pedido.getId());
    }

    @Test
    void updateStatusPedidoEnPreparacionDeberiaLanzarExcepcionCuandoPedidoNoExiste() {
        Long pedidoId = 1L;

        doThrow(new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO))
                .when(pedidoUseCaseValidation).validarYObtenerPedido(pedidoId);

        assertThatThrownBy(() -> pedidoUseCase.updateStatusPedidoEnPreparacion(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO.getMessage());

        verify(pedidoUseCaseValidation, times(1))
                .validarYObtenerPedido(pedidoId);
        verifyNoInteractions(pedidoPersistencePort);
    }

    @Test
    void updateStatusPedidoEnPreparacionDeberiaLanzarExcepcionCuandoEstadoActualNoEsPendiente() {
        Long pedidoId = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        when(pedidoUseCaseValidation.validarYObtenerPedido(pedidoId)).thenReturn(pedido);
        doThrow(new PedidoException(PedidoExceptionType.ONLY_PENDING_ORDERS_CAN_BE_UPDATED_TO_PREPARATION))
                .when(pedidoUseCaseValidation).validationStatusPedidoActualPendiente(pedido.getEstado());

        assertThatThrownBy(() -> pedidoUseCase.updateStatusPedidoEnPreparacion(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType
                        .ONLY_PENDING_ORDERS_CAN_BE_UPDATED_TO_PREPARATION.getMessage());

        verify(pedidoUseCaseValidation, times(1))
                .validarYObtenerPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1))
                .validationStatusPedidoActualPendiente(pedido.getEstado());
    }

    @Test
    void updateStatusPedidoEnPreparacionDeberiaActualizarPedidoCuandoEsValido() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        EstadoPedido estadoAnterior = EstadoPedido.PENDIENTE;

        TrazabilidadPedido trazabilidad = new TrazabilidadPedido();
        trazabilidad.setPedidoId(pedidoId);
        trazabilidad.setEstadoAnterior(estadoAnterior);
        trazabilidad.setEstadoNuevo(EstadoPedido.EN_PREPARACION);
        trazabilidad.setFechaCambio(LocalDateTime.now());

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(pedidoUseCaseValidation.validarYObtenerPedido(pedidoId)).thenReturn(pedido);
        doNothing().when(pedidoUseCaseValidation).validationStatusPedidoActualPendiente(any(EstadoPedido.class));
        when(pedidoPersistencePort.updateStatusPedido(pedido)).thenReturn(pedido);
        when(pedidoUseCaseValidation.createTrazabilidadToUpdateStatusPedido(pedido, userAuthenticatedId, estadoAnterior))
                .thenReturn(trazabilidad);
        doNothing().when(trazabilidadPersistencePort).savedTrazabilidad(trazabilidad);

        String resultado = pedidoUseCase.updateStatusPedidoEnPreparacion(pedidoId);

        assertThat(resultado)
                .isNotNull()
                .isEqualTo("El estado del pedido se ha actualizado exitosamentePENDIENTE a EN_PREPARACION");

        verify(pedidoUseCaseValidation, times(1)).validarYObtenerPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1)).validationStatusPedidoActualPendiente(any(EstadoPedido.class));
        verify(securityContextPort, times(1)).getAuthenticatedUserId();
        verify(pedidoPersistencePort, times(1)).updateStatusPedido(pedido);
        verify(trazabilidadPersistencePort, times(1)).savedTrazabilidad(trazabilidad);
    }

    @Test
    void updateStatusPedidoListoDeberiaLanzarExcepcionCuandoPedidoNoExiste() {
        Long pedidoId = 1L;

        doThrow(new PedidoException(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO))
                .when(pedidoUseCaseValidation).validarYObtenerPedido(pedidoId);

        assertThatThrownBy(() -> pedidoUseCase.updateStatusPedidoListo(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.FIND_NOT_EXISTS_PEDIDO.getMessage());

        verify(pedidoUseCaseValidation, times(1))
                .validarYObtenerPedido(pedidoId);
        verifyNoInteractions(userClientPort, smsPersistencePort, pedidoPersistencePort);
    }

    @Test
    void updateStatusPedidoListoDeberiaLanzarExcepcionCuandoEstadoActualNoEsEnPreparacion() {
        Long pedidoId = 1L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoUseCaseValidation.validarYObtenerPedido(pedidoId)).thenReturn(pedido);
        doThrow(new PedidoException(PedidoExceptionType
                .ONLY_PREPARATION_ORDERS_CAN_BE_UPDATED_TO_LISTO))
                .when(pedidoUseCaseValidation).validationStatusPedidoActualEnPreparacion(
                        pedido.getEstado());

        assertThatThrownBy(() -> pedidoUseCase.updateStatusPedidoListo(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType
                        .ONLY_PREPARATION_ORDERS_CAN_BE_UPDATED_TO_LISTO.getMessage());

        verify(pedidoUseCaseValidation, times(1))
                .validarYObtenerPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1))
                .validationStatusPedidoActualEnPreparacion(pedido.getEstado());
        verifyNoInteractions(userClientPort, smsPersistencePort, pedidoPersistencePort);
    }

    @Test
    void updateStatusPedidoListoDeberiaActualizarPedidoCuandoEsValido() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        pedido.setClienteId(200L);
        EstadoPedido estadoAnterior = EstadoPedido.EN_PREPARACION;

        User cliente = new User(200L, "test@example.com", "CLIENTE", "testcliente",
                "test", "+1234567890");

        String pinSeguridad = "1234";
        String mensajeEsperado = String.format(PedidoUseCaseConstants.ORDER_READY_WITH_PIN, pinSeguridad);

        TrazabilidadPedido trazabilidad = new TrazabilidadPedido();
        trazabilidad.setPedidoId(pedidoId);
        trazabilidad.setEstadoAnterior(estadoAnterior);
        trazabilidad.setEstadoNuevo(EstadoPedido.LISTO);
        trazabilidad.setFechaCambio(LocalDateTime.now());

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(pedidoUseCaseValidation.validarYObtenerPedido(pedidoId)).thenReturn(pedido);
        doNothing().when(pedidoUseCaseValidation).validationStatusPedidoActualEnPreparacion(any(EstadoPedido.class));
        when(userClientPort.getUserById(pedido.getClienteId())).thenReturn(cliente);
        doReturn(pinSeguridad).when(pedidoUseCase).generarPin();
        doNothing().when(smsPersistencePort).sendSms(new SmsMessage(cliente.getPhone(), mensajeEsperado));

        when(pedidoPersistencePort.updateStatusPedido(pedido)).thenReturn(pedido);
        when(pedidoUseCaseValidation.createTrazabilidadToUpdateStatusPedido(pedido, userAuthenticatedId, estadoAnterior))
                .thenReturn(trazabilidad);
        doNothing().when(trazabilidadPersistencePort).savedTrazabilidad(trazabilidad);

        String resultado = pedidoUseCase.updateStatusPedidoListo(pedidoId);

        assertThat(resultado)
                .isNotNull()
                .isEqualTo("El estado del pedido se ha actualizado exitosamenteEN_PREPARACION a LISTO");

        verify(pedidoUseCaseValidation, times(1)).validarYObtenerPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1)).validationStatusPedidoActualEnPreparacion(any(EstadoPedido.class));
        verify(securityContextPort, times(1)).getAuthenticatedUserId();
        verify(userClientPort, times(1)).getUserById(pedido.getClienteId());
        verify(smsPersistencePort, times(1)).sendSms(new SmsMessage(cliente.getPhone(), mensajeEsperado));
        verify(pedidoPersistencePort, times(1)).updateStatusPedido(pedido);
        verify(trazabilidadPersistencePort, times(1)).savedTrazabilidad(trazabilidad);
    }

    @Test
    void updateStatusPedidoEntregadoDeberiaLanzarExcepcionCuandoPinEsIncorrecto() {
        Long pedidoId = 1L;
        String pinIncorrecto = "9999";
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setEstado(EstadoPedido.LISTO);
        pedido.setPinSeguridad("1234");

        when(pedidoUseCaseValidation.validarYObtenerPedido(pedidoId)).thenReturn(pedido);
        doNothing().when(pedidoUseCaseValidation).validationStatusPedidoActualEnListo(pedido.getEstado());

        doThrow(new PedidoException(PedidoExceptionType.PIN_SEGURITY_INCORRECT))
                .when(pedidoUseCaseValidation).validationPinSecurityPedido(pedido, pinIncorrecto);

        assertThatThrownBy(() -> pedidoUseCase.updateStatusPedidoEntregado(pedidoId, pinIncorrecto))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PIN_SEGURITY_INCORRECT.getMessage());

        verify(pedidoUseCaseValidation, times(1))
                .validarYObtenerPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1))
                .validationStatusPedidoActualEnListo(pedido.getEstado());
        verify(pedidoUseCaseValidation, times(1))
                .validationPinSecurityPedido(pedido, pinIncorrecto);
        verifyNoInteractions(pedidoPersistencePort);
    }

    @Test
    void updateStatusPedidoEntregadoDeberiaActualizarPedidoCuandoEsValido() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        String pinCorrecto = "1234";
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setEstado(EstadoPedido.LISTO);
        pedido.setPinSeguridad(pinCorrecto);
        EstadoPedido estadoAnterior = EstadoPedido.LISTO;

        TrazabilidadPedido trazabilidad = new TrazabilidadPedido();
        trazabilidad.setPedidoId(pedidoId);
        trazabilidad.setEstadoAnterior(estadoAnterior);
        trazabilidad.setEstadoNuevo(EstadoPedido.ENTREGADO);
        trazabilidad.setFechaCambio(LocalDateTime.now());

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(pedidoUseCaseValidation.validarYObtenerPedido(pedidoId)).thenReturn(pedido);
        doNothing().when(pedidoUseCaseValidation).validationStatusPedidoActualEnListo(any(EstadoPedido.class));
        doNothing().when(pedidoUseCaseValidation).validationPinSecurityPedido(pedido, pinCorrecto);

        when(pedidoPersistencePort.updateStatusPedido(pedido)).thenReturn(pedido);
        when(pedidoUseCaseValidation.createTrazabilidadToUpdateStatusPedido(pedido, userAuthenticatedId, estadoAnterior))
                .thenReturn(trazabilidad);
        doNothing().when(trazabilidadPersistencePort).savedTrazabilidad(trazabilidad);

        String resultado = pedidoUseCase.updateStatusPedidoEntregado(pedidoId, pinCorrecto);

        assertThat(resultado)
                .isNotNull()
                .isEqualTo("El estado del pedido se ha actualizado exitosamenteLISTO a ENTREGADO");

        verify(pedidoUseCaseValidation, times(1)).validarYObtenerPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1)).validationStatusPedidoActualEnListo(any(EstadoPedido.class));
        verify(pedidoUseCaseValidation, times(1)).validationPinSecurityPedido(pedido, pinCorrecto);
        verify(securityContextPort, times(1)).getAuthenticatedUserId();
        verify(pedidoPersistencePort, times(1)).updateStatusPedido(pedido);
        verify(trazabilidadPersistencePort, times(1)).savedTrazabilidad(trazabilidad);
    }

    @Test
    void canceledPedidoDeberiaLanzarExcepcionCuandoUsuarioNoEsElDueñoDelPedido() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setClienteId(200L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(pedidoPersistencePort.findByIdPedido(pedidoId)).thenReturn(pedido);
        doThrow(new PedidoException(PedidoExceptionType.PEDIDO_NOT_OWNER_ERROR))
                .when(pedidoUseCaseValidation)
                .validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido);

        assertThatThrownBy(() -> pedidoUseCase.canceledPedido(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PEDIDO_NOT_OWNER_ERROR.getMessage());

        verify(pedidoPersistencePort, times(1)).findByIdPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1))
                .validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido);
        verifyNoMoreInteractions(pedidoUseCaseValidation);
    }

    @Test
    void canceledPedidoDeberiaLanzarExcepcionCuandoEstadoNoPermiteCancelacion() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setClienteId(userAuthenticatedId);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(pedidoPersistencePort.findByIdPedido(pedidoId)).thenReturn(pedido);
        doNothing().when(pedidoUseCaseValidation)
                .validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido);
        doThrow(new PedidoException(PedidoExceptionType.PEDIDO_IN_PREPARACION_NOT_CANCELED))
                .when(pedidoUseCaseValidation)
                .validationStatusPedidoActualPendienteToCanceled(pedido.getEstado());

        assertThatThrownBy(() -> pedidoUseCase.canceledPedido(pedidoId))
                .isInstanceOf(PedidoException.class)
                .hasMessage(PedidoExceptionType.PEDIDO_IN_PREPARACION_NOT_CANCELED.getMessage());

        verify(pedidoPersistencePort, times(1)).findByIdPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1))
                .validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido);
        verify(pedidoUseCaseValidation, times(1))
                .validationStatusPedidoActualPendienteToCanceled(pedido.getEstado());
    }

    @Test
    void canceledPedidoDeberiaActualizarPedidoCuandoEsValido() {
        Long pedidoId = 1L;
        Long userAuthenticatedId = 100L;
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setClienteId(userAuthenticatedId);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        EstadoPedido estadoAnterior = EstadoPedido.PENDIENTE;

        TrazabilidadPedido trazabilidad = new TrazabilidadPedido();
        trazabilidad.setPedidoId(pedidoId);
        trazabilidad.setEstadoAnterior(estadoAnterior);
        trazabilidad.setEstadoNuevo(EstadoPedido.CANCELADO);
        trazabilidad.setFechaCambio(LocalDateTime.now());

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(pedidoPersistencePort.findByIdPedido(pedidoId)).thenReturn(pedido);
        doNothing().when(pedidoUseCaseValidation).validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido);
        doNothing().when(pedidoUseCaseValidation).validationStatusPedidoActualPendienteToCanceled(any(EstadoPedido.class));

        when(pedidoPersistencePort.updateStatusPedido(pedido)).thenReturn(pedido);
        when(pedidoUseCaseValidation.createTrazabilidadToUpdateStatusPedido(pedido, userAuthenticatedId, estadoAnterior))
                .thenReturn(trazabilidad);
        doNothing().when(trazabilidadPersistencePort).savedTrazabilidad(trazabilidad);

        String resultado = pedidoUseCase.canceledPedido(pedidoId);

        assertThat(resultado)
                .isNotNull()
                .isEqualTo("El estado del pedido se ha actualizado exitosamentePENDIENTE a CANCELADO");

        verify(pedidoPersistencePort, times(1)).findByIdPedido(pedidoId);
        verify(pedidoUseCaseValidation, times(1)).validationUserAuthenticatedBelongsClientePedido(userAuthenticatedId, pedido);
        verify(pedidoUseCaseValidation, times(1)).validationStatusPedidoActualPendienteToCanceled(any(EstadoPedido.class));
        verify(pedidoPersistencePort, times(1)).updateStatusPedido(pedido);
        verify(trazabilidadPersistencePort, times(1)).savedTrazabilidad(trazabilidad);
    }

    @Test
    void obtainPedidoEfficiencyDeberiaRetornarListaVaciaCuandoUsuarioNoTieneRestaurantes() {
        Long userAuthenticatedId = 1L;

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort
                .findRestaurantByPropietarioId(userAuthenticatedId)).thenReturn(Collections.emptyList());

        List<String> resultado = pedidoUseCase.obtainPedidoEfficiency();

        assertThat(resultado).isEmpty();

        verify(securityContextPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .findRestaurantByPropietarioId(userAuthenticatedId);
    }

    @Test
    void obtainPedidoEfficiencyDeberiaRetornarListaVaciaCuandoNoExistenPedidos() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 10L;
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restaurantId);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort
                .findRestaurantByPropietarioId(userAuthenticatedId)).thenReturn(List.of(restaurante));
        when(pedidoPersistencePort.findAllPedidos()).thenReturn(Collections.emptyList());

        List<String> resultado = pedidoUseCase.obtainPedidoEfficiency();

        assertThat(resultado).isEmpty();

        verify(pedidoPersistencePort, times(1)).findAllPedidos();
    }

    @Test
    void obtainPedidoEfficiencyDeberiaRetornarListaVaciaCuandoNoExistenPedidosEntregados() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 10L;
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restaurantId);

        Pedido pedidoNoEntregado = new Pedido();
        pedidoNoEntregado.setId(100L);
        pedidoNoEntregado.setEstado(EstadoPedido.EN_PREPARACION);
        pedidoNoEntregado.setRestaurant(restaurante);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort
                .findRestaurantByPropietarioId(userAuthenticatedId))
                .thenReturn(List.of(restaurante));
        when(pedidoPersistencePort.findAllPedidos()).thenReturn(List.of(pedidoNoEntregado));

        List<String> resultado = pedidoUseCase.obtainPedidoEfficiency();

        assertThat(resultado).isEmpty();
    }

    @Test
    void obtainPedidoEfficiencyDeberiaCalcularEficienciaCuandoHayPedidosEntregados() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 10L;
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restaurantId);

        Pedido pedidoEntregado = new Pedido();
        pedidoEntregado.setId(100L);
        pedidoEntregado.setEstado(EstadoPedido.ENTREGADO);
        pedidoEntregado.setRestaurant(restaurante);
        pedidoEntregado.setFechaCreacion(LocalDateTime.of(2024, 3, 10, 12, 0));
        pedidoEntregado.setFechaEntrega(LocalDateTime.of(2024, 3, 10, 12, 45));

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort.findRestaurantByPropietarioId(userAuthenticatedId)).thenReturn(List.of(restaurante));
        when(pedidoPersistencePort.findAllPedidos()).thenReturn(List.of(pedidoEntregado));

        List<String> resultado = pedidoUseCase.obtainPedidoEfficiency();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(String.format(PedidoUseCaseConstants.ORDER_TOTAL_TIME, 100L, 45));

        verify(pedidoPersistencePort, times(1)).findAllPedidos();
    }

    @Test
    void employeeEfficiencyRankingDeberiaRetornarListaVaciaCuandoUsuarioNoTieneRestaurantes() {
        Long userAuthenticatedId = 1L;

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort.findRestaurantByPropietarioId(userAuthenticatedId))
                .thenReturn(Collections.emptyList());

        List<String> resultado = pedidoUseCase.employeeEfficiencyRanking();

        assertThat(resultado).isEmpty();

        verify(securityContextPort, times(1)).getAuthenticatedUserId();
        verify(restaurantPersistencePort, times(1))
                .findRestaurantByPropietarioId(userAuthenticatedId);
    }

    @Test
    void employeeEfficiencyRankingDeberiaRetornarListaVaciaCuandoNoExistenPedidos() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 10L;
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restaurantId);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort.findRestaurantByPropietarioId(userAuthenticatedId)).thenReturn(List.of(restaurante));
        when(pedidoPersistencePort.findAllPedidos()).thenReturn(Collections.emptyList());

        List<String> resultado = pedidoUseCase.employeeEfficiencyRanking();

        assertThat(resultado).isEmpty();

        verify(pedidoPersistencePort, times(1)).findAllPedidos();
    }

    @Test
    void employeeEfficiencyRankingDeberiaRetornarListaVaciaCuandoNoExistenPedidosEntregados() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 10L;
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restaurantId);

        Pedido pedidoNoEntregado = new Pedido();
        pedidoNoEntregado.setId(100L);
        pedidoNoEntregado.setEstado(EstadoPedido.EN_PREPARACION);
        pedidoNoEntregado.setRestaurant(restaurante);
        pedidoNoEntregado.setEmpleadoId(200L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort.findRestaurantByPropietarioId(userAuthenticatedId)).thenReturn(List.of(restaurante));
        when(pedidoPersistencePort.findAllPedidos()).thenReturn(List.of(pedidoNoEntregado));

        List<String> resultado = pedidoUseCase.employeeEfficiencyRanking();

        assertThat(resultado).isEmpty();
    }

    @Test
    void employeeEfficiencyRankingDeberiaCalcularRankingCuandoHayPedidosEntregados() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 10L;
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restaurantId);

        Pedido pedidoEmpleado1 = new Pedido();
        pedidoEmpleado1.setId(101L);
        pedidoEmpleado1.setEstado(EstadoPedido.ENTREGADO);
        pedidoEmpleado1.setRestaurant(restaurante);
        pedidoEmpleado1.setEmpleadoId(200L);
        pedidoEmpleado1.setFechaCreacion(LocalDateTime.of(2024, 3, 10, 12, 0));
        pedidoEmpleado1.setFechaEntrega(LocalDateTime.of(2024, 3, 10, 12, 30));

        Pedido pedidoEmpleado2 = new Pedido();
        pedidoEmpleado2.setId(102L);
        pedidoEmpleado2.setEstado(EstadoPedido.ENTREGADO);
        pedidoEmpleado2.setRestaurant(restaurante);
        pedidoEmpleado2.setEmpleadoId(300L);
        pedidoEmpleado2.setFechaCreacion(LocalDateTime.of(2024, 3, 10, 12, 0));
        pedidoEmpleado2.setFechaEntrega(LocalDateTime.of(2024, 3, 10, 12, 45));

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort.findRestaurantByPropietarioId(userAuthenticatedId)).thenReturn(List.of(restaurante));
        when(pedidoPersistencePort.findAllPedidos()).thenReturn(List.of(pedidoEmpleado1, pedidoEmpleado2));

        List<String> resultado = pedidoUseCase.employeeEfficiencyRanking();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0)).isEqualTo(String.format(PedidoUseCaseConstants.EMPLOYEE_AVERAGE_DELIVERY_TIME, 200L, 30));
        assertThat(resultado.get(1)).isEqualTo(String.format(PedidoUseCaseConstants.EMPLOYEE_AVERAGE_DELIVERY_TIME, 300L, 45));

        verify(pedidoPersistencePort, times(1)).findAllPedidos();
    }

    @Test
    void employeeEfficiencyRankingDeberiaOrdenarRankingCuandoHayMultiplesEmpleados() {
        Long userAuthenticatedId = 1L;
        Long restaurantId = 10L;
        Restaurant restaurante = new Restaurant();
        restaurante.setId(restaurantId);

        Pedido pedidoEmpleado1 = new Pedido();
        pedidoEmpleado1.setId(101L);
        pedidoEmpleado1.setEstado(EstadoPedido.ENTREGADO);
        pedidoEmpleado1.setRestaurant(restaurante);
        pedidoEmpleado1.setEmpleadoId(200L);
        pedidoEmpleado1.setFechaCreacion(LocalDateTime.of(2024, 3, 10, 12, 0));
        pedidoEmpleado1.setFechaEntrega(LocalDateTime.of(2024, 3, 10, 12, 30));

        Pedido pedidoEmpleado2 = new Pedido();
        pedidoEmpleado2.setId(102L);
        pedidoEmpleado2.setEstado(EstadoPedido.ENTREGADO);
        pedidoEmpleado2.setRestaurant(restaurante);
        pedidoEmpleado2.setEmpleadoId(300L);
        pedidoEmpleado2.setFechaCreacion(LocalDateTime.of(2024, 3, 10, 12, 0));
        pedidoEmpleado2.setFechaEntrega(LocalDateTime.of(2024, 3, 10, 12, 20));

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(userAuthenticatedId);
        when(restaurantPersistencePort.findRestaurantByPropietarioId(userAuthenticatedId)).thenReturn(List.of(restaurante));
        when(pedidoPersistencePort.findAllPedidos()).thenReturn(List.of(pedidoEmpleado1, pedidoEmpleado2));

        List<String> resultado = pedidoUseCase.employeeEfficiencyRanking();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0)).isEqualTo(String.format(PedidoUseCaseConstants.EMPLOYEE_AVERAGE_DELIVERY_TIME, 300L, 20));
        assertThat(resultado.get(1)).isEqualTo(String.format(PedidoUseCaseConstants.EMPLOYEE_AVERAGE_DELIVERY_TIME, 200L, 30));

        verify(pedidoPersistencePort, times(1)).findAllPedidos();
    }

    @Test
    void obtenerTrazabilidadPedidoDeberiaRetornarListaVaciaCuandoNoExistenRegistros() {
        Long pedidoId = 1L;

        when(trazabilidadPersistencePort.obtenerTrazabilidadPedido(pedidoId)).thenReturn(Collections.emptyList());

        List<TrazabilidadPedido> resultado = pedidoUseCase.obtenerTrazabilidadPedido(pedidoId);

        assertThat(resultado).isEmpty();

        verify(trazabilidadPersistencePort, times(1)).obtenerTrazabilidadPedido(pedidoId);
    }

    @Test
    void obtenerTrazabilidadPedidoDeberiaRetornarListaDeTrazabilidadCuandoExistenRegistros() {
        Long pedidoId = 1L;
        TrazabilidadPedido trazabilidad1 = new TrazabilidadPedido();
        trazabilidad1.setPedidoId(pedidoId);
        trazabilidad1.setEstadoAnterior(EstadoPedido.PENDIENTE);
        trazabilidad1.setEstadoNuevo(EstadoPedido.EN_PREPARACION);
        trazabilidad1.setFechaCambio(LocalDateTime.of(2024, 3, 10, 12, 0));

        TrazabilidadPedido trazabilidad2 = new TrazabilidadPedido();
        trazabilidad2.setPedidoId(pedidoId);
        trazabilidad2.setEstadoAnterior(EstadoPedido.EN_PREPARACION);
        trazabilidad2.setEstadoNuevo(EstadoPedido.LISTO);
        trazabilidad2.setFechaCambio(LocalDateTime.of(2024, 3, 10, 12, 30));

        when(trazabilidadPersistencePort.obtenerTrazabilidadPedido(pedidoId)).thenReturn(List.of(trazabilidad1, trazabilidad2));

        List<TrazabilidadPedido> resultado = pedidoUseCase.obtenerTrazabilidadPedido(pedidoId);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0)).isEqualTo(trazabilidad1);
        assertThat(resultado.get(1)).isEqualTo(trazabilidad2);

        verify(trazabilidadPersistencePort, times(1)).obtenerTrazabilidadPedido(pedidoId);
    }

    @Test
    void generarPinDeberiaGenerarPinDeCuatroDigitos() {
        String pinGenerado = pedidoUseCase.generarPin();

        assertThat(pinGenerado).matches("\\d{4}");
    }

    @Test
    void updateStatusPedidoDeberiaActualizarEstadoCorrectamente() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        Long userAuthenticatedId = 100L;
        EstadoPedido nuevoEstado = EstadoPedido.EN_PREPARACION;

        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(1L);
        pedidoActualizado.setEstado(nuevoEstado);

        doReturn(pedidoActualizado).when(pedidoPersistencePort).updateStatusPedido(any(Pedido.class));
        doReturn("El estado del pedido se ha actualizado exitosamentePENDIENTE a EN_PREPARACION")
                .when(pedidoUseCase).saveTrazabilidadPedido(any(), anyLong(), any());
        String resultado = pedidoUseCase.updateStatusPedido(pedido, nuevoEstado, userAuthenticatedId, false, null);

        assertThat(resultado).isEqualTo("El estado del pedido se ha actualizado exitosamentePENDIENTE a EN_PREPARACION");
        assertThat(pedido.getEstado()).isEqualTo(nuevoEstado);

        verify(pedidoPersistencePort, times(1)).updateStatusPedido(any(Pedido.class));
        verify(pedidoUseCase, times(1)).saveTrazabilidadPedido(any(Pedido.class), eq(userAuthenticatedId), eq(EstadoPedido.PENDIENTE));
    }

    @Test
    void saveTrazabilidadPedidoDeberiaGuardarTrazabilidadYRetornarMensaje() {

        Long userAuthenticatedId = 100L;
        EstadoPedido estadoAnterior = EstadoPedido.PENDIENTE;

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        TrazabilidadPedido trazabilidad = new TrazabilidadPedido();
        trazabilidad.setPedidoId(1L);
        trazabilidad.setEmpleadoId(userAuthenticatedId);
        trazabilidad.setEstadoAnterior(estadoAnterior);
        trazabilidad.setEstadoNuevo(EstadoPedido.EN_PREPARACION);
        trazabilidad.setFechaCambio(LocalDateTime.now());

        when(pedidoUseCaseValidation.createTrazabilidadToUpdateStatusPedido(pedido, userAuthenticatedId, estadoAnterior))
                .thenReturn(trazabilidad);

        String resultado = pedidoUseCase.saveTrazabilidadPedido(pedido, userAuthenticatedId, estadoAnterior);

        assertThat(resultado).isEqualTo(PedidoUseCaseConstants.ORDER_STATUS_UPDATED_SUCCESS + "PENDIENTE a EN_PREPARACION");

        verify(trazabilidadPersistencePort, times(1)).savedTrazabilidad(trazabilidad);
    }
}