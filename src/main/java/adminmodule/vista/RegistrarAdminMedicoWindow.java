package adminmodule.vista;

import adminmodule.dao.ConexionSQL;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrarAdminMedicoWindow extends JFrame {

    public RegistrarAdminMedicoWindow() {
        setTitle("Registrar Nuevo Administrador Médico");
        setSize(400, 380);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Formulario Registro Administrador Médico");
        lblTitulo.setBounds(30, 20, 340, 30);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTitulo);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(30, 70, 100, 25);
        add(lblCedula);
        JTextField txtCedula = new JTextField();
        txtCedula.setBounds(150, 70, 200, 25);
        add(txtCedula);

        JLabel lblNombres = new JLabel("Nombres:");
        lblNombres.setBounds(30, 110, 100, 25);
        add(lblNombres);
        JTextField txtNombres = new JTextField();
        txtNombres.setBounds(150, 110, 200, 25);
        add(txtNombres);

        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setBounds(30, 150, 100, 25);
        add(lblApellidos);
        JTextField txtApellidos = new JTextField();
        txtApellidos.setBounds(150, 150, 200, 25);
        add(txtApellidos);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(30, 190, 100, 25);
        add(lblCorreo);
        JTextField txtCorreo = new JTextField();
        txtCorreo.setBounds(150, 190, 200, 25);
        add(txtCorreo);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(30, 230, 100, 25);
        add(lblContrasena);
        JPasswordField txtContrasena = new JPasswordField();
        txtContrasena.setBounds(150, 230, 200, 25);
        add(txtContrasena);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(150, 280, 120, 35);
        add(btnRegistrar);

        btnRegistrar.addActionListener(e -> {
            String cedula = txtCedula.getText().trim();
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();

            if (cedula.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = ConexionSQL.conectar()) {
                String sql = "INSERT INTO AdministradorMedico (cedula, nombres, apellidos, correo, contrasena_admin_medico) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, cedula);
                ps.setString(2, nombres);
                ps.setString(3, apellidos);
                ps.setString(4, correo.isEmpty() ? null : correo);
                ps.setString(5, contrasena);

                int filas = ps.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(this, "Administrador Médico registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar el administrador médico.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al registrar administrador médico:\n" + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
