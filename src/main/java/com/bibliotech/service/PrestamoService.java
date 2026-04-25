package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.Prestamo;

import java.util.List;

public interface PrestamoService {

    void realizarPrestamo(String isbn, int socioId) throws BibliotecaException;

    long registrarDevolucion(int prestamoId) throws BibliotecaException;

    List<Prestamo> verHistorial();
}