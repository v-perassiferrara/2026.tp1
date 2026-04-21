package com.bibliotech.model;

public record LibroFisico(String isbn, String titulo, String autor, int anio, int categoria, int paginas) implements Recurso {

    // Para validaciones de formato, no de negocio
    public LibroFisico {
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("ISBN es obligatorio");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título es obligatorio");
        if (autor == null || autor.isBlank()) throw new IllegalArgumentException("Autor es obligatorio");
        if (anio <= 0) throw new IllegalArgumentException("Año debe ser positivo");
        if (paginas <= 0) throw new IllegalArgumentException("Número de páginas debe ser positivo");
    }

    @Override
    public Boolean requiereDevolucionManual() {
        return Boolean.TRUE;
    }
}