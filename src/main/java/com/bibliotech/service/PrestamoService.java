package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.Repository;

public class PrestamoService {
    private final Repository<Recurso, String> recursoRepo;
    private final Repository<Socio, Integer> socioRepo;

    // Inyección por constructor
    public PrestamoService(Repository<Recurso, String> recursoRepo, Repository<Socio, Integer> socioRepo) {
        this.recursoRepo = recursoRepo;
        this.socioRepo = socioRepo;
    }

    public void realizarPrestamo(String isbn, int socioId) throws BibliotecaException {
        // Implementar validaciones y lógica
    }
}