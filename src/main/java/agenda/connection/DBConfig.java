package agenda.connection;

import java.io.InputStream;
import java.util.Properties;

public class DBConfig {
    private static final Properties props = new Properties();

    static {
        try {
            InputStream input =
                    DBConfig.class.getClassLoader()
                            .getResourceAsStream("application.properties");

            if (input == null) {
                throw new RuntimeException(
                        "No se encontró database.properties"
                );
            }

            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}