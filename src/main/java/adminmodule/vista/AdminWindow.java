package adminmodule.vista;

import javax.swing.*;
import java.awt.*;
import medicosmodule.vistas.Principal;

public class AdminWindow extends JFrame {

    public AdminWindow() {
        setTitle("Panel del Administrador");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Encabezado
        JLabel titulo = new JLabel("Panel de Administración del Sistema Médico", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 10, 10)); // Solo 3 botones ahora
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnModuloAdministradores = new JButton("Módulo de Administradores");
        JButton btnModuloMedicos = new JButton("Módulo Médicos y Especialidades");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnModuloAdministradores);
        panelBotones.add(btnModuloMedicos);
        panelBotones.add(btnCerrarSesion);

        add(panelBotones, BorderLayout.CENTER);

        // Acción para abrir login de administrador
        btnModuloAdministradores.addActionListener(e -> {
            new LoginAdminWindow().setVisible(true);
            // dispose(); // si quieres cerrar esta ventana al abrir el login
        });

        // Acción para abrir módulo médico
        btnModuloMedicos.addActionListener(e -> {
            new Principal().setVisible(true);
            // dispose();
        });

        // Acción para cerrar sesión
        btnCerrarSesion.addActionListener(e -> {
            new HomeWindow().setVisible(true);
            dispose();
        });
    }
}
