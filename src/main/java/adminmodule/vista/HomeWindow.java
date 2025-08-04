package adminmodule.vista;

import adminmodule.dao.ConexionSQL;
import adminmodule.modelo.Paciente;
import pacientesmodule.vista.PatientHomeWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeWindow extends JFrame {

    public HomeWindow() {
        setTitle("Sistema Hospitalario Médico");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        add(mainPanel, BorderLayout.CENTER);

        // Panel izquierdo (informativo)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(230, 230, 230));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("SISTEMA HOSPITALARIO MÉDICO");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(50, 50, 50));
        leftPanel.add(titulo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel imagePlaceholder = new JLabel(".png");
        imagePlaceholder.setPreferredSize(new Dimension(300, 300));
        imagePlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        imagePlaceholder.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        leftPanel.add(imagePlaceholder);

        mainPanel.add(leftPanel);

        // Panel derecho (formulario login)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBackground(new Color(245, 245, 245));

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(50, 80, 100, 30);
        rightPanel.add(lblCedula);

        JTextField txtCedula = new JTextField();
        txtCedula.setBounds(150, 80, 200, 30);
        rightPanel.add(txtCedula);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 130, 100, 30);
        rightPanel.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 130, 200, 30);
        rightPanel.add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(150, 190, 200, 35);
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String cedula = txtCedula.getText().trim();
            String contrasena = new String(txtPassword.getPassword()).trim();

            if (cedula.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese cédula y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = ConexionSQL.conectar()) {

                // Login Administrador
                String sqlAdmin = "SELECT * FROM Administrador WHERE cedula = ? AND contrasena_admin = ?";
                PreparedStatement psAdmin = conn.prepareStatement(sqlAdmin);
                psAdmin.setString(1, cedula);
                psAdmin.setString(2, contrasena);
                ResultSet rsAdmin = psAdmin.executeQuery();

                if (rsAdmin.next()) {
                    JOptionPane.showMessageDialog(this, "Bienvenido administrador: " + rsAdmin.getString("nombres"), "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
                    new AdminWindow().setVisible(true);
                    dispose();
                    rsAdmin.close();
                    psAdmin.close();
                    return;
                }
                rsAdmin.close();
                psAdmin.close();

                // Login Doctor
                String sqlDoctor = "SELECT * FROM Doctor WHERE cedula = ? AND contrasena_doctor = ?";
                PreparedStatement psDoctor = conn.prepareStatement(sqlDoctor);
                psDoctor.setString(1, cedula);
                psDoctor.setString(2, contrasena);
                ResultSet rsDoctor = psDoctor.executeQuery();

                if (rsDoctor.next()) {
                    JOptionPane.showMessageDialog(this, "Bienvenido doctor: " + rsDoctor.getString("nombres"), "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);
                    new DoctorWindow().setVisible(true);
                    dispose();
                    rsDoctor.close();
                    psDoctor.close();
                    return;
                }
                rsDoctor.close();
                psDoctor.close();

                // Login Paciente
                String sqlPaciente = "SELECT * FROM Paciente WHERE cedula = ? AND contrasena_paciente = ?";
                PreparedStatement psPaciente = conn.prepareStatement(sqlPaciente);
                psPaciente.setString(1, cedula);
                psPaciente.setString(2, contrasena);
                ResultSet rsPaciente = psPaciente.executeQuery();

                if (rsPaciente.next()) {
                    JOptionPane.showMessageDialog(this, "Bienvenido paciente: " + rsPaciente.getString("nombres"), "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);

                    // Crear objeto Paciente con datos del ResultSet
                    Paciente paciente = new Paciente(
                        rsPaciente.getString("cedula"),
                        rsPaciente.getString("nombres"),
                        rsPaciente.getString("apellidos"),
                        rsPaciente.getDate("fecha_nacimiento"),
                        rsPaciente.getString("sexo"),
                        rsPaciente.getString("correo"),
                        rsPaciente.getString("contrasena_paciente"),
                        "Paciente",
                        rsPaciente.getString("alergias"),
                        rsPaciente.getString("oxigenacion"),
                        rsPaciente.getString("id_antecedetes")
                    );

                    new PatientHomeWindow(paciente).setVisible(true);
                    dispose();
                    rsPaciente.close();
                    psPaciente.close();
                    return;
                }
                rsPaciente.close();
                psPaciente.close();

                // Si ningún rol coincide
                JOptionPane.showMessageDialog(this, "Credenciales inválidas o usuario no registrado.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos:\n" + ex.getMessage(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
            }
        });

        JLabel lblRegistrar = new JLabel("¿No tienes cuenta? Regístrate");
        lblRegistrar.setBounds(165, 250, 300, 30);
        lblRegistrar.setForeground(new Color(0, 102, 204));
        lblRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        rightPanel.add(lblRegistrar);

        lblRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RegisterWindow().setVisible(true);
                dispose();
            }
        });

        mainPanel.add(rightPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new HomeWindow().setVisible(true);
        });
    }
}

