package adminmodule.vista;

import adminmodule.dao.BodegueroDAO;
import adminmodule.modelo.Bodeguero;
import javax.swing.*;
import java.awt.*;

public class BodegaLoginWindow extends JFrame {
    private JTextField txtCedula;
    private JPasswordField txtPassword;

    public BodegaLoginWindow() {
        setTitle("Inicio de Sesión - Módulo de Bodega");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel para los componentes (centrado)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setPreferredSize(new Dimension(400, 350));
        centerPanel.setBackground(new Color(240, 240, 240));
        
        JLabel lblTitulo = new JLabel("BODEGA - INICIO DE SESIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBounds(0, 20, 400, 30);
        centerPanel.add(lblTitulo);

        // Campos de login
        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setBounds(50, 80, 100, 25);
        centerPanel.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(150, 80, 200, 25);
        centerPanel.add(txtCedula);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 130, 100, 25);
        centerPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 130, 200, 25);
        centerPanel.add(txtPassword);

        // Botón Login
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(100, 180, 200, 30);
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.addActionListener(e -> loginBodeguero());
        centerPanel.add(btnLogin);

        

        // Botón Volver
        JButton btnVolver = new JButton("Volver al Sistema Principal");
        btnVolver.setBounds(100, 280, 200, 30);
        btnVolver.setBackground(new Color(220, 60, 60));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.addActionListener(e -> {
            new HomeWindow().setVisible(true);
            dispose();
        });
        centerPanel.add(btnVolver);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        
        // Ajustar tamaño y mostrar
        pack();
        setMinimumSize(new Dimension(500, 450)); // Tamaño mínimo garantizado
        setVisible(true);
    }

    private void loginBodeguero() {
        String cedula = txtCedula.getText().trim();
        String contrasena = new String(txtPassword.getPassword()).trim();

        if (cedula.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese cédula y contraseña", 
                "Campos vacíos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            BodegueroDAO bodegueroDAO = new BodegueroDAO();
            Bodeguero bodeguero = bodegueroDAO.obtenerPorCedula(cedula);

            if (bodeguero != null && bodeguero.getContrasena().equals(contrasena)) {
                JOptionPane.showMessageDialog(this, 
                    "Bienvenido bodeguero: " + bodeguero.getNombres(), 
                    "Acceso concedido", 
                    JOptionPane.INFORMATION_MESSAGE);
                new BodegaMainWindow(bodeguero).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Credenciales incorrectas o bodeguero no registrado", 
                    "Acceso denegado", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al conectar con la base de datos: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}