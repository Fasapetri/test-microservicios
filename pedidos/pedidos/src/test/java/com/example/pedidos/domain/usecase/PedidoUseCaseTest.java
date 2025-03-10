package com.example.pedidos.domain.usecase;

import com.example.pedidos.domain.constants.PedidoUseCaseConstants;
import com.example.pedidos.domain.exception.PedidoException;
import com.example.pedidos.domain.exception.PedidoExceptionType;
import com.example.pedidos.domain.model.*;
import com.example.pedidos.domain.spi.*;
import com.example.pedidos.infraestructure.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @Test
    void testUpdatePedidoStatusSuccess(){

        String pedidoId = "123456";
        String newStatus = "EN_PREPARACION";

        Pedido updatePedido = new Pedido();
        updatePedido.setId(pedidoId);
        updatePedido.setEstado(EstadoPedido.PENDIENTE);

        TrazabilidadPedido trazabilidadPedido = new TrazabilidadPedido();

        CustomUserDetails customUserDetails = new CustomUserDetails("test@email.com", "",
                Collections.singletonList(new SimpleGrantedAuthority("EMPLEADO")), 100L);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
        SecurityContext securityContext = new SecurityContextImpl(authentication);

        Mono<SecurityContext> securityContextMono = Mono.just(securityContext);

        doAnswer(invocation -> {
            System.out.println("Llamado a getUserAuthenticateRol()");
            return Mono.just("EMPLOYEE");
        }).when(securityContextPort).getUserAuthenticateRol();

        doAnswer(invocation -> {
            System.out.println("Llamado a getAuthenticatedUserId()");
            return Mono.just(100L);
        }).when(securityContextPort).getAuthenticatedUserId();
        when(pedidoPersistencePort.findByIdPedido(pedidoId)).thenReturn(Mono.just(updatePedido));
        when(pedidoPersistencePort.savePedido(any(Pedido.class))).thenAnswer(invocationOnMock -> {
           Pedido updatedPedido = invocationOnMock.getArgument(0);
           return Mono.just(updatedPedido);
        });
        when(trazabilidadPersistencePort.saveTrazabilidad(any(TrazabilidadPedido.class))).thenReturn(Mono.just(trazabilidadPedido));

        System.out.println("SecurityContextPort: " + securityContextPort);

        StepVerifier.create(
                        securityContextMono.flatMap(ctx ->
                                pedidoUseCase.updateStatusPedido(pedidoId, newStatus)
                                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(ctx)))
                        )
                )
                .expectNext("El estado del pedido ha sido actualizado de PENDIENTE a EN_PREPARACION")
                .verifyComplete();
    }
}