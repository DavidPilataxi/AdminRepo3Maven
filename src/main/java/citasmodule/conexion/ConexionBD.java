package citasmodule.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private Connection conectar = null;

    // Parámetros de conexión
    private final String usuario = "sa";
    private final String contraseña = "1234";
    private final String bd = "polisalud";
    private final String servidor = "localhost";
    private final String puerto = "1433";

    // Cadena de conexión actualizada como en tu otro ejemplo
    private final String cadena = "jdbc:sqlserver://" + servidor + ":" + puerto
            + ";databaseName=" + bd
            + ";encrypt=true;trustServerCertificate=true;";

    // Método público para establecer la conexión
    public Connection establecerConexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conectar = DriverManager.getConnection(cadena, usuario, contraseña);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos:");
            e.printStackTrace();
        }

        return conectar;
    }

    // Método para cerrar la conexión
    public void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                // System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión:");
                e.printStackTrace();
            }
        }
    }
}
