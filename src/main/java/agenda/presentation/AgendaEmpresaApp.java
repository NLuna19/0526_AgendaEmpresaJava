package agenda.presentation;

import agenda.data.CiudadDAO;
import agenda.data.DireccionDAO;
import agenda.data.EmpresaDAO;
import agenda.data.ICiudadDAO;
import agenda.data.IDireccionDAO;
import agenda.data.IEmpresaDAO;
import agenda.data.IPersonaDAO;
import agenda.data.PersonaDAO;
import agenda.domain.Ciudad;
import agenda.domain.Direccion;
import agenda.domain.Empresa;
import agenda.domain.Persona;
import agenda.service.EmpresaService;
import agenda.service.EmpresaServiceImpl;
import agenda.service.PersonaService;
import agenda.service.PersonaServiceImpl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AgendaEmpresaApp {
    private static final ICiudadDAO ciudadDAO = new CiudadDAO();
    private static final IDireccionDAO direccionDAO = new DireccionDAO();
    private static final IPersonaDAO personaDAO = new PersonaDAO();
    private static final IEmpresaDAO empresaDAO = new EmpresaDAO();
    private static final PersonaService personaService = new PersonaServiceImpl(
            personaDAO,
            direccionDAO,
            ciudadDAO
    );
    private static final EmpresaService empresaService = new EmpresaServiceImpl(
            empresaDAO,
            direccionDAO,
            ciudadDAO
    );

    private static final String MAIN_MENU = """
            --- Agenda Java ---

            1 - Personas
            2 - Empresas
            3 - Contactos
            4 - Busquedas
            0 - Salir
            Elije una opcion:\s""";
    private static final String PERSONA_MENU = """
            --- PERSONAS ---

            1 - Crear persona
            2 - Editar persona
            3 - Eliminar persona
            4 - Buscar persona por id
            5 - Listar personas
            0 - Volver
            Elije una opcion:\s""";
    private static final String EMPRESA_MENU = """
            --- EMPRESAS ---

            1 - Crear empresa
            2 - Editar empresa
            3 - Eliminar empresa
            4 - Buscar empresa por id
            5 - Listar empresas
            0 - Volver
            Elije una opcion:\s""";
    private static final String CONTACTO_MENU = """
            --- CONTACTOS ---

            1 - Asociar contacto
            2 - Eliminar contacto
            3 - Ver contactos de empresa
            0 - Volver
            Elije una opcion:\s""";
    private static final String BUSQUEDA_MENU = """
            --- BUSQUEDAS ---

            1 - Buscar persona por nombre
            2 - Buscar persona por ciudad
            3 - Buscar persona por provincia
            4 - Buscar persona por pais
            5 - Buscar persona por nombre y ciudades
            0 - Volver
            Elije una opcion:\s""";

    public static void agendaApp() {
        boolean exit = false;
        Scanner console = new Scanner(System.in);
        while (!exit) {
            try {
                int option = showMenu(MAIN_MENU, console);
                switch (option) {
                    case 1 -> personasMenu(console);
                    case 2 -> empresasMenu(console);
                    case 3 -> contactosMenu(console);
                    case 4 -> busquedasMenu(console);
                    case 0 -> {
                        exit = true;
                        System.out.println("\nSaliendo del sistema...");
                    }
                    default -> System.out.println("\nOpcion no reconocida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nDebe ingresar un numero.");
            } catch (Exception e) {
                System.out.println("\nError al ejecutar opciones: " + e.getMessage());
            }
            System.out.println();
        }

        console.close();
    }

    public static void personasMenu(Scanner console) throws SQLException {
        boolean back = false;
        while (!back) {
            int option = showMenu(PERSONA_MENU, console);
            switch (option) {
                case 1 -> crearPersona(console);
                case 2 -> editarPersona(console);
                case 3 -> eliminarPersona(console);
                case 4 -> buscarPersonaPorId(console);
                case 5 -> listarPersonas();
                case 0 -> back = true;
                default -> System.out.println("\nOpcion no reconocida.");
            }
            if (option != 0) {
                pausar(console);
            }
        }
    }

    public static void crearPersona(Scanner console) throws SQLException {
        System.out.println("--- Crear Persona ---");
        Persona persona = leerPersona(console);

        if (personaService.crear(persona)) {
            System.out.println("Persona agregada exitosamente.");
            System.out.println(persona);
        } else {
            System.out.println("No se pudo agregar la persona.");
        }
    }

    public static void editarPersona(Scanner console) throws SQLException {
        System.out.println("--- Editar Persona ---");
        int idPersona = leerEntero(console, "Id persona: ");

        if (personaService.buscarPorId(idPersona).isEmpty()) {
            System.out.println("No existe una persona con id " + idPersona + ".");
            return;
        }

        Persona persona = leerPersona(console);
        persona.setIdPersona(idPersona);

        if (personaService.actualizar(persona)) {
            System.out.println("Persona actualizada exitosamente.");
            System.out.println(persona);
        } else {
            System.out.println("No se pudo actualizar la persona.");
        }
    }

    public static void eliminarPersona(Scanner console) throws SQLException {
        System.out.println("--- Eliminar Persona ---");
        int idPersona = leerEntero(console, "Id persona: ");

        if (personaService.eliminar(idPersona)) {
            System.out.println("Persona eliminada exitosamente.");
        } else {
            System.out.println("No se pudo eliminar la persona.");
        }
    }

    public static void buscarPersonaPorId(Scanner console) throws SQLException {
        System.out.println("--- Buscar Persona ---");
        int idPersona = leerEntero(console, "Id persona: ");
        personaService.buscarPorId(idPersona)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No se encontro una persona con id " + idPersona + ".")
                );
    }

    public static void listarPersonas() throws SQLException {
        System.out.println("--- Listado de Personas ---");
        imprimirPersonas(personaService.listar());
    }

    public static void empresasMenu(Scanner console) throws SQLException {
        boolean back = false;
        while (!back) {
            int option = showMenu(EMPRESA_MENU, console);
            switch (option) {
                case 1 -> crearEmpresa(console);
                case 2 -> editarEmpresa(console);
                case 3 -> eliminarEmpresa(console);
                case 4 -> buscarEmpresaPorId(console);
                case 5 -> listarEmpresas();
                case 0 -> back = true;
                default -> System.out.println("\nOpcion no reconocida.");
            }
            if (option != 0) {
                pausar(console);
            }
        }
    }

    public static void crearEmpresa(Scanner console) throws SQLException {
        System.out.println("--- Crear Empresa ---");
        Empresa empresa = leerEmpresa(console);

        if (empresaService.crear(empresa)) {
            System.out.println("Empresa agregada exitosamente.");
            System.out.println(empresa);
        } else {
            System.out.println("No se pudo agregar la empresa.");
        }
    }

    public static void editarEmpresa(Scanner console) throws SQLException {
        System.out.println("--- Editar Empresa ---");
        int idEmpresa = leerEntero(console, "Id empresa: ");

        if (empresaService.buscarPorId(idEmpresa).isEmpty()) {
            System.out.println("No existe una empresa con id " + idEmpresa + ".");
            return;
        }

        Empresa empresa = leerEmpresa(console);
        empresa.setIdEmpresa(idEmpresa);

        if (empresaService.actualizar(empresa)) {
            System.out.println("Empresa actualizada exitosamente.");
            System.out.println(empresa);
        } else {
            System.out.println("No se pudo actualizar la empresa.");
        }
    }

    public static void eliminarEmpresa(Scanner console) throws SQLException {
        System.out.println("--- Eliminar Empresa ---");
        int idEmpresa = leerEntero(console, "Id empresa: ");

        if (empresaService.eliminar(idEmpresa)) {
            System.out.println("Empresa eliminada exitosamente.");
        } else {
            System.out.println("No se pudo eliminar la empresa.");
        }
    }

    public static void buscarEmpresaPorId(Scanner console) throws SQLException {
        System.out.println("--- Buscar Empresa ---");
        int idEmpresa = leerEntero(console, "Id empresa: ");
        empresaService.buscarPorId(idEmpresa)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No se encontro una empresa con id " + idEmpresa + ".")
                );
    }

    public static void listarEmpresas() throws SQLException {
        System.out.println("--- Listado de Empresas ---");
        imprimirEmpresas(empresaService.listar());
    }

    public static void contactosMenu(Scanner console) {
        boolean back = false;
        while (!back) {
            int option = showMenu(CONTACTO_MENU, console);
            switch (option) {
                case 1 -> System.out.println("\n[ASOCIAR CONTACTO]");
                case 2 -> System.out.println("\n[ELIMINAR CONTACTO]");
                case 3 -> System.out.println("\n[VER CONTACTOS EMPRESA]");
                case 0 -> back = true;
                default -> System.out.println("\nOpcion no reconocida.");
            }
        }
    }

    public static void busquedasMenu(Scanner console) throws SQLException {
        boolean back = false;
        while (!back) {
            int option = showMenu(BUSQUEDA_MENU, console);
            switch (option) {
                case 1 -> buscarPersonaPorNombre(console);
                case 2 -> buscarPersonaPorCiudad(console);
                case 3 -> buscarPersonaPorProvincia(console);
                case 4 -> buscarPersonaPorPais(console);
                case 5 -> buscarPorNombreYCiudades(console);
                case 0 -> back = true;
                default -> System.out.println("\nOpcion no reconocida.");
            }
            if (option != 0) {
                pausar(console);
            }
        }
    }

    public static void buscarPorNombreYCiudades(Scanner console) throws SQLException {
        System.out.println("--- Busqueda por nombre y ciudades ---");
        System.out.print("Nombre, apellido o nombre completo: ");
        String nombre = console.nextLine();
        System.out.print("Ciudades separadas por coma. Dejar vacio para todas: ");
        List<String> ciudades = Arrays.stream(console.nextLine().split(","))
                .map(String::trim)
                .filter(valor -> !valor.isEmpty())
                .toList();

        imprimirPersonas(personaService.buscarPorNombreYCiudades(nombre, ciudades));
    }

    public static void buscarPersonaPorNombre(Scanner console) throws SQLException {
        System.out.println("--- Busqueda de persona por nombre ---");
        System.out.print("Nombre, apellido o nombre completo: ");
        imprimirPersonas(personaService.buscarPorNombre(console.nextLine()));
    }

    public static void buscarPersonaPorCiudad(Scanner console) throws SQLException {
        System.out.println("--- Busqueda de persona por ciudad ---");
        System.out.print("Ciudad: ");
        imprimirPersonas(personaService.buscarPorCiudad(console.nextLine()));
    }

    public static void buscarPersonaPorProvincia(Scanner console) throws SQLException {
        System.out.println("--- Busqueda de persona por provincia ---");
        System.out.print("Provincia: ");
        imprimirPersonas(personaService.buscarPorProvincia(console.nextLine()));
    }

    public static void buscarPersonaPorPais(Scanner console) throws SQLException {
        System.out.println("--- Busqueda de persona por pais ---");
        System.out.print("Pais: ");
        imprimirPersonas(personaService.buscarPorPais(console.nextLine()));
    }

    private static Persona leerPersona(Scanner console) {
        System.out.println("-- Persona --");
        System.out.print("Nombre: ");
        String nombre = console.nextLine();
        System.out.print("Apellido: ");
        String apellido = console.nextLine();
        System.out.print("Telefono: ");
        String telefono = console.nextLine();
        System.out.print("Email: ");
        String email = console.nextLine();

        Direccion direccion = leerDireccion(console);
        return new Persona(nombre, apellido, telefono, email, direccion);
    }

    private static Empresa leerEmpresa(Scanner console) {
        System.out.println("-- Empresa --");
        System.out.print("Razon social: ");
        String razonSocial = console.nextLine();
        System.out.print("Telefono: ");
        String telefono = console.nextLine();

        Direccion direccion = leerDireccion(console);
        return new Empresa(razonSocial, telefono, direccion);
    }

    private static Direccion leerDireccion(Scanner console) {
        System.out.println("-- Direccion --");
        System.out.print("Calle: ");
        String calle = console.nextLine();
        int numero = leerEntero(console, "Numero: ");
        System.out.print("Piso: ");
        String piso = console.nextLine();
        System.out.print("Depto: ");
        String depto = console.nextLine();
        System.out.print("Codigo postal: ");
        String cp = console.nextLine();

        Ciudad ciudad = leerCiudad(console);
        return new Direccion(calle, numero, piso, depto, cp, ciudad);
    }

    private static Ciudad leerCiudad(Scanner console) {
        System.out.println("-- Ciudad --");
        System.out.print("Ciudad: ");
        String ciudad = console.nextLine();
        System.out.print("Provincia: ");
        String provincia = console.nextLine();
        System.out.print("Pais: ");
        String pais = console.nextLine();

        return new Ciudad(ciudad, provincia, pais);
    }

    private static void imprimirPersonas(List<Persona> personas) {
        if (personas.isEmpty()) {
            System.out.println("No se encontraron personas.");
            return;
        }
        personas.forEach(System.out::println);
    }

    private static void imprimirEmpresas(List<Empresa> empresas) {
        if (empresas.isEmpty()) {
            System.out.println("No se encontraron empresas.");
            return;
        }
        empresas.forEach(System.out::println);
    }

    private static int leerEntero(Scanner console, String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(console.nextLine());
    }

    private static int showMenu(String menu, Scanner console) {
        System.out.print(menu);
        return Integer.parseInt(console.nextLine());
    }

    private static void pausar(Scanner console) {
        System.out.print("Presione Enter para continuar...");
        console.nextLine();
    }
}
