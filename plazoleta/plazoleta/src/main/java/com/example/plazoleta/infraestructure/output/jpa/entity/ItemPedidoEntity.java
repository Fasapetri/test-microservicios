package com.example.plazoleta.infraestructure.output.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "items_pedido")
@Data
public class ItemPedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoEntity pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plato", nullable = false)
    private DishEntity plato;

    @Column(nullable = false)
    private int cantidad;

}
