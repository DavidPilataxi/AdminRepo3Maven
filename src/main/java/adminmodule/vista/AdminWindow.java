package adminmodule.vista;

import javax.swing.*;
import java.awt.*;
import medicosmodule.vistas.Principal; // Asegúrate que este es el nuevo paquete tras mover la clase

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
        panelBotones.setLayout(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnModuloMedicos = new JButton("Módulo Médicos y Especialidades");
        JButton btnVerMedicos = new JButton("Ver Lista de Médicos");
        JButton btnVerPacientes = new JButton("Ver Lista de Pacientes");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        JButton btnGestionarPacientes = new JButton("Gestionar Pacientes");

        panelBotones.add(btnGestionarPacientes);
        panelBotones.add(btnModuloMedicos);
        panelBotones.add(btnVerMedicos);
        panelBotones.add(btnVerPacientes);
        panelBotones.add(btnCerrarSesion);

        add(panelBotones, BorderLayout.CENTER);

        // Acción para abrir módulo médico
        btnModuloMedicos.addActionListener(e -> {
            new Principal().setVisible(true);
            // Si quieres cerrar AdminWindow cuando se abre el módulo:
            // dispose();
        });

        // Acción para gestionar pacientes
        btnGestionarPacientes.addActionListener(e -> {
            new GestionPacientesWindow().setVisible(true);
        });

        // Acción para cerrar sesión
        btnCerrarSesion.addActionListener(e -> {
            new HomeWindow().setVisible(true);
            dispose();
        });
    }
}
