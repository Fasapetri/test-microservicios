package com.example.pedidos.service;

import com.example.pedidos.model.EstadoPedido;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class NotificationService {

    private final SmsService smsService;
    private final ClienteService clienteService;
    private final PedidoRepository pedidoRepository;

    public NotificationService(SmsService smsService, ClienteService clienteService, PedidoRepository pedidoRepository) {
        this.smsService = smsService;
        this.clienteService = clienteService;
        this.pedidoRepository = pedidoRepository;
    }

    public Mono<Void> modificarPedidoListo(Pedido pedido, String token){
        if(!EstadoPedido.LISTO.equals(pedido.getEstado())){
            return Mono.error(new IllegalArgumentException("El pedido no esta listo para notificarlo"));
        }

        return clienteService.obtenerDatosCliente(pedido.getClienteId(), token)
                .flatMap(cliente -> {

                    String pingSeguridad = generarPin();
                    String mensaje = String.format(
                            "Su pedido esta listo. Use el PIN: %s para reclamarlo",
                            pingSeguridad
                    );

                    pedido.setPinSeguridad(pingSeguridad);
                    pedidoRepository.save(pedido);

                    return smsService.enviarSms(cliente.getPhone(), mensaje);
                }).then();
    }

    private String generarPin(){
        return String.format("%04d", new Random().nextInt(10000));
    }
}
