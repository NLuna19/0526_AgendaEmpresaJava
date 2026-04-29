package agenda.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = DBConfig.get("db.root.url");
    private static final String USER = DBConfig.get("db.user");
    private static final String PASS = DBConfig.get("db.pass");
    private static final String DATA_BASE_NAME= DBConfig.get("db.name");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static Connection getDatabaseConnection() throws SQLException {
        return DriverManager.getConnection(
                URL+DATA_BASE_NAME,
                USER,
                PASS
        );
    }

}
