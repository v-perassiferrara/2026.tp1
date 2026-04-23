package com.bibliotech.model;

import com.bibliotech.exception.EmailInvalidoException;

public record Docente(int socioId, String nombre, int dni, String email) implements Socio {

    // Para validaciones de formato, no de negocio
    public Docente {
        if (socioId < 0) throw new IllegalArgumentException("SocioID no puede ser negativo");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre es obligatorio");
        if (dni <= 0) throw new IllegalArgumentException("DNI debe ser positivo");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email es obligatorio");

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) throw new EmailInvalidoException();
    }

    @Override
    public int maxPrestamos() {
        return 5;
    }
}

