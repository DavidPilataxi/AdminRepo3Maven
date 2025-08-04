
package adminmodule.dao;

import adminmodule.modelo.Administrador;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO implements UserDAO<Administrador> {
    
    @Override
    public boolean existe(String cedula) {
        String sql = "SELECT cedula FROM administrador WHERE cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Administrador obtenerPorCedula(String cedula) {
        String sql = "SELECT * FROM administrador WHERE cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Administrador(
                        rs.getString("cedula"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("sexo"),
                        rs.getString("correo"),
                        rs.getString("contrasena_admin"),
                        rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener administrador: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean guardar(Administrador admin) {
        String sql = "INSERT INTO administrador (cedula, nombres, apellidos, fecha_nacimiento, " +
                     "sexo, correo, contrasena_admin, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, admin.getCedula());
            ps.setString(2, admin.getNombres());
            ps.setString(3, admin.getApellidos());
            ps.setDate(4, new java.sql.Date(admin.getFechaNacimiento().getTime()));
            ps.setString(5, admin.getSexo());
            ps.setString(6, admin.getCorreo());
            ps.setString(7, admin.getContrasena());
            ps.setString(8, admin.getRol());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar administrador: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Administrador admin) {
        String sql = "UPDATE administrador SET nombres = ?, apellidos = ?, fecha_nacimiento = ?, " +
                     "sexo = ?, correo = ?, contrasena_admin = ? WHERE cedula = ?";

        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, admin.getNombres());
            ps.setString(2, admin.getApellidos());
            ps.setDate(3, new java.sql.Date(admin.getFechaNacimiento().getTime()));
            ps.setString(4, admin.getSexo());
            ps.setString(5, admin.getCorreo());
            ps.setString(6, admin.getContrasena());
            ps.setString(7, admin.getCedula());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar administrador: " + e.getMessage());
            return false;
        }
    }

    public List<Administrador> obtenerTodos() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM administrador";
        
        try (Connection conn = ConexionSQL.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new Administrador(
                    rs.getString("cedula"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getDate("fecha_nacimiento"),
                    rs.getString("sexo"),
                    rs.getString("correo"),
                    rs.getString("contrasena_admin"),
                    rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener administradores: " + e.getMessage());
        }
        return lista;
    }
}