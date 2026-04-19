package com.bibliotech.model;

public record Ebook(String isbn, String titulo, String autor, String formato, int anio, int categoria, int tamanoMB) implements Recurso {

}