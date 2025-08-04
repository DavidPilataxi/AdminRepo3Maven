package pacientesmodule.pacientesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import pacientesmodule.conexion.ConexionSQLServer;

public class EvolucionDAO {

    public static boolean guardarEvolucion(int idCita, String evolucion, String diagnostico, String pronostico, String tratamiento) {
        String sql = "MERGE Evolucion AS target " +
                     "USING (SELECT ? AS id_cita) AS source ON (target.id_cita = source.id_cita) " +
                     "WHEN MATCHED THEN " +
                     "    UPDATE SET evolucion = ?, diagnostico = ?, pronostico = ?, tratamiento = ? " +
                     "WHEN NOT MATCHED THEN " +
                     "    INSERT (id_cita, evolucion, diagnostico, pronostico, tratamiento) " +
                     "    VALUES (?, ?, ?, ?, ?);";
        
        try (Connection conn = ConexionSQLServer.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // CORRECCIÓN: Se asignan los 10 parámetros en el orden correcto.
            
            // 1. Parámetro para la cláusula USING
            pstmt.setInt(1, idCita);
            
            // 2, 3, 4, 5. Parámetros para la cláusula UPDATE
            pstmt.setString(2, evolucion);
            pstmt.setString(3, diagnostico);
            pstmt.setString(4, pronostico);
            pstmt.setString(5, tratamiento);
            
            // 6, 7, 8, 9, 10. Parámetros para la cláusula INSERT
            pstmt.setInt(6, idCita);
            pstmt.setString(7, evolucion);
            pstmt.setString(8, diagnostico);
            pstmt.setString(9, pronostico);
            pstmt.setString(10, tratamiento); // <-- Este era el parámetro que faltaba
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la evolución: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}