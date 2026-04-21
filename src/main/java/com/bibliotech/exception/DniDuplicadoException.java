package com.bibliotech.exception;

public class DniDuplicadoException extends SocioException {
    public DniDuplicadoException() {
        super("Este DNI ya está registrado para otro usuario");
    }
    public DniDuplicadoException(String message) {
        super(message);
    }
}
