package adminmodule.vista;

import adminmodule.modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class PacientePanel extends JFrame {
    public PacientePanel(Usuario paciente) {
        setTitle("Panel del Paciente - " + paciente.getNombres() + " " + paciente.getApellidos());
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña de Información Personal
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        addInfoField(infoPanel, "Cédula:", paciente.getCedula());
        addInfoField(infoPanel, "Nombres:", paciente.getNombres());
        addInfoField(infoPanel, "Apellidos:", paciente.getApellidos());
        addInfoField(infoPanel, "Fecha Nacimiento:", 
                   new SimpleDateFormat("dd/MM/yyyy").format(paciente.getFechaNacimiento()));
        addInfoField(infoPanel, "Sexo:", paciente.getSexo());
        addInfoField(infoPanel, "Correo:", paciente.getCorreo());
        //addInfoField(infoPanel, "Alergias:", paciente.getAlergias());
        //addInfoField(infoPanel, "Nivel Oxigenación:", paciente.getOxigenacion());
        //addInfoField(infoPanel, "ID Antecedentes:", paciente.getIdAntecedentes());
        
        tabbedPane.addTab("Información Personal", infoPanel);
        
        // Pestaña de Historial Médico (puedes implementarla)
        tabbedPane.addTab("Historial Médico", new JPanel());
        
        add(tabbedPane);
    }
    
    private void addInfoField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label, SwingConstants.RIGHT));
        panel.add(new JTextField(value));
    }
    //asd
}


