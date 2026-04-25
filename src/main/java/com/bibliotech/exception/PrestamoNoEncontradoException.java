package com.bibliotech.exception;

public class PrestamoNoEncontradoException extends PrestamoException {
    public PrestamoNoEncontradoException() {
        super("No se ha encontrado ningún préstamo.");
    }
    public PrestamoNoEncontradoException(String message) {
        super(message);
    }
}
