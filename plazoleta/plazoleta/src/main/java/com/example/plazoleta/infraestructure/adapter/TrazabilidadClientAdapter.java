package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.domain.model.TrazabilidadPedido;
import com.example.plazoleta.domain.spi.ITrazabilidadPersistencePort;
import com.example.plazoleta.infraestructure.client.TrazabilidadFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrazabilidadClientAdapter implements ITrazabilidadPersistencePort {

    private final TrazabilidadFeignClient trazabilidadFeignClient;

    @Override
    public void savedTrazabilidad(TrazabilidadPedido trazabilidadPedido) {
        trazabilidadFeignClient.savedTrazabilidad(trazabilidadPedido);
    }

    @Override
    public List<TrazabilidadPedido> obtenerTrazabilidadPedido(Long findPedidoId) {
        return trazabilidadFeignClient.obtenerTrazabilidadPedido(findPedidoId);
    }
}
