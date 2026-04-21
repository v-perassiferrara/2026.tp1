package com.bibliotech.exception;

public class EmailInvalidoException extends SocioException {
    public EmailInvalidoException() {
        super("El email indicado es inválido.");
    }
    public EmailInvalidoException(String message) {
        super(message);
    }
}
