package com.example.plazoleta.application.handler;

import com.example.plazoleta.application.dto.PedidoRequest;
import com.example.plazoleta.application.dto.PedidoResponse;
import com.example.plazoleta.application.dto.TrazabilidadPedidoResponse;
import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.domain.model.TrazabilidadPedido;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPedidoHandler {

    PedidoResponse savePedido(PedidoRequest pedidoToCreate);

    Page<PedidoResponse> findByStatusPedido(EstadoPedido findStatusPedido, Long findRestaurantId, int pagina, int cantidadPorPagina);

    String updateStatusPedidoEnPreparacion(Long findPedidoId);

    String updateStatusPedidoEntregado(Long findPedidoId, String smsPinSecurityRetirePedido);

    String updateStatusPedidoListo(Long findPedidoId);

    String canceledPedido(Long findPedidoId);

    List<String> obtainPedidoEfficiency();

    List<String> employeeEfficiencyRanking();

    List<TrazabilidadPedidoResponse> obtenerTrazabilidadPedido(Long findPedidoId);

}
