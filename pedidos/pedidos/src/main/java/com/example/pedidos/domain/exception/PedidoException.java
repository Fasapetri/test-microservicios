package com.example.pedidos.domain.exception;

public class PedidoException extends RuntimeException{

    private final PedidoExceptionType pedidoExceptionType;

    public PedidoException(PedidoExceptionType pedidoExceptionType) {
        super(pedidoExceptionType.getMessage());
        this.pedidoExceptionType = pedidoExceptionType;
    }

    public PedidoException(PedidoExceptionType type, String detalle) {
        super(type.getMessage() + ": " + detalle);
        this.pedidoExceptionType = type;
    }

    public PedidoExceptionType getPedidoExceptionType(){
        return pedidoExceptionType;
    }
}
