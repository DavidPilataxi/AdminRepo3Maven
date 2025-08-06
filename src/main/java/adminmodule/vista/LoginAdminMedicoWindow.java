package adminmodule.vista;

import adminmodule.dao.ConexionSQL;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import medicosmodule.vistas.Principal;

public class LoginAdminMedicoWindow extends JFrame {

    public LoginAdminMedicoWindow() {
        setTitle("Login Administrador Médico");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(50, 60, 100, 25);
        add(lblCedula);

        JTextField txtCedula = new JTextField();
        txtCedula.setBounds(150, 60, 180, 25);
        add(txtCedula);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(50, 110, 100, 25);
        add(lblContrasena);

        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setBounds(150, 110, 180, 25);
        add(txtContrasena);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(150, 170, 180, 35);
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String cedula = txtCedula.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();

            if (cedula.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese cédula y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = ConexionSQL.conectar()) {
                String sql = "SELECT * FROM AdministradorMedico WHERE cedula = ? AND contrasena_admin_medico = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, cedula);
                ps.setString(2, contrasena);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Bienvenido administrador médico: " + rs.getString("nombres"), "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
                    // Aquí puedes abrir la ventana principal del módulo médico, por ejemplo:
                    new Principal().setVisible(true);  // Asegúrate de que Principal es la ventana correcta para módulo médicos
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                ps.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
