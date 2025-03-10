package com.example.plazoleta.infraestructure.output.jpa.repository;

import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.infraestructure.output.jpa.entity.PedidoEntity;
import com.example.plazoleta.infraestructure.output.jpa.entity.RestaurantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class IPedidoRepositoryTest {

    @Autowired
    private IPedidoRepository pedidoRepository;

    private PedidoEntity pedido1;
    private PedidoEntity pedido2;
    private RestaurantEntity restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantEntity();
        restaurant.setId(1L);

        pedido1 = new PedidoEntity();
        pedido1.setClienteId(100L);
        pedido1.setEstado(EstadoPedido.PENDIENTE);
        pedido1.setRestaurant(restaurant);
        pedido1.setFechaCreacion(LocalDateTime.now());

        pedido2 = new PedidoEntity();
        pedido2.setClienteId(100L);
        pedido2.setEstado(EstadoPedido.ENTREGADO);
        pedido2.setRestaurant(restaurant);
        pedido2.setFechaCreacion(LocalDateTime.now());

        pedidoRepository.save(pedido1);
        pedidoRepository.save(pedido2);
    }

    @Test
    void findByClienteIdAndEstadoInDeberiaRetornarPedidoActivo() {
        EstadoPedido[] estadosActivos = {EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO};
        PedidoEntity resultado = pedidoRepository.findByClienteIdAndEstadoIn(100L, estadosActivos);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo(EstadoPedido.PENDIENTE);
    }

    @Test
    void findByEstadoAndRestaurantIdDeberiaRetornarPedidosPaginados() {
        Page<PedidoEntity> pedidosPage = pedidoRepository.findByEstadoAndRestaurantId(
                EstadoPedido.PENDIENTE, 1L, PageRequest.of(0, 10));

        assertThat(pedidosPage).isNotEmpty();
        assertThat(pedidosPage.getContent()).hasSize(1);
        assertThat(pedidosPage.getContent().get(0).getEstado()).isEqualTo(EstadoPedido.PENDIENTE);
    }
}