package com.bibliotech.model;

public record Ebook(String isbn, String titulo, String autor, String formato, int anio, int categoria, int tamanoMB) implements Recurso {

    // Para validaciones de formato, no de negocio
    public Ebook {
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("ISBN es obligatorio");
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título es obligatorio");
        if (autor == null || autor.isBlank()) throw new IllegalArgumentException("Autor es obligatorio");
        if (formato == null || formato.isBlank()) throw new IllegalArgumentException("Formato es obligatorio");
        if (anio <= 0) throw new IllegalArgumentException("Año debe ser positivo");
        if (tamanoMB <= 0) throw new IllegalArgumentException("Tamaño MB debe ser positivo");
    }

    @Override
    public Boolean requiereDevolucionManual() {
        return Boolean.FALSE;
    }
}