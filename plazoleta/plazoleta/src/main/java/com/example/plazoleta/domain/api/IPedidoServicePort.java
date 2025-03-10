package com.example.plazoleta.domain.api;

import com.example.plazoleta.application.dto.TrazabilidadPedidoResponse;
import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.domain.model.Pedido;
import com.example.plazoleta.domain.model.TrazabilidadPedido;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPedidoServicePort {

    Pedido savePedido(Pedido pedido);

    Page<Pedido> findByStatusPedido(EstadoPedido estadoPedido, Long restaurantId, int pagina, int cantidadPorPagina);

    String updateStatusPedidoEnPreparacion(Long pedidoId);

    String updateStatusPedidoListo(Long pedidoId);

    String updateStatusPedidoEntregado(Long pedidoId, String pinSeguridad);

    String canceledPedido(Long pedidoId);

    List<String> obtainPedidoEfficiency();

    List<String> employeeEfficiencyRanking();

    List<TrazabilidadPedido> obtenerTrazabilidadPedido(Long findPedidoId);
}
