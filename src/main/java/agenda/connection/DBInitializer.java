package agenda.connection;

import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {
    private static final String DATA_BASE_NAME= DBConfig.get("db.name");


    public static void init() {
        System.out.println(DATA_BASE_NAME);
        try (
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement()
        ) {
            st.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS " + DATA_BASE_NAME
            );
            System.out.println("BD created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        try (
            Connection con = DBConnection.getDatabaseConnection();
            Statement st = con.createStatement()
        ) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS persona(
                    id_persona INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(25),
                    apellido VARCHAR(25),
                    telefono INT,
                    email VARCHAR(25),
                    id_direccion INT
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS empresa(
                    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
                    razon_social VARCHAR(50),
                    telefono INT,
                    id_direccion INT;
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS direccion(
                    id_direccion INT AUTO_INCREMENTAL PRIMARY KEY,
                    calle VARCHAR(25),
                    numero INT,
                    piso VARCHAR(25),
                    depto VARCHAR(25),
                    cp VARCHAR(25),
                    id_ciudad INT
                )
            """);

            System.out.println("db tables created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBInitializer.init();
        DBInitializer.createTables();

        System.out.println("Agenda App ready");
    }
}