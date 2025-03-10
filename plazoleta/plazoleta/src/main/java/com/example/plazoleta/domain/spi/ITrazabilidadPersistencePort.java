package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.TrazabilidadPedido;

import java.util.List;

public interface ITrazabilidadPersistencePort {

    void savedTrazabilidad(TrazabilidadPedido trazabilidadPedido);

    List<TrazabilidadPedido> obtenerTrazabilidadPedido(Long findPedidoId);
}
