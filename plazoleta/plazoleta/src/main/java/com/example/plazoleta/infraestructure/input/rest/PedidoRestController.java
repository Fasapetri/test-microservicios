package com.example.plazoleta.infraestructure.input.rest;

import com.example.plazoleta.application.dto.PedidoRequest;
import com.example.plazoleta.application.dto.PedidoResponse;
import com.example.plazoleta.application.dto.TrazabilidadPedidoResponse;
import com.example.plazoleta.application.handler.PedidoHandler;
import com.example.plazoleta.domain.model.EstadoPedido;
import com.example.plazoleta.infraestructure.constants.PedidoRestControllerConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PedidoRestControllerConstants.PEDIDOS_BASE)
@RequiredArgsConstructor
public class PedidoRestController {

    private final PedidoHandler pedidoHandler;

    @PostMapping(PedidoRestControllerConstants.PEDIDO_SAVE)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_CLIENTE)
    public ResponseEntity<PedidoResponse> crearPedido(@RequestBody PedidoRequest pedidoToCreate){
        return ResponseEntity.ok(pedidoHandler.savePedido(pedidoToCreate));
    }

    @GetMapping(PedidoRestControllerConstants.PEDIDO_LIST_STATUS)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_EMPLEADO)
    public ResponseEntity<Page<PedidoResponse>> listarPedidosPorEstado(
            @RequestParam EstadoPedido findStatusPedido,
            @RequestParam Long findRestaurantId,
            @RequestParam int pagina,
            @RequestParam int cantidadPorPagina) {

        Page<PedidoResponse> pedidos = pedidoHandler.findByStatusPedido(findStatusPedido, findRestaurantId, pagina, cantidadPorPagina);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping(PedidoRestControllerConstants.PEDIDO_UPDATE_STATUS_PREPARACION)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_EMPLEADO)
    public ResponseEntity<String> actualizarEstadoPedidoEnPreparacion(
            @RequestParam Long findPedidoId) {

        String resultado = pedidoHandler.updateStatusPedidoEnPreparacion(findPedidoId);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping(PedidoRestControllerConstants.PEDIDO_UPDATE_STATUS_LISTO)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_EMPLEADO)
    public ResponseEntity<String> actualizarEstadoPedidoListo(
            @RequestParam Long findPedidoId) {

        String resultado = pedidoHandler.updateStatusPedidoListo(findPedidoId);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping(PedidoRestControllerConstants.PEDIDO_UPDATE_STATUS_ENTREGADO)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_EMPLEADO)
    public ResponseEntity<String> actualizarEstadoPedidoEntregado(
            @RequestParam Long findPedidoId, @RequestParam String pinSecurityPedido) {

        String resultado = pedidoHandler.updateStatusPedidoEntregado(findPedidoId, pinSecurityPedido);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping(PedidoRestControllerConstants.PEDIDO_UPDATE_STATUS_CANCELADO)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_CLIENTE)
    public ResponseEntity<String> actualizarEstadoPedidoCancelado(
            @RequestParam Long findPedidoId) {

        String resultado = pedidoHandler.canceledPedido(findPedidoId);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping(PedidoRestControllerConstants.PEDIDO_EFFICIENCY)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_PROPIETARIO)
    public ResponseEntity<List<String>> obtainPedidoEfficiency() {
        return ResponseEntity.ok(pedidoHandler.obtainPedidoEfficiency());
    }

    @GetMapping(PedidoRestControllerConstants.PEDIDO_RANKING_EMPLOYEE)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_PROPIETARIO)
    public ResponseEntity<List<String>> employeeEfficiencyRanking() {
        return ResponseEntity.ok(pedidoHandler.employeeEfficiencyRanking());
    }

    @GetMapping(PedidoRestControllerConstants.PEDIDO_TRAZABILIDAD)
    @PreAuthorize(PedidoRestControllerConstants.PEDIDO_HAS_AUTHORITY_CLIENTE)
    public ResponseEntity<List<TrazabilidadPedidoResponse>> obtenerHistorialPedido(@RequestParam Long findPedidoId) {
        return ResponseEntity.ok(pedidoHandler.obtenerTrazabilidadPedido(findPedidoId));
    }
}
