package com.bibliotech.exception;

public class SocioNoEncontradoException extends SocioException {
    public SocioNoEncontradoException() {
        super("No se ha encontrado ningún socio.");
    }

    public SocioNoEncontradoException(String message) {
        super(message);
    }
}
