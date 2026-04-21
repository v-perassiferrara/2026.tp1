package com.bibliotech.exception;

public class IsbnDuplicadoException extends RecursoException {
    public IsbnDuplicadoException() {
        super("El recurso que se intenta guardar ya existe en el sistema.");
    }
    public IsbnDuplicadoException(String message) {
        super(message);
    }
}
