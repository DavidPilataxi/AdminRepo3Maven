package adminmodule.dao;

import adminmodule.modelo.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                        rs.getString("id_antecedetes"),
                        rs.getString("estado_civil"),
                        rs.getString("telefono"),
                        rs.getString("sangre"),
                        rs.getString("tipo_identificador")
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
        String sql = "INSERT INTO paciente (cedula, nombres, apellidos, fecha_nacimiento, sexo, correo, contrasena_paciente, alergias, estado_civil, telefono, sangre, tipo_identificador, id_antecedetes, rol) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            ps.setString(9, paciente.getEstadoCivil());
            ps.setString(10, paciente.getTelefono());
            ps.setString(11, paciente.getTipoSangre());
            ps.setString(12, paciente.getTipoIdentificador());

            // Convertimos a entero si hay ID de antecedentes
            if (paciente.getIdAntecedentes() != null && !paciente.getIdAntecedentes().isEmpty()) {
                ps.setInt(13, Integer.parseInt(paciente.getIdAntecedentes()));
            } else {
                ps.setInt(13, 3);  // En lugar de setNull, envía 0
            }

            ps.setString(14, paciente.getRol());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar paciente: " + e.getMessage());
        }
    }


    @Override
    public boolean actualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET nombres=?, apellidos=?, fecha_nacimiento=?, sexo=?, correo=?, alergias=?, id_antecedetes=? WHERE cedula=?";

        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNombres());
            ps.setString(2, paciente.getApellidos());
            ps.setDate(3, new java.sql.Date(paciente.getFechaNacimiento().getTime()));
            ps.setString(4, paciente.getSexo());
            ps.setString(5, paciente.getCorreo());
            ps.setString(6, paciente.getAlergias());

            if (paciente.getIdAntecedentes() != null && !paciente.getIdAntecedentes().isEmpty()) {
                ps.setString(7, paciente.getIdAntecedentes());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }

            ps.setString(8, paciente.getCedula());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar paciente: " + e.getMessage());
        }
    }

    public List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";

        try (Connection conn = ConexionSQL.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paciente paciente = new Paciente(
                    rs.getString("cedula"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getDate("fecha_nacimiento"),
                    rs.getString("sexo"),
                    rs.getString("correo"),
                    rs.getString("contrasena_paciente"),
                    rs.getString("rol"),
                    rs.getString("alergias"),
                    rs.getString("id_antecedetes"),
                    rs.getString("estado_civil"),
                    rs.getString("telefono"),
                    rs.getString("sangre"),
                    rs.getString("tipo_identificador")
                );
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    public boolean eliminar(String cedula) throws SQLException {
        String sql = "DELETE FROM paciente WHERE cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            return ps.executeUpdate() > 0;
        }
    }

    // Métodos auxiliares para tabla Antecedente (simulación)

    public boolean existeAntecedente(int idAntecedentes) throws SQLException {
        String sql = "SELECT 1 FROM Antecedente WHERE idAntecedente = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAntecedentes);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean crearAntecedente(int idAntecedentes) throws SQLException {
        String sql = "INSERT INTO Antecedente(idAntecedente) VALUES(?)";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAntecedentes);
            return stmt.executeUpdate() > 0;
        }
    }
}
