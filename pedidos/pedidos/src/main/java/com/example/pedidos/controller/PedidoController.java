package com.example.pedidos.controller;

import com.example.pedidos.model.EstadoPedido;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.model.TrazabilidadPedido;
import com.example.pedidos.service.NotificationService;
import com.example.pedidos.service.PedidoService;
import com.example.pedidos.service.TrazabilidadService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final NotificationService notificationService;
    private final TrazabilidadService trazabilidadService;

    public PedidoController(PedidoService pedidoService, NotificationService notificationService, TrazabilidadService trazabilidadService){
        this.pedidoService = pedidoService;
        this.notificationService = notificationService;
        this.trazabilidadService = trazabilidadService;
    }

    @PostMapping("/crear")
    public Mono<Pedido> crearPedido(@RequestHeader("Authorization") String token, @RequestBody Pedido pedido){
        return pedidoService.crearPedido(token, pedido);
    }

    @GetMapping
    public Flux<Pedido> obtenerPedido(){
        return pedidoService.obtenerPedidos();
    }

    @GetMapping("/list")
    public Flux<Pedido> listarPedidosPorEstado(@RequestHeader("Authorization") String token, @RequestParam EstadoPedido estado, @RequestParam Long restauranteId, @RequestParam int pagina, @RequestParam int cantidadPorPagina){
        return pedidoService.obtenerPedidosPorEstado(token, estado, restauranteId, pagina, cantidadPorPagina);
    }

    @PutMapping("/editEstado")
    public Mono<String> actualizarEstadoPedido(@RequestHeader("Authorization") String token, @RequestParam String idPedido, @RequestParam String estado){
        return pedidoService.actualizarEstadoPedido(token, idPedido, estado);

    }

    @PutMapping("/{id}/notificar")
    public Mono<String> notificarPedidoListo(@PathVariable String id, @RequestHeader("Authorization") String token){
        return pedidoService.obtenerPedidoId(id)
                .flatMap(pedido -> {
                    pedido.setEstado(EstadoPedido.LISTO);
                    return pedidoService.actualizarEstadoPedido(token, pedido.getId(), pedido.getEstado().toString())
                            .then(notificationService.modificarPedidoListo(pedido, token))
                            .thenReturn("Notificacion enviada con exito");
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Pedido no encontrado")));
    }

    @PutMapping("/{id}/entregar")
    public Mono<String> cambiarEstadoEntregado(@RequestHeader("Authorization") String token, @PathVariable String id, @RequestParam String pinSeguridad){
        return pedidoService.cambiarEstadoEntregadoPedido(token, id, pinSeguridad);
    }

    @PutMapping("/{id}/cancelar")
    public Mono<String> cancelarPedido(@RequestHeader("Authorization") String token, @PathVariable String id){
        return pedidoService.cancelarPedido(token, id);
    }

    @GetMapping("/{id}/historial")
    public Flux<TrazabilidadPedido> obtenerHistorialPedido(@PathVariable String id, @RequestHeader("Authorization") String token){
        return trazabilidadService.listarHistorialPedido(token, id);
    }
}
