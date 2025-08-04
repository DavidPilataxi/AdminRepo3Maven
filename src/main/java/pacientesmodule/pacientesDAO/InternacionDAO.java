/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacientesmodule.pacientesDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pacientesmodule.conexion.ConexionSQLServer;

/**
 *
 * @author USUARIO
 */
public class InternacionDAO {
    
    public static List<Map<String, Object>> obtenerInternacionesPorPaciente(String cedulaPaciente) {
        List<Map<String, Object>> internaciones = new ArrayList<>();
        
        String sql = "SELECT i.id_internacion, i.tipo, i.nivelDeCuidado, h.fecha_apertura, d.diagnostico " +
                     "FROM Internación i " +
                     "JOIN HistoriaClínica h ON i.id_historiaClinica = h.id_historiaClinica " +
                     "LEFT JOIN Diagnóstico d ON i.id_diagnostico = d.id_diagnostico " +
                     "JOIN Paciente p ON h.id_historiaClinica = p.id_historiaClinica " +
                     "WHERE p.cedula = ? " +
                     "ORDER BY h.fecha_apertura DESC";

        try (Connection conn = ConexionSQLServer.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cedulaPaciente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> internacion = new HashMap<>();
                internacion.put("id_internacion", rs.getInt("id_internacion"));
                internacion.put("tipo", rs.getString("tipo"));
                internacion.put("nivelDeCuidado", rs.getString("nivelDeCuidado"));
                internacion.put("fecha_apertura", rs.getDate("fecha_apertura"));
                internacion.put("diagnostico", rs.getString("diagnostico"));
                
                internaciones.add(internacion);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al obtener internaciones: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        System.out.println(internaciones);

        return internaciones;
    }
}