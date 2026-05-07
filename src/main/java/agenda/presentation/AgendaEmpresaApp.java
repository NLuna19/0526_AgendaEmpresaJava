package agenda.presentation;

import java.util.Scanner;

public class AgendaEmpresaApp {
    //public static ICiudadDAO cityDAO = new CiudadDAO();

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

    public static void personasMenu(Scanner console) {
        boolean back = false;
        while (!back) {
            int option = showMenu(PERSONA_MENU, console);
            switch (option) {
                case 1 -> System.out.println("\n[CREAR PERSONA]");
                case 2 -> System.out.println("\n[EDITAR PERSONA]");
                case 3 -> System.out.println("\n[ELIMINAR PERSONA]");
                case 4 -> System.out.println("\n[BUSCAR PERSONA]");
                case 5 -> System.out.println("\n[LISTAR PERSONAS]");
                case 0 -> back = true;
                default -> System.out.println("\nOpción no reconocida.");
            }
        }
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

