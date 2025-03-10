package com.example.pedidos.domain.usecase;

import com.example.pedidos.domain.api.ITrazabilidadServicePort;
import com.example.pedidos.domain.constants.TrazabilidadUseCaseConstants;
import com.example.pedidos.domain.model.TrazabilidadPedido;
import com.example.pedidos.domain.spi.ISecurityContextPort;
import com.example.pedidos.domain.spi.ITrazabilidadPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class TrazabilidadUseCase implements ITrazabilidadServicePort {

    private final ITrazabilidadPersistencePort trazabilidadPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public TrazabilidadUseCase(ITrazabilidadPersistencePort iTrazabilidadPersistencePort, ISecurityContextPort securityContextPort) {
        this.trazabilidadPersistencePort = iTrazabilidadPersistencePort;
        this.securityContextPort = securityContextPort;
    }


    @Override
    public Flux<TrazabilidadPedido> listHistoryPedido(String pedidoId) {
        return securityContextPort.getAuthenticatedUserId()
                .flatMapMany(userAuthenticatedId -> {

                    return trazabilidadPersistencePort.findByPedidoId(pedidoId)
                            .collectList()
                            .flatMapMany(listTrazabilidadPedidos -> {
                               if(listTrazabilidadPedidos.isEmpty()){
                                   return Flux.error(new IllegalArgumentException(TrazabilidadUseCaseConstants.ERROR_NO_TRACEABILITY_FOR_ORDER));
                               }

                               boolean accesoUser = listTrazabilidadPedidos.stream()
                                       .anyMatch(trazabilidadPedido -> trazabilidadPedido.getClienteId().equals(userAuthenticatedId));

                               if(!accesoUser){
                                   return Flux.error(new IllegalArgumentException(TrazabilidadUseCaseConstants.ERROR_NO_PERMISSION_TO_VIEW_ORDER));
                               }

                               return Flux.fromIterable(listTrazabilidadPedidos);
                            });
                });
    }



}
