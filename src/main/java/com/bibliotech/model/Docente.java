package com.bibliotech.model;

public record Docente(int socioId, String nombre, int dni, String email) implements Socio {

    @Override
    public int maxPrestamos() {
        return 5;
    }
}

