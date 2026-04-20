package com.bibliotech.repository;

import com.bibliotech.model.Recurso;
import java.util.*;

public class RecursoRepository implements Repository<Recurso, String>{

    private final Map<String, Recurso> recursos = new HashMap<>();

    @Override
    public void guardar(Recurso recurso) {
        recursos.put(recurso.isbn(),recurso);
    }

    @Override
    public Optional<Recurso> buscarPorId(String id) {
        return Optional.ofNullable(recursos.get(id));
    }

    @Override
    public List<Recurso> buscarTodos() {
        return List.copyOf(recursos.values());
    }
}