package com.bibliotech.service;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.exception.DniDuplicadoException;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocioServiceImpl implements SocioService {

    private final Repository<Socio, Integer> socioRepo;

    private int contadorSocioId = 0; // Para que el id sea "autoincremental"

    public SocioServiceImpl(Repository<Socio, Integer> socioRepo) {
        this.socioRepo = socioRepo;
    }

    @Override
    public int generarProximoSocioId() {
        return ++contadorSocioId; // Incrementa y retorna el nuevo ID
    }

    @Override
    public void registrarSocio(Socio socio) throws BibliotecaException {
        if (buscarPorDni(socio.dni()).isEmpty()) {
            socioRepo.guardar(socio);
        }
        // Se lanza excepcion porque romper la unicidad del DNI es un "error" de negocio de SocioService
        else { throw new DniDuplicadoException(); }
    }

    @Override
    public List<Socio> buscarPorNombre(String nombre) {

        List<Socio> resultados = new ArrayList<>();
        List<Socio> todosLosSocios = this.socioRepo.buscarTodos();

        for(Socio socio : todosLosSocios) {
            // Contains en vez de == para permitir busquedas parciales
            if (socio.nombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(socio);
            }
        }
        return resultados;
    }

    @Override
    public List<Socio> buscarPorEmail(String email) {

        List<Socio> resultados = new ArrayList<>();
        List<Socio> todosLosSocios = this.socioRepo.buscarTodos();

        for(Socio socio : todosLosSocios) {
            // Contains en vez de == para permitir busquedas parciales
            if (socio.email().toLowerCase().contains(email.toLowerCase())) {
                resultados.add(socio);
            }
        }
        return resultados;
    }

    @Override
    public Optional<Socio> buscarPorDni(int dni) {
        for (Socio socio : socioRepo.buscarTodos()) {
            if (socio.dni() == dni) {
                return Optional.of(socio);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Socio> buscarPorSocioId(int socioId) {
        // No se lanza excepcion porque no encontrar un socio no es un "error" del servicio
        // Se gestiona directamente en PrestamoService
        return socioRepo.buscarPorId(socioId);

    }

}
