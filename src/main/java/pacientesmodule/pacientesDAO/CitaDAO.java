package pacientesmodule.pacientesDAO;

import pacientesmodule.conexion.ConexionSQLServer;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {
    public static Map<String, String> obtenerPacienteConCitaMasProxima(String cedulaDoctor) {
        Map<String, String> datosPaciente = new HashMap<>();
        String sql = "SELECT TOP 1 p.cedula, p.nombres, p.apellidos, p.estado_civil, p.tipo_identificador, p.sangre, p.telefono, " +
             "p.fecha_nacimiento, p.sexo, p.edad, p.correo, p.alergias, c.fecha, c.hora,c.id_cita " +
             "FROM Cita c " +
             "INNER JOIN Paciente p ON c.id_paciente = p.cedula " +
             "WHERE c.id_doctor = ? " + 
             "AND (c.fecha > CAST(GETDATE() AS DATE) OR (c.fecha = CAST(GETDATE() AS DATE) AND c.hora >= CAST(GETDATE() AS TIME))) " +
             "ORDER BY c.fecha ASC, c.hora ASC";
        try (Connection conn = ConexionSQLServer.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cedulaDoctor);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaNacimiento = "";
                if (rs.getDate("fecha_nacimiento") != null) {
                    fechaNacimiento = sdf.format(rs.getDate("fecha_nacimiento"));
                }
                
                datosPaciente.put("cedula", rs.getString("cedula"));
                datosPaciente.put("nombres", rs.getString("nombres"));
                datosPaciente.put("apellidos", rs.getString("apellidos"));
                datosPaciente.put("fecha_nacimiento", fechaNacimiento);
                datosPaciente.put("sexo", rs.getString("sexo"));
                datosPaciente.put("correo", rs.getString("correo"));
                datosPaciente.put("alergias", rs.getString("alergias"));
                datosPaciente.put("edad", rs.getString("edad"));
                datosPaciente.put("telefono", rs.getString("telefono"));
                datosPaciente.put("estado_civil", rs.getString("estado_civil"));
                datosPaciente.put("sangre", rs.getString("sangre"));
                datosPaciente.put("fecha", rs.getString("fecha"));
                datosPaciente.put("hora", rs.getString("hora"));
                datosPaciente.put("id_cita", rs.getString("id_cita"));
                datosPaciente.put("tipo_identificador", rs.getString("tipo_identificador"));
                
                
                
                if (rs.getDate("fecha") != null) {
                    datosPaciente.put("fecha_cita", sdf.format(rs.getDate("fecha")));
                }
                if (rs.getTime("hora") != null) {
                    datosPaciente.put("hora_cita", rs.getTime("hora").toString());
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error en la consulta SQL: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Error: Driver JDBC no encontrado", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        System.out.println("*******************");
        System.out.println("Datos encontrados: " + datosPaciente); // Mensaje de depuración mejorado
        return datosPaciente;
        
    }
    
    public static Map<String, String> obtenerPacienteConCitaMasProximaEnfermero(String cedulaEnfermero) {
    Map<String, String> datosPaciente = new HashMap<>();
    String sql = "SELECT TOP 1 p.cedula, p.nombres, p.apellidos, p.estado_civil, p.tipo_identificador, p.sangre, p.telefono, " +
                 "p.fecha_nacimiento, p.sexo, p.edad, p.correo, p.alergias, c.fecha, c.hora, c.id_cita, " +
                 "c.motivo, c.peso, c.estatura, c.presion_sistolica, c.presion_diastolica, c.frecuencia_cardiaca " +
                 "FROM Cita c " +
                 "INNER JOIN Paciente p ON c.id_paciente = p.cedula " +
                 "INNER JOIN Enfermero e ON c.id_enfermero = e.cedula " +
                 "WHERE e.cedula = ? " + 
                 "AND (c.fecha > CAST(GETDATE() AS DATE) OR (c.fecha = CAST(GETDATE() AS DATE) AND c.hora >= CAST(GETDATE() AS TIME))) " +
                 "ORDER BY c.fecha ASC, c.hora ASC";

    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, cedulaEnfermero);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaNacimiento = "";
            if (rs.getDate("fecha_nacimiento") != null) {
                fechaNacimiento = sdf.format(rs.getDate("fecha_nacimiento"));
            }
            
            // Datos básicos del paciente
            datosPaciente.put("cedula", rs.getString("cedula"));
            datosPaciente.put("nombres", rs.getString("nombres"));
            datosPaciente.put("apellidos", rs.getString("apellidos"));
            datosPaciente.put("fecha_nacimiento", fechaNacimiento);
            datosPaciente.put("sexo", rs.getString("sexo"));
            datosPaciente.put("correo", rs.getString("correo"));
            datosPaciente.put("alergias", rs.getString("alergias"));
            datosPaciente.put("edad", rs.getString("edad"));
            datosPaciente.put("telefono", rs.getString("telefono"));
            datosPaciente.put("estado_civil", rs.getString("estado_civil"));
            datosPaciente.put("sangre", rs.getString("sangre"));
            
            // Datos de la cita
            datosPaciente.put("fecha", rs.getString("fecha"));
            datosPaciente.put("hora", rs.getString("hora"));
            datosPaciente.put("id_cita", rs.getString("id_cita"));
            datosPaciente.put("motivo", rs.getString("motivo"));
            datosPaciente.put("peso", rs.getString("peso"));
            datosPaciente.put("estatura", rs.getString("estatura"));
            datosPaciente.put("presion_sistolica", rs.getString("presion_sistolica"));
            datosPaciente.put("presion_diastolica", rs.getString("presion_diastolica"));
            datosPaciente.put("frecuencia_cardiaca", rs.getString("frecuencia_cardiaca"));
            datosPaciente.put("tipo_identificador",rs.getString("tipo_identificador"));
            
            // Formatear fechas
            if (rs.getDate("fecha") != null) {
                datosPaciente.put("fecha_cita", sdf.format(rs.getDate("fecha")));
            }
            if (rs.getTime("hora") != null) {
                datosPaciente.put("hora_cita", rs.getTime("hora").toString());
            }
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error en la consulta SQL: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null, 
            "Error: Driver JDBC no encontrado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    System.out.println("*******************");
    System.out.println("Datos encontrados para enfermero: " + datosPaciente);
    return datosPaciente;
}
    

public static List<ConsultaPrevia> obtenerConsultasPrevias(String cedulaPaciente) {
    List<ConsultaPrevia> consultas = new ArrayList<>();
    
    String sql = "SELECT c.id_cita, c.fecha, " +
                 "COALESCE(e.Nombre, 'Sin especialidad') AS especialidad, " +
                 "d.nombres + ' ' + d.apellidos AS nombre_doctor " +
                 "FROM Cita c " +
                 "INNER JOIN Doctor d ON c.id_doctor = d.cedula " +
                 "LEFT JOIN Especialidades e ON d.id_especialidad = e.IdEspecialidad " +
                 "WHERE c.id_paciente = ? AND c.fecha < GETDATE() " +
                 "ORDER BY c.fecha DESC";

    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, cedulaPaciente);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            consultas.add(new ConsultaPrevia(
                rs.getInt("id_cita"),
                rs.getDate("fecha"),
                rs.getString("especialidad"),
                rs.getString("nombre_doctor")
            ));
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al consultar consultas previas: " + e.getMessage(),
            "Error de base de datos",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null, 
            "Error: Driver JDBC no encontrado",
            "Error de configuración",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    return consultas;
}
public static List<Map<String, String>> obtenerEvolucion(String cedulaPaciente) {
    List<Map<String, String>> citas = new ArrayList<>();
   
    String sql = "SELECT " +
             "e.id_evolucion, " +
             "e.diagnostico, " +
             "e.pronostico, " +
             "e.evolucion, " +
             "e.tratamiento, " +
             "c.fecha, " +
             "c.hora, " +
             "d.nombres + ' ' + d.apellidos AS nombre_doctor, " +
             "ISNULL(es.Nombre, 'Sin especialidad') AS especialidad " +
             "FROM Evolucion e " +
             "INNER JOIN Cita c ON e.id_cita = c.id_cita " +
             "INNER JOIN Doctor d ON c.id_doctor = d.cedula " +
             "LEFT JOIN Especialidades es ON d.id_especialidad = es.IdEspecialidad " +
             "WHERE c.id_paciente = ? " +
             "ORDER BY c.fecha DESC, c.hora DESC, e.id_evolucion DESC";
    
    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, cedulaPaciente);
        ResultSet rs = pstmt.executeQuery();
        
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        
        while (rs.next()) {
            Map<String, String> cita = new HashMap<>();
            
            if (rs.getDate("fecha") != null) {
                cita.put("fecha", sdfFecha.format(rs.getDate("fecha")));
            }
            if (rs.getTime("hora") != null) {
                cita.put("hora", sdfHora.format(rs.getTime("hora")));
            }
            
            // Datos del doctor y evolución
            cita.put("doctor", rs.getString("nombre_doctor"));
            cita.put("especialidad", rs.getString("especialidad"));
            // CORRECCIÓN: Se añaden todos los campos recuperados
            cita.put("diagnostico", rs.getString("diagnostico"));
            cita.put("pronostico", rs.getString("pronostico"));
            cita.put("evolucion", rs.getString("evolucion"));
            cita.put("tratamiento", rs.getString("tratamiento"));
            
            citas.add(cita);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al obtener historial de evolución: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    System.out.println("aaaaaaaaaaaaaaaaaa");
    System.out.println(citas);
    return citas;

}
public static Map<String, String> obtenerDatosContacto(String cedulaPaciente) {
    Map<String, String> datosContacto = new HashMap<>();
    
    String sql = "SELECT c.cedula, c.nombres, c.apellidos, c.telefono " +
                 "FROM Contacto c, Paciente p " +
                 "WHERE p.id_contacto = c.cedula AND p.cedula = ?";
    
    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, cedulaPaciente);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            datosContacto.put("cedula", rs.getString("cedula"));
            datosContacto.put("nombres", rs.getString("nombres"));
            datosContacto.put("apellidos", rs.getString("apellidos"));
            datosContacto.put("telefono", rs.getString("telefono"));
            
        }
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    
    return datosContacto;
}

public static Map<String, String> obtenerAnamnesis(String idPaciente) {
    Map<String, String> datosAnamnesis = new HashMap<>();
    String sql = "SELECT a.*, c.fecha, c.hora,c.motivo " +
             "FROM Cita c " +
             "JOIN Anamnesis a ON c.id_anamnesis = a.id_anamnesis " +  
             "WHERE c.id_paciente = ? " +
             "ORDER BY c.fecha DESC, c.hora DESC";  
    
    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, idPaciente);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            
            // Datos de la cita
            if (rs.getDate("fecha") != null) {
                datosAnamnesis.put("fecha_cita", sdf.format(rs.getDate("fecha")));
            }
            if (rs.getTime("hora") != null) {
                datosAnamnesis.put("hora_cita", timeFormat.format(rs.getTime("hora")));
            }
            
            // Datos físicos del paciente
            datosAnamnesis.put("estatura", rs.getString("estatura"));
            datosAnamnesis.put("presion_sistolica", rs.getString("presion_sistolica"));
            datosAnamnesis.put("presion_diastolica", rs.getString("presion_diastolica"));
            datosAnamnesis.put("peso", rs.getString("peso"));
            datosAnamnesis.put("frecuencia_cardiaca", rs.getString("frecuencia_cardiaca"));
            datosAnamnesis.put("examen_fisico", rs.getString("examen_fisico"));
            datosAnamnesis.put("temperatura", rs.getString("temperatura"));
            datosAnamnesis.put("oxigenacion", rs.getString("oxigenacion"));
            datosAnamnesis.put("motivo", rs.getString("motivo"));            
            datosAnamnesis.put("id_anamnesis", rs.getString("id_anamnesis"));
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error en la consulta SQL: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null, 
            "Error: Driver JDBC no encontrado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    System.out.println("Datos de anamnesis encontrados: " + datosAnamnesis);
    return datosAnamnesis;
}



}