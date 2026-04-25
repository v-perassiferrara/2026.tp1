package com.bibliotech.exception;

public class RecursoNoDisponibleException extends RecursoException {
    public RecursoNoDisponibleException() {
        super("El recurso no está disponible en este momento.");
    }

    public RecursoNoDisponibleException(String message) {
        super(message);
    }
}
