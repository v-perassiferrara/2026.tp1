package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.*;

public class SocioRepository implements Repository<Socio, Integer>{

    private final Map<Integer, Socio> socios = new HashMap<>();

    @Override
    public void guardar(Socio socio) {
        socios.put(socio.socioId(),socio);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer id) {
        return Optional.ofNullable(socios.get(id));
    }

    @Override
    public List<Socio> buscarTodos() {
        return List.copyOf(socios.values());
    }
}