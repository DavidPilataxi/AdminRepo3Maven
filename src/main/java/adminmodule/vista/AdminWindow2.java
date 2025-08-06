package adminmodule.vista;

import javax.swing.*;
import java.awt.*;

public class AdminWindow2 extends JFrame {

    public AdminWindow2() {
        setTitle("Panel del Administrador");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Panel del Módulo Administrativo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        JButton btnGestionarPacientes = new JButton("Gestionar Pacientes");
        JButton btnRegistrarAdmin = new JButton("Registrar Administrador");
        JButton btnRegistrarBodeguero = new JButton("Registrar Bodeguero");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnGestionarPacientes);
        panelBotones.add(btnRegistrarAdmin);
        panelBotones.add(btnRegistrarBodeguero);
        panelBotones.add(btnCerrarSesion);

        add(panelBotones, BorderLayout.CENTER);

        // Acciones
        btnGestionarPacientes.addActionListener(e -> new GestionPacientesWindow().setVisible(true));
        btnRegistrarAdmin.addActionListener(e -> new RegistrarAdministradorWindow().setVisible(true));
        btnRegistrarBodeguero.addActionListener(e -> new RegistrarBodegueroWindow().setVisible(true));
        btnCerrarSesion.addActionListener(e -> {
            new HomeWindow().setVisible(true);
            dispose();
        });
    }
}
