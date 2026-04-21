package com.bibliotech.service;

import com.bibliotech.model.Recurso;

import java.util.List;
import java.util.Optional;

public interface CatalogoService {

    void guardarRecurso(Recurso recurso);
    List<Recurso> buscarPorTitulo(String titulo);
    List<Recurso> buscarPorAutor(String autor);
    List<Recurso> buscarPorCategoria(String categoria);

    // Optional para no devolver null
    Optional<Recurso> buscarPorIsbn(String isbn);
}
