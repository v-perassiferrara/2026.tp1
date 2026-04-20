package com.bibliotech.model;

public record LibroFisico(String isbn, String titulo, String autor, int anio, int categoria, int paginas) implements Recurso {
    @Override
    public Boolean requiereDevolucionManual() {
        return Boolean.TRUE;
    }
}