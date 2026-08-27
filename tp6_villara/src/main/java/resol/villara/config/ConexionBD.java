package resol.villara.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Configuración para la base de datos H2 integrada[cite: 11]
    private static final String URL = "jdbc:h2:./data/videojuegos";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
