
package adminmodule.dao;

import adminmodule.modelo.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PacienteDAO implements UserDAO<Paciente> {
    @Override
    public boolean existe(String cedula) {
        String sql = "SELECT cedula FROM paciente WHERE cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Paciente obtenerPorCedula(String cedula) {
        String sql = "SELECT * FROM paciente WHERE cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                        rs.getString("cedula"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("sexo"),
                        rs.getString("correo"),
                        rs.getString("contrasena_paciente"),
                        rs.getString("rol"),
                        rs.getString("alergias"),
                        rs.getString("oxigenacion"),
                        rs.getString("id_antecedetes")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean guardar(Paciente paciente) {
        String sql = "INSERT INTO paciente (cedula, nombres, apellidos, fecha_nacimiento, " +
                    "sexo, correo, contrasena_paciente, alergias, oxigenacion, id_antecedetes, rol) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paciente.getCedula());
            ps.setString(2, paciente.getNombres());
            ps.setString(3, paciente.getApellidos());
            ps.setDate(4, new java.sql.Date(paciente.getFechaNacimiento().getTime()));
            ps.setString(5, paciente.getSexo());
            ps.setString(6, paciente.getCorreo());
            ps.setString(7, paciente.getContrasena());
            ps.setString(8, paciente.getAlergias());
            ps.setString(9, paciente.getOxigenacion());
            ps.setString(10, paciente.getIdAntecedentes());
            ps.setString(11, paciente.getRol());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET nombres=?, apellidos=?, fecha_nacimiento=?, " +
                    "sexo=?, correo=?, alergias=?, oxigenacion=?, id_antecedetes=? " +
                    "WHERE cedula=?";

        try (Connection conn = ConexionSQL.conectar()) {
            conn.setAutoCommit(false); // Iniciamos transacción

            // 1. Validar antecedente si se proporciona
            Integer idAntecedentes = null;
            if (paciente.getIdAntecedentes() != null && !paciente.getIdAntecedentes().isEmpty()) {
                try {
                    idAntecedentes = Integer.parseInt(paciente.getIdAntecedentes());

                    // Verificar existencia del antecedente
                    if (!existeEnTabla(conn, "Antecedentes", "id_antecedentes", idAntecedentes)) {
                        conn.rollback();
                        throw new SQLException("El ID de antecedentes " + idAntecedentes + " no existe");
                    }
                } catch (NumberFormatException e) {
                    conn.rollback();
                    throw new SQLException("El ID de antecedentes debe ser un número válido");
                }
            }

            // 2. Ejecutar actualización
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, paciente.getNombres());
                ps.setString(2, paciente.getApellidos());
                ps.setDate(3, new java.sql.Date(paciente.getFechaNacimiento().getTime()));
                ps.setString(4, paciente.getSexo());
                ps.setString(5, paciente.getCorreo());
                ps.setString(6, paciente.getAlergias());
                ps.setString(7, paciente.getOxigenacion());

                if (idAntecedentes != null) {
                    ps.setInt(8, idAntecedentes);
                } else {
                    ps.setNull(8, Types.INTEGER);
                }

                ps.setString(9, paciente.getCedula());

                int result = ps.executeUpdate();
                conn.commit();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar paciente: " + e.getMessage());
        }
    }

    private boolean existeEnTabla(Connection conn, String tabla, String columna, Object valor) throws SQLException {
        String sql = String.format("SELECT 1 FROM %s WHERE %s = ?", tabla, columna);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (valor instanceof Integer) {
                ps.setInt(1, (Integer) valor);
            } else {
                ps.setString(1, valor.toString());
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public boolean existeAntecedente(int idAntecedentes) throws SQLException {
        String sql = "SELECT 1 FROM Antecedentes WHERE id_antecedentes = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAntecedentes);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean crearAntecedente(int idAntecedentes) throws SQLException {
        String sql = "INSERT INTO Antecedentes (id_antecedentes) VALUES (?)";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAntecedentes);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean eliminar(String cedula) throws SQLException {
    Connection conn = null;
    try {
        conn = ConexionSQL.conectar();
        conn.setAutoCommit(false);  // Iniciar transacción
        
        // 1. Primero verificar si existe el paciente
        String sqlSelect = "SELECT 1 FROM paciente WHERE cedula = ?";
        try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
            psSelect.setString(1, cedula);
            if (!psSelect.executeQuery().next()) {
                throw new SQLException("No existe un paciente con esa cédula");
            }
        }
        
        // 2. Proceder con la eliminación
        String sqlDelete = "DELETE FROM paciente WHERE cedula = ?";
        try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
            psDelete.setString(1, cedula);
            int affectedRows = psDelete.executeUpdate();
            
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }
            
            conn.commit();
            return true;
        }
    } catch (SQLException e) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ex) {}
        }
        // Personalizar mensaje de error
        if (e.getMessage().contains("id_antecedentes")) {
            throw new SQLException("Error en la estructura de la base de datos", e);
        }
        throw e;
    } finally {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) {}
        }
    
    }
}


    public List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";
        
        try (Connection conn = ConexionSQL.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pacientes.add(new Paciente(
                    rs.getString("cedula"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getDate("fecha_nacimiento"),
                    rs.getString("sexo"),
                    rs.getString("correo"),
                    rs.getString("contrasena_paciente"),
                    rs.getString("rol"),
                    rs.getString("alergias"),
                    rs.getString("oxigenacion"),
                    rs.getString("id_antecedetes")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacientes;
    }
    
    
}