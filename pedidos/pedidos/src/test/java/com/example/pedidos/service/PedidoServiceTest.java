package com.example.pedidos.service;

import com.example.pedidos.client.RestauranteClient;
import com.example.pedidos.client.UsuarioClient;
import com.example.pedidos.model.ClienteDTO;
import com.example.pedidos.model.EstadoPedido;
import com.example.pedidos.model.ItemPedido;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.repository.PedidoRepository;
import com.example.pedidos.repository.TrazabilidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private RestauranteClient restauranteClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private TrazabilidadRepository trazabilidadRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private String token;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId("1");
        pedido.setClienteId(2L);
        pedido.setRestauranteId(1L);
        pedido.setEmpleadoId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());
        List<ItemPedido> items = List.of(new ItemPedido(1L, 2));
        pedido.setItems(items);
        token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0OEBleGFtcGxlLmNvbSIsInJvbGUiOiJFTVBMRUFETyIsInVzZXJJZCI6OSwiaWF0IjoxNzM5MTI0ODA4LCJleHAiOjE3MzkxNjA4MDh9.EEsQpPQKiU_yW75OoNXu5CKhGlXQojN-XefDpMa_wJ0";
    }

    //PRUEBA DE CREAR UN PEDIDO EXITOSO
    @Test
    void testCrearPedidoExitoso(){
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("userId", 1L);
        usuario.put("rol", "EMPLEADO");
        usuario.put("email", "test@gmail.com");

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(2L);
        clienteDTO.setName("Juan");
        clienteDTO.setLastName("Perez");
        clienteDTO.setPhone("+573115211945");

        doReturn(usuario).when(usuarioClient).validateToken(token);
        doReturn(true).when(restauranteClient).existeRestaurante(1L, token);
        doReturn(clienteDTO).when(usuarioClient).findById(2L, token);
        doReturn(Optional.empty()).when(pedidoRepository).findByClienteIdAndEstadoIn(2L, new EstadoPedido[]{EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO});
        doReturn(Map.of("1", Map.of("active", true, "precio", 4))).when(restauranteClient).obtenerPlatosRestaurante(1L, token);
        doReturn(Mono.just(pedido)).when(pedidoRepository).save(any(Pedido.class));

        StepVerifier.create(pedidoService.crearPedido(token, pedido))
                .expectNext(pedido)
                .verifyComplete();
    }

    //ERROR PRUEBA SI EL USUARIO NO ES EMPLEADO

    @Test
    void testCrearPedidoFallaSiNoEsEmpleado(){
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("userId", 3L);
        usuario.put("rol", "CLIENTE");
        usuario.put("email", "test2@gmail.com");

        doReturn(usuario).when(usuarioClient).validateToken(token);

        StepVerifier.create(pedidoService.crearPedido(token, pedido))
                .expectErrorMessage("Solo los empleados pueden realizar pedido")
                .verify();
    }

    //ERROR SI EL RESTAURANTE NO EXISTE
    @Test
    void testCrearPedidoFallaSiElRestauranteNoExiste(){
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("userId", 1L);
        usuario.put("rol", "EMPLEADO");
        usuario.put("email", "test@gmail.com");

        doReturn(usuario).when(usuarioClient).validateToken(token);
        doReturn(false).when(restauranteClient).existeRestaurante(1L, token);

        StepVerifier.create(pedidoService.crearPedido(token, pedido))
                .expectErrorMessage("Restaurante no existe")
                .verify();
    }

    //OBTENER PEDIDOS EXITOSAMENTE
    @Test
    void testObtenerPedidosExitosamente(){
        doReturn(List.of(pedido)).when(pedidoRepository).findAll();

        StepVerifier.create(pedidoService.obtenerPedidos())
                .expectNext(pedido)
                .verifyComplete();
    }

    //ERROR TEST OBTENER PEDIDOS CUANDO NO HAY UNO
    @Test
    void testObtenerPedidosFallaCuandoNoHayPedidos(){
        doReturn(Collections.emptyList()).when(pedidoRepository).findAll();

        StepVerifier.create(pedidoService.obtenerPedidos())
                .expectErrorMessage("No hay pedidos disponibles")
                .verify();
    }

    //OBTENER UN PEDIDO POR ID EXITOSAMENTE
    @Test
    void testObtenerUnPedidoPorSuId(){
        doReturn(Optional.of(pedido)).when(pedidoRepository).findById("1");

        StepVerifier.create(pedidoService.obtenerPedidoId("1"))
                .expectNext(pedido)
                .verifyComplete();
    }

    //ERROR BUSCAR EL PEDIDO POR ID SI ESTE NO EXISTE
    @Test
    void testObtenerPedidoPorIdFallaNoExiste(){
        doReturn(Optional.empty()).when(pedidoRepository).findById("222");

        StepVerifier.create(pedidoService.obtenerPedidoId("222"))
                .expectErrorMessage("No existe el pedido")
                .verify();
    }

    //ERROR SI EL CLIENTE YA TIENE UN PEDIDO EN PROCESO
    @Test
    void testCrearPedidoFallaClienteYaTieneUnPedidoEnProceso(){
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("userId", 1L);
        usuario.put("rol", "EMPLEADO");
        usuario.put("email", "test@gmail.com");

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(2L);
        clienteDTO.setName("Juan");
        clienteDTO.setLastName("Perez");
        clienteDTO.setPhone("+573115211945");

        doReturn(usuario).when(usuarioClient).validateToken(token);
        doReturn(true).when(restauranteClient).existeRestaurante(1L, token);
        doReturn(clienteDTO).when(usuarioClient).findById(2L, token);
        doReturn(Optional.of(pedido)).when(pedidoRepository).findByClienteIdAndEstadoIn(2L, new EstadoPedido[]{EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO});

        StepVerifier.create(pedidoService.crearPedido(token, pedido))
                .expectErrorMessage("Este cliente ya tiene un pedido en curso")
                .verify();
    }
}