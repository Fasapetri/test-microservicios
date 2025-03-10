package com.example.plazoleta.infraestructure.output.jpa.repository;

import com.example.plazoleta.infraestructure.output.jpa.entity.ItemPedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Long> {

    List<ItemPedidoEntity> findByPedidoId(Long findPedidoId);
}
