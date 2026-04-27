# Decisiones de Diseño - BiblioTech

**1. Arquitectura en Capas e Inversión de Dependencias**
* Se implementó una separación estricta en cuatro capas: `Model`, `Repository`, `Service` y `Main` (CLI).
* **Dependency Inversion Principle (DIP):** Las dependencias fluyen hacia las abstracciones. Los servicios no instancian sus repositorios; los reciben por inyección en el constructor mediante la interfaz genérica `Repository<T, ID>`.
* **Single Responsibility Principle (SRP):** `PrestamoServiceImpl` actúa como orquestador. En lugar de inyectar repositorios ajenos, inyecta `CatalogoService` y `SocioService`, delegando las validaciones de existencia a los dominios correspondientes y concentrándose exclusivamente en la lógica transaccional de los préstamos.

**2. Inmutabilidad y Validación "Fail-Fast"**
* Se adoptó el uso de `records` para todas las entidades del dominio (`Estudiante`, `Docente`, `LibroFisico`, `Ebook`, `Prestamo`), garantizando inmutabilidad estructural.
* **Validación de integridad:** Los constructores compactos de los `records` validan formatos (ej. Regex para email) y consistencia básica (valores > 0). Si los datos son inválidos, se lanza una excepción en el momento de la instanciación, impidiendo la creación de objetos inconsistentes.
* **Validación de negocio:** Las reglas de dominio que requieren estado del sistema (ej. DNI único o disponibilidad de un recurso) se validan exclusivamente en la capa `Service`.

**3. Gestión de Identificadores**
* Se separó la identidad técnica de la identidad de negocio en la entidad `Socio`.
* El repositorio utiliza un `socioId` (entero autoincremental gestionado por el servicio) como Primary Key técnica.
* El `dni` se trata como una restricción de unicidad de negocio, validada antes del registro. Esto protege la inmutabilidad de la clave primaria ante posibles errores de carga en atributos humanos.

**4. Prevención de Nulos y Flujo de Excepciones**
* **Uso de Optional:** Los métodos de búsqueda por identificador retornan `Optional<T>` en toda la capa de repositorios y servicios base. Esto previene la devolución de valores nulos.
* **Delegación de control:** Los servicios base (`CatalogoService`, `SocioService`) no lanzan excepciones cuando una búsqueda por ID falla, ya que no encontrar un elemento no constituye un error de su dominio. La responsabilidad de "abrir" el `Optional` y lanzar la excepción recae en el consumidor (ej. `PrestamoService`), si la ausencia del dato rompe una regla de negocio.
* **Jerarquía de Excepciones:** Se definió `BibliotecaException` como clase base (`RuntimeException`), con ramificaciones específicas (`SocioException`, `RecursoException`, `PrestamoException`). Esto permite a la capa de presentación (`Main`) capturar y mostrar mensajes granulares al usuario final sin exponer trazas de error internas.