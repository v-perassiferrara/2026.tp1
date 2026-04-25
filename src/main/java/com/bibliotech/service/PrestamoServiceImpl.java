package com.bibliotech.service;

import com.bibliotech.exception.*;
import com.bibliotech.model.EstadoPrestamo;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PrestamoServiceImpl implements PrestamoService {
    private final CatalogoService catalogoService;
    private final SocioService socioService;
    private final Repository<Prestamo, Integer> prestamoRepo;
    private int contadorPrestamoId = 0;

    // Inyectamos los servicios para no repetir lógica que ya existe en ellos
    public PrestamoServiceImpl(CatalogoService catalogoService,
                               SocioService socioService,
                               Repository<Prestamo, Integer> prestamoRepo) {
        this.catalogoService = catalogoService;
        this.socioService = socioService;
        this.prestamoRepo = prestamoRepo;
    }

    // Metodo auxiliar para "syncear" todos los prestamos
    private void actualizarEstados() {
        List<Prestamo> todosLosPrestamos = prestamoRepo.buscarTodos();
        LocalDate fechaHoy = LocalDate.now();

        for (Prestamo p : todosLosPrestamos) {
            if (p.estado() == EstadoPrestamo.ACTIVO) {

                // Si ya tiene fecha de devolución (porque es automática) y llegó el día, queda DEVUELTO
                if (p.fechaDevolucion() != null && !fechaHoy.isBefore(p.fechaDevolucion())) {
                    prestamoRepo.guardar(
                            new Prestamo(
                                    p.prestamoId(),
                                    p.isbn(),
                                    p.socioId(),
                                    p.fechaInicio(),
                                    p.fechaVencimiento(),
                                    p.fechaDevolucion(),
                                    EstadoPrestamo.DEVUELTO));
                }

                // Si no tiene fecha de devolución y ya se pasó del vencimiento, queda VENCIDO
                else if (fechaHoy.isAfter(p.fechaVencimiento())) {
                    prestamoRepo.guardar(
                            new Prestamo(
                                    p.prestamoId(),
                                    p.isbn(),
                                    p.socioId(),
                                    p.fechaInicio(),
                                    p.fechaVencimiento(),
                                    p.fechaDevolucion(),
                                    EstadoPrestamo.VENCIDO));
                }
            }
        }
    }


    @Override
    public void realizarPrestamo(String isbn, int socioId) throws BibliotecaException {
        // Sincronizamos estados antes de validar
        actualizarEstados();

        // 1. Validar que el recurso exista
        Recurso recurso = catalogoService.buscarPorIsbn(isbn)
                .orElseThrow(RecursoNoEncontradoException::new);
                // Referencia de metodo para instanciar la excepcion solo si el optional está vacío
                // Si hay valor lo extrae, si no, usa el constructor (::new) para lanzar la excepcion

        // 2. Validar disponibilidad (que no haya préstamos ACTIVOS para ese ISBN)
        boolean yaEstaPrestado = prestamoRepo.buscarTodos().stream()
                .anyMatch(p -> p.estado() == EstadoPrestamo.ACTIVO && p.isbn().equals(isbn));

        if (yaEstaPrestado) {
            throw new RecursoNoDisponibleException();
        }

        // 3. Validar que el socio exista
        Socio socio = socioService.buscarPorSocioId(socioId)
                .orElseThrow(SocioNoEncontradoException::new);

        // 4. Validar si el socio tiene préstamos vencidos
        boolean tieneVencidos = prestamoRepo.buscarTodos().stream()
                .anyMatch(p -> p.socioId() == socioId && p.estado() == EstadoPrestamo.VENCIDO);

        if (tieneVencidos) {
            throw new PrestamoVencidoException();   // Si tiene vencidos, no puede pedir más
        }

        // 5. Validar límite de préstamos del socio
        long numPrestamosActivosSocio = prestamoRepo.buscarTodos().stream()
                .filter(p -> p.socioId() == socioId && p.estado() == EstadoPrestamo.ACTIVO)
                .count();

        if (numPrestamosActivosSocio >= socio.maxPrestamos()) {
            throw new LimitePrestamosExcedidoException();   // Si se excede del límite, no puede pedir más
        }

        // 6. Todo OK: Crear y guardar el préstamo
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaVencimiento = fechaHoy.plusDays(14);   // Tentativamente, 2 semanas de duracion

        // Si el recurso no requiere devolución manual (como un Ebook),
        // la fecha de devolución se setea automáticamente al vencimiento, sino es nula hasta el retorno.
        LocalDate fechaDevolucion = recurso.requiereDevolucionManual() ? null : fechaVencimiento;

        Prestamo nuevoPrestamo = new Prestamo(
                ++contadorPrestamoId,
                isbn,
                socioId,
                fechaHoy,
                fechaVencimiento,
                fechaDevolucion,
                EstadoPrestamo.ACTIVO
        );

        prestamoRepo.guardar(nuevoPrestamo);
    }

    @Override
    public long registrarDevolucion(int prestamoId) throws BibliotecaException {
        // Sincronizamos estados antes de validar
        actualizarEstados();

        Prestamo prestamo = this.prestamoRepo.buscarPorId(prestamoId).orElseThrow(PrestamoNoEncontradoException::new);

        LocalDate fechaHoy = LocalDate.now();

        prestamoRepo.guardar(
                new Prestamo(
                        prestamo.prestamoId(),
                        prestamo.isbn(),
                        prestamo.socioId(),
                        prestamo.fechaInicio(),
                        prestamo.fechaVencimiento(),
                        fechaHoy,
                        EstadoPrestamo.DEVUELTO));


        if (fechaHoy.isAfter(prestamo.fechaVencimiento())) {

            // Chronounit para calcular dias de diferencia
            return ChronoUnit.DAYS.between(prestamo.fechaVencimiento(),fechaHoy);
            // Retornamos el número de días de retraso en la devolución
        }

        return 0; // No tiene días de retraso en la devolución

    }

    @Override
    public List<Prestamo> verHistorial() {
        actualizarEstados();

        return prestamoRepo.buscarTodos();
    }
}