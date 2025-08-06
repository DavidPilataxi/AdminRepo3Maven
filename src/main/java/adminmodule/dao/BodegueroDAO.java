package adminmodule.dao;

import adminmodule.modelo.Bodeguero;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BodegueroDAO {
    public boolean existe(String cedula) {
        String sql = "SELECT Cedula FROM Bodeguero WHERE Cedula = ?";
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

    public Bodeguero obtenerPorCedula(String cedula) {
        String sql = "SELECT * FROM Bodeguero WHERE Cedula = ?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Bodeguero(
                        rs.getString("Cedula"),
                        rs.getString("Nombres"),
                        rs.getString("Apellidos"),
                        rs.getString("Contrasena")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean guardar(Bodeguero bodeguero) {
        String sql = "INSERT INTO Bodeguero (Cedula, Nombres, Apellidos, Contrasena) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bodeguero.getCedula());
            ps.setString(2, bodeguero.getNombres());
            ps.setString(3, bodeguero.getApellidos());
            ps.setString(4, bodeguero.getContrasena());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Bodeguero bodeguero) {
        String sql = "UPDATE Bodeguero SET Nombres=?, Apellidos=?, Contrasena=? WHERE Cedula=?";
        try (Connection conn = ConexionSQL.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bodeguero.getNombres());
            ps.setString(2, bodeguero.getApellidos());
            ps.setString(3, bodeguero.getContrasena());
            ps.setString(4, bodeguero.getCedula());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Bodeguero> obtenerTodos() {
        List<Bodeguero> bodegueros = new ArrayList<>();
        String sql = "SELECT * FROM Bodeguero";
        try (Connection conn = ConexionSQL.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bodegueros.add(new Bodeguero(
                    rs.getString("Cedula"),
                    rs.getString("Nombres"),
                    rs.getString("Apellidos"),
                    rs.getString("Contrasena")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bodegueros;
    }
}