package com.bibliotech.model;

import java.time.LocalDate;

public record Prestamo(int prestamoId, String isbn, int socioId, LocalDate fechaInicio, LocalDate fechaVencimiento, LocalDate fechaDevolucion, EstadoPrestamo estado) {
}
