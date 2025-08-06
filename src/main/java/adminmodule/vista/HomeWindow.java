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
        setTitle("Inicio de Sesión - Pacientes");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        add(mainPanel, BorderLayout.CENTER);

        // Panel izquierdo
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(230, 230, 230));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("Sistema Hospitalario Médico");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(50, 50, 50));
        leftPanel.add(titulo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel subtitulo = new JLabel("Inicio de sesión para pacientes");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(80, 80, 80));
        leftPanel.add(subtitulo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel imagePlaceholder = new JLabel(".png");
        imagePlaceholder.setPreferredSize(new Dimension(300, 300));
        imagePlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        imagePlaceholder.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        leftPanel.add(imagePlaceholder);

        mainPanel.add(leftPanel);

        // Panel derecho - CENTRADO
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 40, 40));

        // Espacio superior para centrar verticalmente
        rightPanel.add(Box.createVerticalGlue());

        // Panel del formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(300, 200));

        // Campo cédula
        JPanel cedulaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        cedulaPanel.setOpaque(false);
        JLabel lblCedula = new JLabel("Cédula del paciente:");
        JTextField txtCedula = new JTextField(15);
        cedulaPanel.add(lblCedula);
        cedulaPanel.add(txtCedula);
        formPanel.add(cedulaPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Campo contraseña
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordPanel.setOpaque(false);
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setPreferredSize(lblCedula.getPreferredSize()); // Mismo ancho que el label de cédula
        JPasswordField txtPassword = new JPasswordField(15);
        passwordPanel.add(lblPassword);
        passwordPanel.add(txtPassword);
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón de login
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setMaximumSize(new Dimension(200, 35));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(btnLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Link de registro
        JLabel lblRegistrar = new JLabel("Registrarse como paciente");
        lblRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegistrar.setForeground(new Color(0, 102, 204));
        lblRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        lblRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        formPanel.add(lblRegistrar);

        rightPanel.add(formPanel);
        rightPanel.add(Box.createVerticalGlue());

        // Panel inferior para links de médico y admin
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomPanel.setOpaque(false);

        JLabel lblMedico = new JLabel("Ingresar como médico");
        lblMedico.setForeground(new Color(0, 102, 204));
        lblMedico.setFont(new Font("Arial", Font.BOLD, 12));
        lblMedico.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bottomPanel.add(lblMedico);

        JLabel lblAdmin = new JLabel("Módulos del Sistema");
        lblAdmin.setForeground(new Color(0, 102, 204));
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 12));
        lblAdmin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bottomPanel.add(lblAdmin);

        rightPanel.add(bottomPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Event listeners
        btnLogin.addActionListener(e -> {
            String cedula = txtCedula.getText().trim();
            String contrasena = new String(txtPassword.getPassword()).trim();

            if (cedula.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese cédula y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = ConexionSQL.conectar()) {
                String sqlPaciente = "SELECT * FROM Paciente WHERE cedula = ? AND contrasena_paciente = ?";
                PreparedStatement psPaciente = conn.prepareStatement(sqlPaciente);
                psPaciente.setString(1, cedula);
                psPaciente.setString(2, contrasena);
                ResultSet rsPaciente = psPaciente.executeQuery();

                if (rsPaciente.next()) {
                    JOptionPane.showMessageDialog(this, "Bienvenido paciente: " + rsPaciente.getString("nombres"), "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);

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
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                }

                rsPaciente.close();
                psPaciente.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos:\n" + ex.getMessage(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
            }
        });

        lblRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RegisterWindow().setVisible(true);
                dispose();
            }
        });

        lblMedico.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        new LoginMedicoWindow().setVisible(true);
        dispose();
    }
});

        lblAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new AdminWindow().setVisible(true);
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