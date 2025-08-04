package pacientesmodule.pacientesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import pacientesmodule.conexion.ConexionSQLServer;

public class AntecedentesDAO {
    
    /**
     * Obtiene los antecedentes de un paciente específico usando su cédula.
     * @param cedulaPaciente La cédula del paciente a buscar.
     * @return Un Map con los antecedentes (claves: "familiares", "patologicos", "fisiologicos", "enfermedades_actuales").
     */
    public static Map<String, String> obtenerAntecedentesPorPaciente(String cedulaPaciente) {
        Map<String, String> antecedentes = new HashMap<>();
        String sql = "SELECT a.familiares, a.patologicos, a.fisiologicos, a.enfermedades_actuales " + // Campo añadido
                     "FROM Antecedentes a " +
                     "INNER JOIN Paciente p ON a.id_antecedentes = p.id_antecedetes " + // Nota: 'id_antecedetes' parece tener error de tipeo
                     "WHERE p.cedula = ?";

        try (Connection conn = ConexionSQLServer.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedulaPaciente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                antecedentes.put("familiares", rs.getString("familiares"));
                antecedentes.put("patologicos", rs.getString("patologicos"));
                antecedentes.put("fisiologicos", rs.getString("fisiologicos"));
                antecedentes.put("enfermedades_actuales", rs.getString("enfermedades_actuales")); // Nuevo campo
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error al consultar antecedentes: " + e.getMessage(),
                "Error de Base de Datos",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        System.out.println("Antecedenteeeeeeeesssssssssssssssssssss");
        System.out.println(antecedentes);
        return antecedentes;
    }
}