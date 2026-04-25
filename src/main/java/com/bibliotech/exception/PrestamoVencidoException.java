package com.bibliotech.exception;

public class PrestamoVencidoException extends PrestamoException {
    public PrestamoVencidoException() {
        super("El socio tiene préstamos vencidos. Debe devolverlos antes de realizar más.");
    }

    public PrestamoVencidoException(String message) {
        super(message);
    }
}
