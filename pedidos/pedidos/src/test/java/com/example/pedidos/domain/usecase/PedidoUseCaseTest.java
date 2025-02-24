package com.example.pedidos.domain.usecase;

import com.example.pedidos.domain.constants.PedidoUseCaseConstants;
import com.example.pedidos.domain.model.Client;
import com.example.pedidos.domain.model.EstadoPedido;
import com.example.pedidos.domain.model.ItemPedido;
import com.example.pedidos.domain.model.Pedido;
import com.example.pedidos.domain.spi.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PedidoUseCaseTest {

    @Mock
    private  IPedidoPersistencePort pedidoPersistencePort;

    @Mock
    private  ITrazabilidadPersistencePort trazabilidadPersistencePort;

    @Mock
    private  IRestaurantServicePort restaurantServicePort;

    @Mock
    private  IUserClientServicePort userClientServicePort;

    @Mock
    private  ISmsServicePort smsServicePort;

    @Mock
    private  ISecurityContextPort securityContextPort;

    @InjectMocks
    private PedidoUseCase pedidoUseCase;

    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pedidoMock = new Pedido();
        pedidoMock.setId("pedido123");
        pedidoMock.setEstado(EstadoPedido.PENDIENTE);
        pedidoMock.setFechaCreacion(LocalDateTime.now());
        pedidoMock.setRestauranteId(1L);
        pedidoMock.setClienteId(10L);
        pedidoMock.setEmpleadoId(100L);
    }

    @Test
    void testSavePedidoSuccess(){

        when(securityContextPort.getUserAuthenticateRol()).thenReturn(Mono.just("EMPLEADO"));
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(Mono.just(100L));
        when(restaurantServicePort.existsRestaurant(1L)).thenReturn(Mono.just(true));
        when(userClientServicePort.findDataClient(10L)).thenReturn(Mono.just(new Client(10L, "Farid", "Peña", "+573115211945")));
        when(pedidoPersistencePort.findByClienteIdAndEstadoIn(10L, new EstadoPedido[]{EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO})).thenReturn(Mono.empty());


        List<Object> dishAvailable = List.of(Map.of("id", 1L, PedidoUseCaseConstants.DISH_ACTIVE_KEY, true));

        when(restaurantServicePort.findDishsRestaurant(1L)).thenReturn(Mono.just(dishAvailable));

        List<ItemPedido> itemPedidoList = List.of(new ItemPedido(1L, 2));
        pedidoMock.setItems(itemPedidoList);

        when(pedidoPersistencePort.savePedido(any(Pedido.class))).thenReturn(Mono.just(pedidoMock));

        Mono<Pedido> result = pedidoUseCase.savePedido(pedidoMock);

        StepVerifier.create(result)
                .assertNext(pedido -> {
                    assertNotNull(pedido);
                    assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
                })
                .verifyComplete();


        verify(pedidoPersistencePort, times(1)).savePedido(any(Pedido.class));
        verify(restaurantServicePort, times(1)).findDishsRestaurant(1L);

    }
}