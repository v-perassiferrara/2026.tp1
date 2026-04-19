package com.bibliotech.model;

public record Estudiante(int socioId, String nombre, int dni, String email) implements Socio {

    @Override
    public int maxPrestamos() {
        return 3;
    }
}
