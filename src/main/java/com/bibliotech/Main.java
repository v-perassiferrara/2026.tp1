package com.bibliotech;

import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.repository.PrestamoRepository;
import com.bibliotech.repository.RecursoRepository;
import com.bibliotech.repository.SocioRepository;
import com.bibliotech.service.*;
import com.bibliotech.model.*;
import com.bibliotech.exception.BibliotecaException;

import java.util.List;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static CatalogoService catalogoService;
    private static SocioService socioService;
    private static PrestamoService prestamoService;

    public static void main(String[] args) {
        inicializarSistema();

        Scanner scanner = new Scanner(System.in); // Para permitir inputs del usuario

        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            try {
                int opcion = Integer.parseInt(scanner.nextLine()); // Manejo limpio de los inputs
                switch (opcion) {
                    // SECCIÓN PRÉSTAMOS
                    case 1 -> realizarPrestamoMenu(scanner); // Se le pasa scanner para seguir usando inputs
                    case 2 -> buscarPrestamoMenu(scanner);
                    case 3 -> registrarDevolucionMenu(scanner);
                    case 4 -> verHistorialMenu();

                    // SECCIÓN RECURSOS
                    case 5 -> registrarRecursoMenu(scanner);
                    case 6 -> buscarRecursoMenu(scanner);

                    // SECCIÓN SOCIOS
                    case 7 -> registrarSocioMenu(scanner);
                    case 8 -> buscarSocioMenu(scanner);

                    case 0 -> salir = true;
                    default -> System.out.println("Opción no válida.");
                    }

                } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");

                } catch (BibliotecaException e) {   // Para excepciones personalizadas del sistema
                System.out.println("\n" + e.getMessage());

                } catch (Exception e) { // Para excepciones adicionales/no contempladas
                System.out.println("\nERROR INESPERADO: " + e.getMessage());
                    // e.printStackTrace();
                }
            }


    }

    private static void inicializarSistema() {
        RecursoRepository recursoRepo = new RecursoRepository();
        SocioRepository socioRepo = new SocioRepository();
        PrestamoRepository prestamoRepo = new PrestamoRepository();

        catalogoService = new CatalogoServiceImpl(recursoRepo);
        socioService = new SocioServiceImpl(socioRepo);
        prestamoService = new PrestamoServiceImpl(catalogoService, socioService, prestamoRepo);
    }

    private static void mostrarMenu() {
        System.out.println("\n========= BIBLIOTECH =========");
        System.out.println("1. Realizar Préstamo      5. Registrar Recurso");
        System.out.println("2. Buscar Préstamo        6. Buscar Recurso");
        System.out.println("3. Registrar Devolución   7. Registrar Socio");
        System.out.println("4. Ver Historial          8. Buscar Socio");
        System.out.println("0. Salir");
        System.out.print("Seleccione: ");
    }



    // --- LÓGICA DE PRÉSTAMOS ---
    private static void realizarPrestamoMenu(Scanner scanner) throws BibliotecaException {
        System.out.println("\n--------- REALIZAR PRÉSTAMO ---------");
        System.out.print("Ingrese ISBN del recurso solicitado: ");
        String isbn = scanner.nextLine();
        System.out.print("Ingrese ID del socio solicitante: ");
        int socioId = Integer.parseInt(scanner.nextLine());

        prestamoService.realizarPrestamo(isbn, socioId);

        System.out.println("\nPréstamo realizado correctamente.");
    }

    private static void buscarPrestamoMenu(Scanner scanner) throws BibliotecaException {
        System.out.println("\n--------- BUSCAR PRÉSTAMO ---------");
        System.out.println("Ingrese ID del préstamo a buscar: ");

        int prestamoId = Integer.parseInt(scanner.nextLine());

        Prestamo prestamo = prestamoService.verHistorial().stream()
                .filter(p -> p.prestamoId() == prestamoId)
                .findFirst().orElse(null);

        if (prestamo != null) {
            System.out.println(prestamo);
        } else {
        System.out.println("No se encontró el préstamo indicado.");
        }

    }

    private static void registrarDevolucionMenu(Scanner scanner) throws BibliotecaException {
        System.out.println("\n--------- REGISTRAR DEVOLUCIÓN ---------");
        System.out.println("Ingrese el ID del préstamo: ");
        int prestamoId = Integer.parseInt(scanner.nextLine());

        long diasDeRetraso = prestamoService.registrarDevolucion(prestamoId);

        if  (diasDeRetraso > 0) {
            System.out.println("\nAtención: Devolución realizada con un retraso de " + diasDeRetraso + " dias.");
        } else {
            System.out.println("\nDevolución realizada a tiempo. Muchas Gracias. ");
        }
    }

    private static void verHistorialMenu() {
        List<Prestamo> historial = prestamoService.verHistorial();

        if (historial.isEmpty()) {
            System.out.println("\nNo hay transacciones registradas.");
            return;
        }

        System.out.println("\n--- HISTORIAL DE TRANSACCIONES ---");
        historial.forEach(p -> System.out.println("-> " + p));
    }



    // --- LÓGICA DE RECURSOS ---
    private static void registrarRecursoMenu(Scanner scanner) throws BibliotecaException {
        System.out.println("\n--------- REGISTRAR RECURSO ---------");
        System.out.println("1. Libro Físico");
        System.out.println("2. Ebook");
        System.out.println("0. Volver");
        System.out.print("Seleccione tipo: ");
        int tipo = Integer.parseInt(scanner.nextLine());

        if (tipo == 0) {
            return;
        }

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        System.out.print("Año: ");
        int anio = Integer.parseInt(scanner.nextLine());

        Recurso recursoNuevo = null;
        switch (tipo) {
            case 1 -> {
                System.out.print("Páginas: ");
                int pags = Integer.parseInt(scanner.nextLine());
                recursoNuevo = new LibroFisico(isbn, titulo, autor, anio, categoria, pags);
            }
            case 2 -> {
                System.out.print("Formato (PDF, EPUB, ...): ");
                String formato = scanner.nextLine();
                System.out.print("Tamaño (MB): ");
                int mb = Integer.parseInt(scanner.nextLine());
                recursoNuevo = new Ebook(isbn, titulo, autor, formato, anio, categoria, mb);
            }
            default -> System.out.println("Tipo inválido.");
        }

        if (recursoNuevo != null) {
            catalogoService.guardarRecurso(recursoNuevo);
            System.out.println("\nRecurso registrado con éxito.");
        }
    }

    private static void buscarRecursoMenu(Scanner scanner) {
        System.out.println("\n--------- BUSCAR RECURSO ---------");
        System.out.println("1. Por ISBN");
        System.out.println("2. Por Título");
        System.out.println("3. Por Autor");
        System.out.println("4. Por Categoría");
        System.out.println("0. Volver");
        System.out.print("Seleccione criterio: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());

            if (opcion == 0) {
                return;
            }

            System.out.print("Ingrese el texto a buscar: ");
            String busqueda = scanner.nextLine();

            List<Recurso> resultados = new ArrayList<>();

            switch (opcion) {
                // Si el optional retornado no está vacio, lo agrega a la lista de resultados
                case 1 -> catalogoService.buscarPorIsbn(busqueda).ifPresent(resultados::add);

                case 2 -> resultados = catalogoService.buscarPorTitulo(busqueda);
                case 3 -> resultados = catalogoService.buscarPorAutor(busqueda);
                case 4 -> resultados = catalogoService.buscarPorCategoria(busqueda);
                default -> System.out.println("Opción no válida.");
            }

            if (resultados.isEmpty()) {
                System.out.println("\nNo se encontraron recursos para el criterio ingresado.");
            } else {
                System.out.println("\n--- RESULTADOS ENCONTRADOS ---");
                resultados.forEach(r -> System.out.println("-> " + r));
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un número para seleccionar el criterio.");
        }
    }



    // --- LÓGICA DE SOCIOS ---
    private static void registrarSocioMenu(Scanner scanner) throws BibliotecaException {
        System.out.println("\n--------- REGISTRAR SOCIO ---------");
        System.out.println("1. Estudiante");
        System.out.println("2. Docente");
        System.out.println("0. Volver");
        System.out.print("Seleccione tipo: ");
        int tipo = Integer.parseInt(scanner.nextLine());

        if (tipo == 0) {
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("DNI: ");
        int dni = Integer.parseInt(scanner.nextLine());
        System.out.print("Email: ");
        String email = scanner.nextLine();

        int id = socioService.generarProximoSocioId();
        Socio socioNuevo = (tipo == 1) ? new Estudiante(id, nombre, dni, email) : new Docente(id, nombre, dni, email);

        socioService.registrarSocio(socioNuevo);
        System.out.println("\nSocio registrado con éxito. ID asignado: " + id);
    }

    private static void buscarSocioMenu(Scanner scanner) {
        System.out.println("\n--------- BUSCAR SOCIO ---------");
        System.out.println("1. Por ID de Socio");
        System.out.println("2. Por DNI");
        System.out.println("3. Por Nombre");
        System.out.println("4. Por Email");
        System.out.println("0. Volver");
        System.out.print("Seleccione criterio: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());

            if (opcion == 0) {
                return;
            }

            System.out.print("Ingrese el dato a buscar: ");
            String busqueda = scanner.nextLine();

            List<Socio> resultados = new ArrayList<>();

            switch (opcion) {
                case 1 -> socioService.buscarPorSocioId(Integer.parseInt(busqueda)).ifPresent(resultados::add);
                case 2 -> socioService.buscarPorDni(Integer.parseInt(busqueda)).ifPresent(resultados::add);
                case 3 -> resultados = socioService.buscarPorNombre(busqueda);
                case 4 -> resultados = socioService.buscarPorEmail(busqueda);
                default -> System.out.println("Opción no válida.");
            }

            if (resultados.isEmpty()) {
                System.out.println("\nNo se encontraron socios.");
            } else {
                System.out.println("\n--- RESULTADOS ENCONTRADOS ---");
                resultados.forEach(s -> System.out.println("-> " + s));
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El dato ingresado es inválido para este criterio.");
        }
    }
}
