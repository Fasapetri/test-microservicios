package com.example.plazoleta.infraestructure.output.jpa.adapter;

import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.domain.model.Pedido;
import com.example.plazoleta.infraestructure.output.jpa.entity.PedidoEntity;
import com.example.plazoleta.infraestructure.output.jpa.mapper.PedidoEntityMapper;
import com.example.plazoleta.infraestructure.output.jpa.repository.IPedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PedidoJpaAdapterTest {

    @Mock
    private IPedidoRepository pedidoRepository;

    @Mock
    private PedidoEntityMapper pedidoEntityMapper;

    @InjectMocks
    private PedidoJpaAdapter pedidoJpaAdapter;

    private Pedido pedido;
    private PedidoEntity pedidoEntity;

    private List<PedidoEntity> pedidoEntities;
    private List<Pedido> pedidos;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        // Datos de prueba
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setClienteId(100L);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());

        Pedido pedido2 = new Pedido();
        pedido2.setId(2L);
        pedido2.setClienteId(200L);
        pedido2.setEstado(EstadoPedido.ENTREGADO);
        pedido2.setFechaCreacion(LocalDateTime.now());

        pedidoEntity = new PedidoEntity();
        pedidoEntity.setId(1L);
        pedidoEntity.setClienteId(100L);
        pedidoEntity.setEstado(EstadoPedido.PENDIENTE);
        pedidoEntity.setFechaCreacion(LocalDateTime.now());

        PedidoEntity pedidoEntity2 = new PedidoEntity();
        pedidoEntity2.setId(2L);
        pedidoEntity2.setClienteId(200L);
        pedidoEntity2.setEstado(EstadoPedido.ENTREGADO);
        pedidoEntity2.setFechaCreacion(LocalDateTime.now());


        pedidoEntities = Arrays.asList(pedidoEntity, pedidoEntity2);
        pedidos = Arrays.asList(pedido, pedido2);


    }

    @Test
    void savePedidoDeberiaGuardarYRetornarPedidoCorrectamente() {
        when(pedidoEntityMapper.toPedidoEntity(pedido)).thenReturn(pedidoEntity);
        when(pedidoRepository.save(pedidoEntity)).thenReturn(pedidoEntity);
        when(pedidoEntityMapper.toPedido(pedidoEntity)).thenReturn(pedido);

        Pedido resultado = pedidoJpaAdapter.savePedido(pedido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(pedido.getId());
        assertThat(resultado.getClienteId()).isEqualTo(pedido.getClienteId());

        verify(pedidoEntityMapper, times(1)).toPedidoEntity(pedido);
        verify(pedidoRepository, times(1)).save(pedidoEntity);
        verify(pedidoEntityMapper, times(1)).toPedido(pedidoEntity);
    }

    @Test
    void findAllPedidosDeberiaRetornarListaDePedidos() {
        when(pedidoRepository.findAll()).thenReturn(pedidoEntities);
        when(pedidoEntityMapper.toListPedido(pedidoEntities)).thenReturn(pedidos);

        List<Pedido> resultado = pedidoJpaAdapter.findAllPedidos();

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        assertThat(resultado.get(1).getId()).isEqualTo(2L);

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoEntityMapper, times(1)).toListPedido(pedidoEntities);
    }

    @Test
    void findByIdPedidoDeberiaRetornarPedidoCuandoExiste() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoEntity));
        when(pedidoEntityMapper.toPedido(pedidoEntity)).thenReturn(pedido);

        Pedido resultado = pedidoJpaAdapter.findByIdPedido(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getClienteId()).isEqualTo(100L);

        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoEntityMapper, times(1)).toPedido(pedidoEntity);
    }

    @Test
    void findByIdPedidoDeberiaRetornarNullCuandoNoExiste() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());

        Pedido resultado = pedidoJpaAdapter.findByIdPedido(2L);

        assertThat(resultado).isNull();

        verify(pedidoRepository, times(1)).findById(2L);
    }

    @Test
    void updateStatusPedidoDeberiaActualizarYRetornarPedido() {

        Pedido pedidoToUpdate = new Pedido();
        pedidoToUpdate.setId(1L);
        pedidoToUpdate.setClienteId(100L);
        pedidoToUpdate.setEstado(EstadoPedido.PENDIENTE);
        pedidoToUpdate.setFechaCreacion(LocalDateTime.now());

        PedidoEntity pedidoEntityUpdated = new PedidoEntity();
        pedidoEntityUpdated.setId(1L);
        pedidoEntityUpdated.setClienteId(100L);
        pedidoEntityUpdated.setEstado(EstadoPedido.ENTREGADO);
        pedidoEntityUpdated.setFechaCreacion(LocalDateTime.now());

        Pedido pedidoUpdated = new Pedido();
        pedidoUpdated.setId(1L);
        pedidoUpdated.setClienteId(100L);
        pedidoUpdated.setEstado(EstadoPedido.ENTREGADO);
        pedidoUpdated.setFechaCreacion(LocalDateTime.now());

        when(pedidoEntityMapper.toPedidoEntity(pedidoToUpdate)).thenReturn(pedidoEntityUpdated);
        when(pedidoRepository.save(pedidoEntityUpdated)).thenReturn(pedidoEntityUpdated);
        when(pedidoEntityMapper.toPedido(pedidoEntityUpdated)).thenReturn(pedidoUpdated);

        Pedido resultado = pedidoJpaAdapter.updateStatusPedido(pedidoToUpdate);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo(EstadoPedido.ENTREGADO);

        verify(pedidoEntityMapper, times(1)).toPedidoEntity(pedidoToUpdate);
        verify(pedidoRepository, times(1)).save(pedidoEntityUpdated);
        verify(pedidoEntityMapper, times(1)).toPedido(pedidoEntityUpdated);
    }

    @Test
    void findByClienteIdAndEstadoInDeberiaRetornarPedidoCuandoExiste() {
        EstadoPedido[] estados = {EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION};

        when(pedidoRepository.findByClienteIdAndEstadoIn(100L, estados)).thenReturn(pedidoEntity);
        when(pedidoEntityMapper.toPedido(pedidoEntity)).thenReturn(pedido);

        Pedido resultado = pedidoJpaAdapter.findByClienteIdAndEstadoIn(100L, estados);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getClienteId()).isEqualTo(100L);
        assertThat(resultado.getEstado()).isEqualTo(EstadoPedido.PENDIENTE);

        verify(pedidoRepository, times(1)).findByClienteIdAndEstadoIn(100L, estados);
        verify(pedidoEntityMapper, times(1)).toPedido(pedidoEntity);
    }

    @Test
    void findByClienteIdAndEstadoIn_DeberiaRetornarNullCuandoNoExiste() {
        EstadoPedido[] estados = {EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION};

        when(pedidoRepository.findByClienteIdAndEstadoIn(200L, estados)).thenReturn(null);

        Pedido resultado = pedidoJpaAdapter.findByClienteIdAndEstadoIn(200L, estados);

        assertThat(resultado).isNull();

        verify(pedidoRepository, times(1)).findByClienteIdAndEstadoIn(200L, estados);
    }

    @Test
    void findByEstadoAndRestaurantIdDeberiaRetornarPaginaDePedidos() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("fechaCreacion").descending());
        Page<PedidoEntity> pedidoEntityPage = new PageImpl<>(List.of(pedidoEntity), pageable, 1);

        Long restaurantId = 1L;
        EstadoPedido estado = EstadoPedido.PENDIENTE;

        when(pedidoRepository.findByEstadoAndRestaurantId(estado, restaurantId, pageable)).thenReturn(pedidoEntityPage);
        when(pedidoEntityMapper.toPedido(pedidoEntity)).thenReturn(pedido);

        Page<Pedido> resultado = pedidoJpaAdapter.findByEstadoAndRestaurantId(estado, restaurantId, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(resultado.getContent().get(0).getEstado()).isEqualTo(EstadoPedido.PENDIENTE);

        verify(pedidoRepository, times(1)).findByEstadoAndRestaurantId(estado, restaurantId, pageable);
        verify(pedidoEntityMapper, times(1)).toPedido(pedidoEntity);
    }

    @Test
    void findByEstadoAndRestaurantIdDeberiaRetornarPaginaVaciaCuandoNoHayPedidos() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("fechaCreacion").descending());

        Long restaurantId = 1L;
        EstadoPedido estado = EstadoPedido.PENDIENTE;
        Page<PedidoEntity> emptyPage = Page.empty(pageable);

        when(pedidoRepository.findByEstadoAndRestaurantId(estado, restaurantId, pageable)).thenReturn(emptyPage);

        Page<Pedido> resultado = pedidoJpaAdapter.findByEstadoAndRestaurantId(estado, restaurantId, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTotalElements()).isEqualTo(0);
        assertThat(resultado.getContent()).isEmpty();

        verify(pedidoRepository, times(1)).findByEstadoAndRestaurantId(estado, restaurantId, pageable);
        verify(pedidoEntityMapper, never()).toPedido(any());
    }
}