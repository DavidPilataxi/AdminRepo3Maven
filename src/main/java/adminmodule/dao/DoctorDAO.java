
package adminmodule.dao;

import adminmodule.modelo.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO implements UserDAO<Doctor> {
    @Override
    public boolean existe(String cedula) {
        String sql = "SELECT cedula FROM doctor WHERE cedula = ?";
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
    public Doctor obtenerPorCedula(String cedula) {
        String sql = "SELECT * FROM doctor WHERE cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Doctor(
                        rs.getString("cedula"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("sexo"),
                        rs.getString("correo"),
                        rs.getString("contrasena_doctor"),
                        rs.getString("rol"),
                        rs.getString("especialidad")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean guardar(Doctor doctor) {
        String sql = "INSERT INTO doctor (cedula, nombres, apellidos, fecha_nacimiento, " +
                     "sexo, correo, contrasena_doctor, rol, especialidad) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getCedula());
            ps.setString(2, doctor.getNombres());
            ps.setString(3, doctor.getApellidos());
            ps.setDate(4, new java.sql.Date(doctor.getFechaNacimiento().getTime()));
            ps.setString(5, doctor.getSexo());
            ps.setString(6, doctor.getCorreo());
            ps.setString(7, doctor.getContrasena());
            ps.setString(8, doctor.getRol());
            ps.setString(9, doctor.getEspecialidad());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Doctor doctor) {
        String sql = "UPDATE doctor SET nombres = ?, apellidos = ?, fecha_nacimiento = ?, " +
                     "sexo = ?, correo = ?, contrasena_doctor = ?, especialidad = ? " +
                     "WHERE cedula = ?";

        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getNombres());
            ps.setString(2, doctor.getApellidos());
            ps.setDate(3, new java.sql.Date(doctor.getFechaNacimiento().getTime()));
            ps.setString(4, doctor.getSexo());
            ps.setString(5, doctor.getCorreo());
            ps.setString(6, doctor.getContrasena());
            ps.setString(7, doctor.getEspecialidad());
            ps.setString(8, doctor.getCedula());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminar(String cedula) {
        String sql = "DELETE FROM doctor WHERE cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Doctor> obtenerTodos() {
        List<Doctor> doctores = new ArrayList<>();
        String sql = "SELECT * FROM doctor";
        
        try (Connection conn = ConexionSQL.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctores.add(new Doctor(
                    rs.getString("cedula"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getDate("fecha_nacimiento"),
                    rs.getString("sexo"),
                    rs.getString("correo"),
                    rs.getString("contrasena_doctor"),
                    rs.getString("rol"),
                    rs.getString("especialidad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctores;
    }
}