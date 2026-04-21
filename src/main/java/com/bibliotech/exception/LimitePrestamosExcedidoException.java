package com.bibliotech.exception;

public class LimitePrestamosExcedidoException extends PrestamoException {
    public LimitePrestamosExcedidoException() {
        super("Se ha excedido el límite de prestamos simultáneos.");
    }
    public LimitePrestamosExcedidoException(String message) {
        super(message);
    }
}
