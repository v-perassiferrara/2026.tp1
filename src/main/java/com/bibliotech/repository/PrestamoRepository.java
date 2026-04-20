package com.bibliotech.repository;

import com.bibliotech.model.Prestamo;

import java.util.*;

// Especificar tipos concretos al implementar la interfaz
public class PrestamoRepository implements Repository<Prestamo, Integer>{

    // Referenciar con interfaz Map y asignar implementacion HashMap
    // Final evita poder reasignar la variable, pero el HashMap se puede modificar
    private final Map<Integer, Prestamo> prestamos = new HashMap<>();

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.put(prestamo.prestamoId(), prestamo);
    }

    // Optional.ofNullable por si get resulta nulo
    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        return Optional.ofNullable(prestamos.get(id));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return List.copyOf(prestamos.values());
    }
}
