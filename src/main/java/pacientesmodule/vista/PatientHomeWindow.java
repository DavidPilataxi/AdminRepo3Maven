/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacientesmodule.vista; // ¡Este es el paquete correcto!

import adminmodule.modelo.Paciente; // <--- ¡CAMBIO AQUÍ! Importa desde adminmodule.modelo
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box; // Necesario para Box.createRigidArea

/**
 * Esta es la ventana principal (JFrame) para el módulo de pacientes.
 * Actúa como un hub desde donde el paciente puede acceder a su información
 * personal y a los resultados de sus citas.
 *
 * @author TuNombre
 */
public class PatientHomeWindow extends JFrame {

    private static final Logger logger = Logger.getLogger(PatientHomeWindow.class.getName());
    private Paciente pacienteActual; // Almacenará el objeto Paciente completo

    // Componentes de la interfaz de usuario
    private JLabel lblBienvenida;
    private JButton btnVerInformacion;
    private JButton btnVerCitasYResultados; // Unificado para una mejor navegación
    private JButton btnCerrarSesion;

    /**
     * Crea un nuevo formulario PatientHomeWindow.
     * Este constructor recibe el objeto Paciente completo para cargar sus datos.
     *
     * @param paciente El objeto Paciente que ha iniciado sesión.
     */
    public PatientHomeWindow(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El objeto Paciente no puede ser nulo.");
        }
        this.pacienteActual = paciente;
        initComponents();
        this.setLocationRelativeTo(null);
        setTitle("Módulo de Pacientes - " + pacienteActual.getNombres() + " " + pacienteActual.getApellidos());
        lblBienvenida.setText("¡Bienvenido, " + pacienteActual.getNombres() + " " + pacienteActual.getApellidos() + "!");
    }

    /**
     * Este método inicializa y configura los componentes de la interfaz de usuario.
     * (Normalmente generado por un constructor de GUI como el de NetBeans).
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 350);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblBienvenida = new JLabel("¡Bienvenido al módulo de pacientes!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnVerInformacion = new JButton("Ver Mi Información Personal");
        btnVerInformacion.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVerInformacion.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnVerCitasYResultados = new JButton("Ver Citas y Resultados");
        btnVerCitasYResultados.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVerCitasYResultados.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCerrarSesion.setBackground(new Color(220, 50, 50));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(lblBienvenida);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnVerInformacion);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(btnVerCitasYResultados);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(btnCerrarSesion);

        add(mainPanel);

        btnVerInformacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnVerInformacionActionPerformed(evt);
            }
        });

        btnVerCitasYResultados.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnVerCitasYResultadosActionPerformed(evt);
            }
        });

        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
    }

    private void btnVerInformacionActionPerformed(ActionEvent evt) {
        // Asumiendo que informacion_paciente puede tomar la cédula del paciente o el objeto completo
        // Aquí pasamos la cédula del paciente, ya que es lo que informacion_paciente espera según códigos anteriores.
        informacion_paciente infoPaciente = new informacion_paciente(pacienteActual.getCedula());
        infoPaciente.setVisible(true);
        // this.setVisible(false); // Opcional
    }

    private void btnVerCitasYResultadosActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this,
            "Esta sección mostrará todas tus citas y te permitirá ver los resultados de cada una.",
            "Funcionalidad Pendiente",
            JOptionPane.INFORMATION_MESSAGE);
            
        // Si solo quieres abrir la ventana de resultados con un idCita de prueba (NO RECOMENDADO para producción):
        // int idCitaDePrueba = 123; // Reemplaza con un ID de cita real para pruebas si lo tienes.
        // resultados resultadosFrame = new resultados(idCitaDePrueba);
        // resultadosFrame.setVisible(true);
    }

    private void btnCerrarSesionActionPerformed(ActionEvent evt) {
        this.dispose(); // Cierra la ventana actual del módulo de paciente
        
        // Volver a la HomeWindow principal (la ventana de login general)
        // Asegúrate de que la clase HomeWindow esté en 'adminmodule.vista'
        adminmodule.vista.HomeWindow loginPrincipal = new adminmodule.vista.HomeWindow();
        loginPrincipal.setVisible(true);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, "Error al configurar el Look and Feel", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            // Para probar, crea un objeto Paciente ficticio usando la clase correcta
            Paciente pacienteDePrueba = new Paciente("123456789", "Juan", "Pérez", new java.util.Date(),
                                                     "Masculino", "juan@example.com", "pass123", "Paciente",
                                                     "Ninguna", "98%", "ANT001");

            new PatientHomeWindow(pacienteDePrueba).setVisible(true);
        });
    }
}