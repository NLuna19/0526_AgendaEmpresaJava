package agenda.presentation;

import agenda.data.*;
import agenda.domain.Ciudad;
import agenda.domain.Direccion;
import agenda.domain.Persona;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class AgendaEmpresaApp {
    public static ICiudadDAO cityDAO = new CiudadDAO();
    public static IDireccionDAO addressDAO = new DireccionDAO();
    public static IPersonaDAO peopleDAO = new PersonaDAO();

    private static final String MAIN_MENU = """
            --- Agenda Java---
            
            1 - Personas
            2 - Empresas
            3 - Contactos
            4 - Búsquedas
            0 - Salir
            Elije una opcion:\s""";
    private static final String PERSONA_MENU = """
            --- PERSONAS ---
            
            1 - Crear persona
            2 - Editar persona
            3 - Eliminar persona
            4 - Buscar persona
            5 - Listar personas
            0 - Volver
            Elije una opcion:\s""";
    private static final String EMPRESA_MENU = """
            --- EMPRESAS ---
            
            1 - Crear empresa
            2 - Editar empresa
            3 - Eliminar empresa
            4 - Listar empresas
            0 - Volver
            Elije una opcion:\s""";
    private static final String CONTACTO_MENU = """
            --- CONTACTOS ---
            
            1 - Asociar contacto
            2 - Eliminar contacto
            3 - Ver contactos de empres
            0 - Volver
            Elije una opcion:\s""";
    private static final String BUSQUEDA_MENU = """
            --- BUSQUEDAS ---
            
            1 - Buscar por nombre
            2 - Buscar por ciudad
            3 - Buscar por nombre y ciudades
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
                    default ->
                            System.out.println("\nOpción no reconocida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nDebe ingresar un número.");
            } catch (Exception e) {
                System.out.println(
                        "\nError al ejecutar opciones: "
                                + e.getMessage()
                );
            }
            System.out.println();
        }

        console.close();
    }

    // SUBMENUS

    public static void personasMenu(Scanner console) throws SQLException {
        boolean back = false;
        while (!back) {
            int option = showMenu(PERSONA_MENU, console);
            switch (option) {
                case 1 -> {
                    System.out.println("\n[CREAR PERSONA]");
                    createPersona(console);
                }
                case 2 -> System.out.println("\n[EDITAR PERSONA]");
                case 3 -> System.out.println("\n[ELIMINAR PERSONA]");
                case 4 -> System.out.println("\n[BUSCAR PERSONA]");
                case 5 -> getPersonaList();
                case 0 -> back = true;
                default -> System.out.println("\nOpción no reconocida.");
            }
            if(option != 0) {
                System.out.print("Presione cualquier tecla para continuar...\n");
                console.nextLine();
            }
        }

    }

    // PERSONA
    public static void createPersona(Scanner console) throws SQLException {
        System.out.println("--- Agregar Persona ---");
        System.out.println("-- Nueva persona --");
        System.out.print("-- Nombre: ");
        var _nombre = console.nextLine();
        System.out.print("-- Apellido: ");
        var _apellido = console.nextLine();
        System.out.print("-- telefono: ");
        var _telefono = console.nextLine();
        System.out.print("-- email: ");
        var _email = console.nextLine();
        //DIRECCION
        System.out.println("-- Direccion --");
        System.out.print("-- calle: ");
        var _calle = console.nextLine();
        System.out.print("-- numero: ");
        var _numero = Integer.parseInt(console.nextLine());
        System.out.print("-- piso: ");
        var _piso = console.nextLine();
        System.out.print("-- depto: ");
        var _depto = console.nextLine();
        System.out.print("-- codigo postal: ");
        var _cp = console.nextLine();
        //CIUDAD
        System.out.print("-- ciudad: ");
        var _ciudad = console.nextLine();
        System.out.print("-- provincia: ");
        var _provincia = console.nextLine();
        System.out.print("-- pais: ");
        var _pais = console.nextLine();

        Ciudad inputCity = new Ciudad(_ciudad, _provincia, _pais);
        Ciudad newCity;
        Optional<Ciudad> ciudadDB = cityDAO.findCity(inputCity);
        if(ciudadDB.isPresent()) {
            newCity = ciudadDB.get();
        } else {
            newCity = cityDAO.addCity(inputCity);
        }

        Direccion inputAddress = new Direccion(_calle,_numero,_piso,_depto,_cp,newCity);
        Direccion newAddress;
        Optional<Direccion> addressDB = addressDAO.findAddress(inputAddress);
        if(addressDB.isPresent()) {
            newAddress = addressDB.get();
        } else {
            newAddress = addressDAO.addAddress(inputAddress);
        }

        Persona newPerson = new Persona(_nombre, _apellido, _telefono, _email, newAddress);
        if(peopleDAO.addPerson(newPerson)) {
            System.out.println("Persona agregada exitosamente!!\n" + newPerson);
        } else {
            System.out.println("No se pudo agregar a la Persona.");
        }
    }

    //5
    public static void getPersonaList() throws SQLException {
        System.out.println("--- Listado de Personas ---");
        var people = peopleDAO.personList();
        people.forEach(System.out::println);
    }


    public static void empresasMenu(Scanner console) {
        boolean back = false;
        while (!back) {
            int option = showMenu(EMPRESA_MENU, console);
            switch (option) {
                case 1 -> System.out.println("\n[CREAR EMPRESA]");
                case 2 -> System.out.println("\n[EDITAR EMPRESA]");
                case 3 -> System.out.println("\n[ELIMINAR EMPRESA]");
                case 4 -> System.out.println("\n[LISTAR EMPRESAS]");
                case 0 -> back = true;
                default -> System.out.println("\nOpción no reconocida.");
            }
        }

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
                default ->
                        System.out.println("\nOpción no reconocida.");
            }
        }
    }

    public static void busquedasMenu(Scanner console) {
        boolean back = false;
        while (!back) {
            int option = showMenu(BUSQUEDA_MENU, console);
            switch (option) {
                case 1 -> System.out.println("\n[BUSCAR POR NOMBRE]");
                case 2 -> System.out.println("\n[BUSCAR POR CIUDAD]");
                case 3 -> buscarPorNombreYCiudades(console);
                case 0 -> back = true;
                default -> System.out.println("\nOpción no reconocida.");
            }
        }
    }

    // ACCIONES

    public static void buscarPorNombreYCiudades(Scanner console) {

        System.out.println("""
                --- BUSQUEDA POR NOMBRE Y CIUDADES ---
                """);
        System.out.print("Nombre: ");
        String nombre = console.nextLine();
        System.out.print("Apellido: ");
        String apellido = console.nextLine();

        System.out.print("""
                Ciudades separadas por coma:
                """);
        String ciudadesInput = console.nextLine();
        String[] ciudades = ciudadesInput.split(",");

        System.out.println("""
                
                Buscando:
                """ + nombre + " " + apellido);

        System.out.println("\nCiudades:");

        for (String ciudad : ciudades) {
            System.out.println("- " + ciudad.trim());
        }

        System.out.println("\n[RESULTADOS]");
    }

    // HELPERS

    public static int showMenu(String menu, Scanner console) {
        System.out.print(menu);
        return Integer.parseInt(console.nextLine());
    }


}

