package com.bibliotech.service;

import com.bibliotech.model.LibroFisico;
import com.bibliotech.repository.Repository;

public class PrestamoService {
    private final Repository<LibroFisico, String> libroRepo;
    private final Repository<Socio, Integer> socioRepo;

    // Inyección por constructor
    public PrestamoService(Repository<LibroFisico, String> libroRepo, Repository<Socio, Integer> socioRepo) {
        this.libroRepo = libroRepo;
        this.socioRepo = socioRepo;
    }

    public void realizarPrestamo(String isbn, int socioId) throws BibliotecaException {
        // Implementar validaciones y lógica
    }
}