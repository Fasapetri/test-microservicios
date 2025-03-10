package com.example.plazoleta.application.handler;

import com.example.plazoleta.application.dto.PedidoRequest;
import com.example.plazoleta.application.dto.PedidoResponse;
import com.example.plazoleta.application.dto.TrazabilidadPedidoResponse;
import com.example.plazoleta.application.mapper.PedidoMapper;
import com.example.plazoleta.application.mapper.TrazabilidadPedidoMapper;
import com.example.plazoleta.domain.api.IPedidoServicePort;
import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.domain.model.Pedido;
import com.example.plazoleta.domain.model.TrazabilidadPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PedidoHandler implements IPedidoHandler{

    private final IPedidoServicePort pedidoServicePort;
    private final PedidoMapper pedidoMapper;
    private final TrazabilidadPedidoMapper trazabilidadPedidoMapper;

    @Override
    public PedidoResponse savePedido(PedidoRequest pedidoToCreate) {
        Pedido mapperPedido = pedidoMapper.toPedido(pedidoToCreate);
        return pedidoMapper.toPedidoResponse(pedidoServicePort.savePedido(mapperPedido));
    }

    @Override
    public Page<PedidoResponse> findByStatusPedido(EstadoPedido findStatusPedido, Long findRestaurantId, int pagina, int cantidadPorPagina) {
        Page<Pedido> paginacionPedidos = pedidoServicePort.findByStatusPedido(findStatusPedido, findRestaurantId, pagina, cantidadPorPagina);
        return paginacionPedidos.map(pedidoMapper::toPedidoResponse);
    }

    @Override
    public String updateStatusPedidoEnPreparacion(Long findPedidoId) {
        return pedidoServicePort.updateStatusPedidoEnPreparacion(findPedidoId);
    }

    @Override
    public String updateStatusPedidoEntregado(Long findPedidoId, String smsPinSecurityRetirePedido) {
        return pedidoServicePort.updateStatusPedidoEntregado(findPedidoId, smsPinSecurityRetirePedido);
    }

    @Override
    public String updateStatusPedidoListo(Long findPedidoId) {
        return pedidoServicePort.updateStatusPedidoListo(findPedidoId);
    }

    @Override
    public String canceledPedido(Long findPedidoId) {
        return pedidoServicePort.canceledPedido(findPedidoId);
    }

    @Override
    public List<String> obtainPedidoEfficiency() {
        return pedidoServicePort.obtainPedidoEfficiency();
    }

    @Override
    public List<String> employeeEfficiencyRanking() {
        return pedidoServicePort.employeeEfficiencyRanking();
    }

    @Override
    public List<TrazabilidadPedidoResponse> obtenerTrazabilidadPedido(Long findPedidoId) {
        return trazabilidadPedidoMapper.toListTrazabilidadPedidoResponse(pedidoServicePort.obtenerTrazabilidadPedido(findPedidoId));
    }
}
