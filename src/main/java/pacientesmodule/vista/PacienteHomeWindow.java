package pacientesmodule.vista;

import adminmodule.modelo.Paciente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PacienteHomeWindow extends JFrame {

    private static final Logger logger = Logger.getLogger(PacienteHomeWindow.class.getName());
    private Paciente pacienteActual;

    private JLabel lblBienvenida;
    private JButton btnEditarInformacion;
    private JButton btnAgendarCita;
    private JButton btnHistorialMedico;
    private JButton btnCerrarSesion;

    public PacienteHomeWindow(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El objeto Paciente no puede ser nulo.");
        }
        this.pacienteActual = paciente;
        initComponents();
        this.setLocationRelativeTo(null);
        setTitle("Módulo de Pacientes - " + pacienteActual.getNombres() + " " + pacienteActual.getApellidos());
        lblBienvenida.setText("¡Bienvenido, " + pacienteActual.getNombres() + " " + pacienteActual.getApellidos() + "!");
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500); // Misma medida que HomeWindow

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        lblBienvenida = new JLabel("¡Bienvenido al módulo de pacientes!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEditarInformacion = new JButton("Editar Información del Paciente");
        btnEditarInformacion.setFont(new Font("Arial", Font.PLAIN, 16));
        btnEditarInformacion.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnAgendarCita = new JButton("Agendar Cita");
        btnAgendarCita.setFont(new Font("Arial", Font.PLAIN, 16));
        btnAgendarCita.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnHistorialMedico = new JButton("Historial Médico");
        btnHistorialMedico.setFont(new Font("Arial", Font.PLAIN, 16));
        btnHistorialMedico.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setPreferredSize(new Dimension(200, 35));
        btnCerrarSesion.setMaximumSize(new Dimension(200, 35)); // asegura tamaño consistente
        btnCerrarSesion.setBackground(new Color(70, 130, 180)); // azul similar al de login
        btnCerrarSesion.setForeground(Color.BLACK);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar componentes al panel principal
        mainPanel.add(lblBienvenida);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(btnEditarInformacion);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(btnAgendarCita);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(btnHistorialMedico);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(btnCerrarSesion);

        add(mainPanel);

        // Solo el botón de cerrar sesión tiene funcionalidad por ahora
        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
    }

    private void btnCerrarSesionActionPerformed(ActionEvent evt) {
        this.dispose();
        new adminmodule.vista.HomeWindow().setVisible(true);
    }
}
