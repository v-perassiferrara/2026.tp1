package com.bibliotech.model;

public record Libro(String isbn, String titulo, String autor, int paginas) implements Recurso {

}