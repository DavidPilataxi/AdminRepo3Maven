package pacientesmodule.pacientesDAO;

import pacientesmodule.conexion.ConexionSQLServer;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ResultadosDAO {

    /**
     * Obtiene el diagnóstico desde la tabla Evolucion.
     * @param idCita El ID de la cita.
     * @return Un String con el diagnóstico.
     */
    public static String obtenerDiagnostico(int idCita) {
        String diagnostico = "";
        String sql = "SELECT diagnostico FROM Cita WHERE id_cita = ?";
        try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                diagnostico = rs.getString("diagnostico");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return diagnostico;
    }
  public static String obtenerFecha(int idCita) {
    String fecha = "";
    String sql = "SELECT CONVERT(varchar, fecha, 23) AS fecha_str FROM Cita WHERE id_cita = ?";
    
    try (Connection conn = ConexionSQLServer.conectar(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, idCita);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            fecha = rs.getString("fecha_str");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return fecha;
}

    /**
     * Obtiene todas las descripciones de tratamientos para una cita.
     * @param idCita El ID de la cita.
     * @return Un String con todos los tratamientos, separados por saltos de línea.
     */
    public static String obtenerTratamientos(int idCita) {
        StringBuilder tratamientos = new StringBuilder();
        String sql = "SELECT t.descripcion FROM Tratamiento t " +
                     "INNER JOIN Aplicar a ON t.id_tratamiento = a.id_tratamiento " +
                     "WHERE a.id_cita = ?";
        try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tratamientos.append(rs.getString("descripcion")).append("\n");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tratamientos.toString();
    }

    /**
     * Obtiene la tabla de exámenes y sus resultados.
     * @param idCita El ID de la cita.
     * @return Un DefaultTableModel para la tabla de exámenes.
     */
    public static DefaultTableModel obtenerExamenes(int idCita) {
        String[] columnas = {"Examen", "Resultado"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        String sql = "SELECT ex.nombre, ev.resultados FROM Examen ex " +
                     "INNER JOIN Evaluar ev ON ex.id_examen = ev.id_examen " +
                     "WHERE ev.id_cita = ?";
        try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("nombre"), rs.getString("resultados")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }

    /**
     * Obtiene la receta médica completa.
     * @param idCita El ID de la cita.
     * @return Un DefaultTableModel para la tabla de recetas.
     */
    public static DefaultTableModel obtenerReceta(int idCita) {
    String[] columnas = {"Medicamento", "Presentación", "Dosis", "Frecuencia"};
    DefaultTableModel model = new DefaultTableModel(columnas, 0);
    
    // Esta consulta une la tabla Receta para poder filtrar por id_cita.
    String sql = "SELECT m.nombre, m.presentacion, p.dosis, p.frecuencia " +
                 "FROM Medicamento m " +
                 "INNER JOIN Prescribir p ON m.id_medicamento = p.id_medicamento " +
                 "INNER JOIN Receta r ON p.id_receta = r.id_receta " +
                 "WHERE r.id_cita = ?";

    try (Connection conn = ConexionSQLServer.conectar(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, idCita);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("nombre"),
                rs.getString("presentacion"),
                rs.getString("dosis"),
                rs.getString("frecuencia")
            });
        }
    } catch (Exception e) {
        e.printStackTrace(); 
        JOptionPane.showMessageDialog(null, "Error al obtener la receta: " + e.getMessage());
    }
    return model;
}
    
    /**
     * Obtiene los signos vitales directamente de la tabla Cita.
     * @param idCita El ID de la cita.
     * @return Un DefaultTableModel para la tabla de signos vitales.
     */
    public static DefaultTableModel obtenerSignosVitales(int idCita) {
        String[] columnas = {"Presión sistólica", "Presión diastólica", "Peso", "Frecuencia Cardíaca"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        String sql = "SELECT presion_sistolica, presion_diastolica, peso, frecuencia_cardiaca FROM Cita WHERE id_cita = ?";
        try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("presion_sistolica"),
                    rs.getString("presion_diastolica"),
                    rs.getString("peso"),
                    rs.getString("frecuencia_cardiaca")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }
    public static String obtenerResultadosGenerales(int idCita) {
    StringBuilder resultados = new StringBuilder();
    String sql = "SELECT resultados FROM Evaluar WHERE id_cita = ? AND resultados IS NOT NULL";
    try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, idCita);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            resultados.append(rs.getString("resultados")).append(". ");
        }
    } catch (Exception e) { e.printStackTrace(); }
    return resultados.toString();
}
}
