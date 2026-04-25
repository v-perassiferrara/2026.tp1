package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService {

    void registrarSocio(Socio socio) throws BibliotecaException;
    List<Socio> buscarPorNombre(String nombre);
    List<Socio> buscarPorEmail(String email);

    // Optional para no devolver null
    Optional<Socio> buscarPorSocioId(int socioId);
    Optional<Socio> buscarPorDni(int dni);

    // Para generacion de socioId autoincrementales
    int generarProximoSocioId();
}
