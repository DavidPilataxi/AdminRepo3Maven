package adminmodule.vista;

import adminmodule.dao.ConexionSQL;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginAdminWindow extends JFrame {

    public LoginAdminWindow() {
        setTitle("Login Administrador");
        setSize(450, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Inicio de Sesión - Administrador");
        lblTitulo.setBounds(70, 20, 300, 30);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitulo);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(50, 70, 100, 25);
        add(lblCedula);

        JTextField txtCedula = new JTextField();
        txtCedula.setBounds(150, 70, 200, 25);
        add(txtCedula);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 110, 100, 25);
        add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 110, 200, 25);
        add(txtPassword);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(150, 160, 200, 30);
        btnLogin.setBackground(new Color(100, 149, 237));
        btnLogin.setForeground(Color.WHITE);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String cedula = txtCedula.getText().trim();
            String contrasena = new String(txtPassword.getPassword()).trim();

            if (cedula.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese cédula y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = ConexionSQL.conectar()) {
                String sql = "SELECT * FROM Administrador WHERE cedula = ? AND contrasena_admin = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, cedula);
                ps.setString(2, contrasena);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String rol = rs.getString("rol");
                    if ("admin".equalsIgnoreCase(rol)) {
                        JOptionPane.showMessageDialog(this, "Bienvenido, administrador " + rs.getString("nombres"), "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
                        new AdminWindow2().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "El usuario no tiene rol de administrador.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error de conexión:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginAdminWindow().setVisible(true);
        });
    }
}
