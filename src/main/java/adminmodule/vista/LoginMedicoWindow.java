package adminmodule.vista;

import adminmodule.dao.ConexionSQL;
import adminmodule.vista.HomeWindow;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginMedicoWindow extends JFrame {

    public LoginMedicoWindow() {
        setTitle("Sistema Hospitalario Médico - Médicos");
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
        btnLogin.setBackground(new Color(34, 139, 34)); // Verde para médicos
        btnLogin.setForeground(Color.WHITE);
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

                // Login Doctor - Reutilizando tu lógica exacta
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

                // Si no coincide
                JOptionPane.showMessageDialog(this, "Credenciales inválidas o doctor no registrado.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos:\n" + ex.getMessage(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        
        JLabel lblIrPaciente = new JLabel("Ir a login pacientes");
        lblIrPaciente.setBounds(140, 280, 250, 30);
        lblIrPaciente.setForeground(new Color(0, 102, 204));
        lblIrPaciente.setFont(new Font("Arial", Font.BOLD, 12));
        lblIrPaciente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(lblIrPaciente);

        lblIrPaciente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new HomeWindow().setVisible(true);
                dispose();
            }
        });

        JLabel lblIrAdmin = new JLabel("Módulos Sistema");
        lblIrAdmin.setBounds(135, 310, 250, 30);
        lblIrAdmin.setForeground(new Color(0, 102, 204));
        lblIrAdmin.setFont(new Font("Arial", Font.BOLD, 12));
        lblIrAdmin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(lblIrAdmin);

        lblIrAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
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

            new LoginMedicoWindow().setVisible(true);
        });
    }
}