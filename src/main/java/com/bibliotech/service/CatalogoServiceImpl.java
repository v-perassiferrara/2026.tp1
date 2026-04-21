package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.exception.IsbnDuplicadoException;
import com.bibliotech.model.Recurso;
import com.bibliotech.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalogoServiceImpl implements CatalogoService {

    private final Repository<Recurso, String> recursoRepo;


    public CatalogoServiceImpl(Repository<Recurso, String> recursoRepo) {
        this.recursoRepo = recursoRepo;
    }

    @Override
    public void guardarRecurso(Recurso recurso) throws BibliotecaException {
        if (recursoRepo.buscarPorId(recurso.isbn()).isEmpty()) {
            recursoRepo.guardar(recurso);
        }
        // Se lanza excepcion porque romper la unicidad del ISBN es un "error" de catálogo
        else { throw new IsbnDuplicadoException(); }
    }

    @Override
    public List<Recurso> buscarPorTitulo(String titulo) {

        List<Recurso> resultados = new ArrayList<>();
        List<Recurso> todosLosRecursos = this.recursoRepo.buscarTodos();

        for(Recurso recurso : todosLosRecursos) {
            // Contains en vez de == para permitir busquedas parciales
            if (recurso.titulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultados.add(recurso);
            }
        }
        return resultados;
    }

    @Override
    public List<Recurso> buscarPorAutor(String autor) {

        List<Recurso> resultados = new ArrayList<>();
        List<Recurso> todosLosRecursos = this.recursoRepo.buscarTodos();

        for(Recurso recurso : todosLosRecursos) {
            // Contains en vez de == para permitir busquedas parciales
            if (recurso.autor().toLowerCase().contains(autor.toLowerCase())) {
                resultados.add(recurso);
            }
        }
        return resultados;
    }

    @Override
    public List<Recurso> buscarPorCategoria(String categoria) {

        List<Recurso> resultados = new ArrayList<>();
        List<Recurso> todosLosRecursos = this.recursoRepo.buscarTodos();

        for(Recurso recurso : todosLosRecursos) {
            // Contains en vez de == para permitir busquedas parciales
            if (recurso.categoria().toLowerCase().contains(categoria.toLowerCase())) {
                resultados.add(recurso);
            }
        }
        return resultados;
    }
    @Override
    public Optional<Recurso> buscarPorIsbn(String isbn) {
        // No se lanza excepcion porque no encontrar un libro no es un "error" de catálogo
        // Se gestiona directamente en PrestamoService
        return recursoRepo.buscarPorId(isbn);

    }
}
