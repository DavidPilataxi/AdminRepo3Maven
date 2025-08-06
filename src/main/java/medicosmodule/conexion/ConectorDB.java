package medicosmodule.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectorDB {

    // Cambié la URL para apuntar al servidor y instancia correcta
    private static final String URL = "jdbc:sqlserver://DESKTOP-LIQ0V6G\\SQLEXPRESS;"
            + "databaseName=polisalud;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=30";

    private static final String USER = "login1";
    private static final String PASSWORD = "P@ssw0rd";

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC no encontrado: " + e.getMessage());
        }
    }
}
