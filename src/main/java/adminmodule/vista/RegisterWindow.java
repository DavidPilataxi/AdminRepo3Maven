package adminmodule.vista;

import adminmodule.util.*;
import adminmodule.dao.*;
import adminmodule.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Period;
import com.toedter.calendar.JDateChooser;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class RegisterWindow extends JFrame {
    private JTextField cedulaField, nombresField, apellidosField, correoField, usuarioPacienteField, alergiasField, telefonoField, sangreField, tipoIdentificadorField;
    private JPasswordField contrasenaField;
    private JComboBox<String> sexoComboBox, estadoCivilComboBox;
    private JDateChooser fechaNacimientoChooser;

    public RegisterWindow() {
        setTitle("Registro de Paciente");
        setSize(600, 650); // Ventana visible
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Título
        JLabel lblTitulo = new JLabel("Registro de Paciente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;

        int y = 1;

        addFormField(panel, "Cédula:", cedulaField = new JTextField(), y++);
        addFormField(panel, "Nombres:", nombresField = new JTextField(), y++);
        addFormField(panel, "Apellidos:", apellidosField = new JTextField(), y++);

        panel.add(new JLabel("Fecha Nacimiento:"), createGbc(0, y));
        fechaNacimientoChooser = new JDateChooser();
        fechaNacimientoChooser.setDateFormatString("dd/MM/yyyy");
        panel.add(fechaNacimientoChooser, createGbc(1, y++));
        
        addFormField(panel, "Sexo:", sexoComboBox = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"}), y++);
        addFormField(panel, "Correo:", correoField = new JTextField(), y++);
        addFormField(panel, "Usuario:", usuarioPacienteField = new JTextField(), y++);
        addFormField(panel, "Contraseña:", contrasenaField = new JPasswordField(), y++);
        addFormField(panel, "Alergias:", alergiasField = new JTextField(), y++);
        addFormField(panel, "Estado Civil:", estadoCivilComboBox = new JComboBox<>(new String[]{"Soltero", "Casado", "Divorciado", "Viudo"}), y++);
        addFormField(panel, "Teléfono:", telefonoField = new JTextField(), y++);
        addFormField(panel, "Tipo de Sangre:", sangreField = new JTextField(), y++);
        addFormField(panel, "Tipo Identificador:", tipoIdentificadorField = new JTextField(), y++);

        // Botones
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());

        JButton registrarBtn = new JButton("Registrar");
        registrarBtn.setForeground(Color.BLACK);
        registrarBtn.setFont(new Font("Arial", Font.BOLD, 12));
        registrarBtn.setBackground(new Color(70, 130, 180));
        registrarBtn.setOpaque(true);
        registrarBtn.setBorderPainted(false);
        registrarBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { registrarBtn.setBackground(new Color(100, 150, 200)); }
            public void mouseExited(MouseEvent evt) { registrarBtn.setBackground(new Color(70, 130, 180)); }
        });
        registrarBtn.addActionListener(e -> registrarPaciente());
        btnPanel.add(registrarBtn);

        JButton volverBtn = new JButton("← Volver");
        volverBtn.setForeground(Color.BLACK);
        volverBtn.setFont(new Font("Arial", Font.BOLD, 12));
        volverBtn.setBackground(new Color(220, 60, 60));
        volverBtn.setOpaque(true);
        volverBtn.setBorderPainted(false);
        volverBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { volverBtn.setBackground(new Color(240, 80, 80)); }
            public void mouseExited(MouseEvent evt) { volverBtn.setBackground(new Color(220, 60, 60)); }
        });
        volverBtn.addActionListener(e -> {
            new HomeWindow().setVisible(true);
            dispose();
        });
        btnPanel.add(volverBtn);

        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        aplicarRestricciones();
    }

    private void addFormField(JPanel panel, String label, JComponent field, int y) {
        panel.add(new JLabel(label), createGbc(0, y));
        panel.add(field, createGbc(1, y));
    }

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = (x == 0) ? 0.3 : 0.7;
        return gbc;
    }

    private void aplicarRestricciones() {
        cedulaField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || cedulaField.getText().length() >= 10) e.consume();
            }
        });

        KeyAdapter soloLetras = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) e.consume();
            }
        };
        nombresField.addKeyListener(soloLetras);
        apellidosField.addKeyListener(soloLetras);
    }

    private void registrarPaciente() {
        try {
            String cedula = cedulaField.getText().trim();
            if (!CedulaEcuatoriana.validar(cedula))
                throw new IllegalArgumentException("Cédula inválida");

            String nombres = nombresField.getText().trim();
            if (!Validaciones.validarNombresCompletos(nombres))
                throw new IllegalArgumentException("Debe ingresar al menos dos nombres");

            String apellidos = apellidosField.getText().trim();
            if (!Validaciones.validarCampoObligatorio(apellidos))
                throw new IllegalArgumentException("Apellidos obligatorios");

            Date fechaNacimiento = fechaNacimientoChooser.getDate();
            if (!Validaciones.validarFechaNacimiento(fechaNacimiento))
                throw new IllegalArgumentException("Fecha de nacimiento inválida");

            String correo = correoField.getText().trim();
            if (!Validaciones.validarEmail(correo))
                throw new IllegalArgumentException("Email inválido");

            String contrasena = new String(contrasenaField.getPassword());
            if (!Validaciones.validarContrasena(contrasena))
                throw new IllegalArgumentException("Contraseña mínima de 6 caracteres");

            String alergias = alergiasField.getText().trim();
            String sexo = sexoComboBox.getSelectedItem().toString();
            String estadoCivil = estadoCivilComboBox.getSelectedItem().toString();
            String telefono = telefonoField.getText().trim();
            String tipoSangre = sangreField.getText().trim();
            String tipoIdentificador = tipoIdentificadorField.getText().trim();

            // Aquí si tienes usuarioPacienteField, no lo usas para crear Paciente, 
            // pues el usuario está definido en la clase Usuario (cedula o correo es identificador)

            PacienteDAO pacienteDAO = new PacienteDAO();
            if (pacienteDAO.existe(cedula))
                throw new IllegalArgumentException("La cédula ya existe");

            // Creas el paciente con todos los campos (idAntecedentes lo dejas null si no hay)
            Paciente nuevoPaciente = new Paciente(
                    cedula, nombres, apellidos, fechaNacimiento,
                    sexo, correo, contrasena, "PACIENTE",
                    alergias, null,  // idAntecedentes null por ahora
                    estadoCivil, telefono, tipoSangre, tipoIdentificador);

            if (pacienteDAO.guardar(nuevoPaciente)) {
                JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente");
                new HomeWindow().setVisible(true);
                dispose();
            } else {
                throw new Exception("Error al guardar en la base de datos");
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private int calcularEdad(Date fechaNacimiento) {
        LocalDate nacimiento = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(nacimiento, LocalDate.now()).getYears();
    }
}
