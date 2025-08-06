package adminmodule.vista;

import adminmodule.modelo.Bodeguero;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BodegaMainWindow extends JFrame {
    public BodegaMainWindow(Bodeguero bodeguero) {
        setTitle("Módulo de Bodega - Sistema Hospitalario");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Información del usuario
        JLabel lblUsuario = new JLabel("Bodeguero: " + bodeguero.getNombres() + " " + bodeguero.getApellidos());
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(lblUsuario, BorderLayout.WEST);
        
        // Botón de logout
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> {
            new BodegaLoginWindow().setVisible(true);
            dispose();
        });
        topPanel.add(btnLogout, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Botones de funcionalidades
        centerPanel.add(createMenuButton("Gestión de Inventario", new Color(52, 152, 219)));
        centerPanel.add(createMenuButton("Gestión de Pedidos", new Color(46, 204, 113)));
        centerPanel.add(createMenuButton("Generar Reportes", new Color(155, 89, 182)));
        centerPanel.add(createMenuButton("Configuración", new Color(241, 196, 15)));

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 100));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
}