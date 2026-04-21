package com.bibliotech.exception;

public class RecursoNoEncontradoException extends RecursoException {
    public RecursoNoEncontradoException() {
        super("No se ha encontrado ningún recurso.");
    }

    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
