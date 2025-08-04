    package adminmodule.vista;

    import adminmodule.controlador.AdminController;
    import adminmodule.modelo.Paciente;
    import adminmodule.util.CedulaEcuatoriana;
    import adminmodule.util.Validaciones;
    import com.toedter.calendar.JDateChooser;

    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import java.awt.*;
    import java.awt.event.MouseAdapter;
    import java.awt.event.MouseEvent;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.List;

    public class GestionPacientesWindow extends JFrame {

        private JTable tablaPacientes;
        private DefaultTableModel modeloTabla;
        private AdminController adminController;

        // Campos del formulario de edición
        private JTextField txtCedula, txtNombres, txtApellidos, txtCorreo, txtAlergias, txtOxigenacion, txtIdAntecedentes;
        private JPasswordField txtContrasena;
        private JComboBox<String> cmbSexo;
        private JDateChooser dcFechaNacimiento;

        private JButton btnEditar, btnEliminar, btnGuardarCambios, btnCancelar;

        public GestionPacientesWindow() {
            setTitle("Gestión de Pacientes");
            setSize(1200, 700);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            adminController = new AdminController();

            // --- Panel Superior: Título ---
            JLabel lblTitulo = new JLabel("Gestión de Información de Pacientes", SwingConstants.CENTER);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
            lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            add(lblTitulo, BorderLayout.NORTH);

            // --- Panel Central: Tabla y Formulario ---
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setResizeWeight(0.7); // La tabla ocupa más espacio

            // --- Panel Izquierdo: Tabla de Pacientes ---
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder("Lista de Pacientes"));

            String[] columnas = {"Cédula", "Nombres", "Apellidos", "Fecha Nac.", "Sexo", "Correo", "Alergias", "Oxigenación", "ID Antecedentes"};
            modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Hacer que las celdas de la tabla no sean editables directamente
                }
            };
            tablaPacientes = new JTable(modeloTabla);
            tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo una fila a la vez
            tablaPacientes.getTableHeader().setReorderingAllowed(false); // Evitar reordenar columnas

            // Listener para cargar datos al seleccionar una fila
            tablaPacientes.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int filaSeleccionada = tablaPacientes.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        cargarDatosPacienteEnFormulario(filaSeleccionada);
                        habilitarCamposFormulario(false); // Deshabilitar para solo visualización
                        btnEditar.setEnabled(true);
                        btnEliminar.setEnabled(true);
                        btnGuardarCambios.setEnabled(false);
                        btnCancelar.setEnabled(false);
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(tablaPacientes);
            panelTabla.add(scrollPane, BorderLayout.CENTER);

            // --- Panel Derecho: Formulario de Edición ---
            JPanel panelFormulario = new JPanel(new GridBagLayout());
            panelFormulario.setBorder(BorderFactory.createTitledBorder("Detalles del Paciente"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            int row = 0;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Cédula:"), gbc);
            gbc.gridx = 1; txtCedula = new JTextField(20); txtCedula.setEditable(false); panelFormulario.add(txtCedula, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Nombres:"), gbc);
            gbc.gridx = 1; txtNombres = new JTextField(20); panelFormulario.add(txtNombres, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Apellidos:"), gbc);
            gbc.gridx = 1; txtApellidos = new JTextField(20); panelFormulario.add(txtApellidos, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Fecha Nacimiento:"), gbc);
            gbc.gridx = 1; dcFechaNacimiento = new JDateChooser(); dcFechaNacimiento.setDateFormatString("dd/MM/yyyy"); panelFormulario.add(dcFechaNacimiento, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Sexo:"), gbc);
            gbc.gridx = 1; cmbSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"}); panelFormulario.add(cmbSexo, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Correo:"), gbc);
            gbc.gridx = 1; txtCorreo = new JTextField(20); panelFormulario.add(txtCorreo, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Contraseña:"), gbc);
            gbc.gridx = 1; txtContrasena = new JPasswordField(20); panelFormulario.add(txtContrasena, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Alergias:"), gbc);
            gbc.gridx = 1; txtAlergias = new JTextField(20); panelFormulario.add(txtAlergias, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("Oxigenación:"), gbc);
            gbc.gridx = 1; txtOxigenacion = new JTextField(20); panelFormulario.add(txtOxigenacion, gbc);

            row++;
            gbc.gridx = 0; gbc.gridy = row; panelFormulario.add(new JLabel("ID Antecedentes:"), gbc);
            gbc.gridx = 1; txtIdAntecedentes = new JTextField(20); panelFormulario.add(txtIdAntecedentes, gbc);

            // Botones de acción del formulario
            row++;
            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
            JPanel panelBotonesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            btnEditar = new JButton("Editar");
            btnEliminar = new JButton("Eliminar");
            btnGuardarCambios = new JButton("Guardar Cambios");
            btnCancelar = new JButton("Cancelar");

            panelBotonesFormulario.add(btnEditar);
            panelBotonesFormulario.add(btnEliminar);
            panelBotonesFormulario.add(btnGuardarCambios);
            panelBotonesFormulario.add(btnCancelar);
            panelFormulario.add(panelBotonesFormulario, gbc);

            splitPane.setLeftComponent(panelTabla);
            splitPane.setRightComponent(panelFormulario);
            add(splitPane, BorderLayout.CENTER);

            // --- Panel Inferior: Botones de Control General ---
            JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            JButton btnActualizarTabla = new JButton("Actualizar Tabla");
            JButton btnVolver = new JButton("Volver al Panel Admin");

            panelControl.add(btnActualizarTabla);
            panelControl.add(btnVolver);
            add(panelControl, BorderLayout.SOUTH);

            // --- Acciones de los botones ---
            btnEditar.addActionListener(e -> {
                habilitarCamposFormulario(true);
                txtCedula.setEditable(false); // La cédula no se edita
                btnEditar.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnGuardarCambios.setEnabled(true);
                btnCancelar.setEnabled(true);
            });

            btnEliminar.addActionListener(e -> eliminarPaciente());

            btnGuardarCambios.addActionListener(e -> guardarCambiosPaciente());

            btnCancelar.addActionListener(e -> {
                limpiarFormulario();
                habilitarCamposFormulario(false);
                btnEditar.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnGuardarCambios.setEnabled(false);
                btnCancelar.setEnabled(false);
            });

            btnActualizarTabla.addActionListener(e -> cargarPacientesEnTabla());

            btnVolver.addActionListener(e -> {
                dispose();
                // Si AdminWindow ya está abierto, no es necesario crear uno nuevo
                // new AdminWindow().setVisible(true); // Descomentar si AdminWindow se cierra al abrir esta
            });

            // Inicializar
            cargarPacientesEnTabla();
            habilitarCamposFormulario(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnGuardarCambios.setEnabled(false);
            btnCancelar.setEnabled(false);
        }

        private void cargarPacientesEnTabla() {
            modeloTabla.setRowCount(0); // Limpiar tabla
            List<Paciente> pacientes = adminController.obtenerTodosPacientes();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (Paciente p : pacientes) {
                modeloTabla.addRow(new Object[]{
                    p.getCedula(),
                    p.getNombres(),
                    p.getApellidos(),
                    p.getFechaNacimiento() != null ? sdf.format(p.getFechaNacimiento()) : "",
                    p.getSexo(),
                    p.getCorreo(),
                    p.getAlergias(),
                    p.getOxigenacion(),
                    p.getIdAntecedentes()
                });
            }
        }

        private void cargarDatosPacienteEnFormulario(int fila) {
            txtCedula.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombres.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtApellidos.setText(modeloTabla.getValueAt(fila, 2).toString());
            try {
                Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(modeloTabla.getValueAt(fila, 3).toString());
                dcFechaNacimiento.setDate(fecha);
            } catch (ParseException ex) {
                dcFechaNacimiento.setDate(null);
            }
            cmbSexo.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
            txtCorreo.setText(modeloTabla.getValueAt(fila, 5).toString());
            // La contraseña no se carga por seguridad, se dejará en blanco o con asteriscos
            txtContrasena.setText("");
            txtAlergias.setText(modeloTabla.getValueAt(fila, 6) != null ? modeloTabla.getValueAt(fila, 6).toString() : "");
            txtOxigenacion.setText(modeloTabla.getValueAt(fila, 7) != null ? modeloTabla.getValueAt(fila, 7).toString() : "");
            txtIdAntecedentes.setText(modeloTabla.getValueAt(fila, 8) != null ? modeloTabla.getValueAt(fila, 8).toString() : "");
        }

        private void habilitarCamposFormulario(boolean habilitar) {
            txtNombres.setEditable(habilitar);
            txtApellidos.setEditable(habilitar);
            dcFechaNacimiento.setEnabled(habilitar);
            cmbSexo.setEnabled(habilitar);
            txtCorreo.setEditable(habilitar);
            txtContrasena.setEditable(habilitar);
            txtAlergias.setEditable(habilitar);
            txtOxigenacion.setEditable(habilitar);
            txtIdAntecedentes.setEditable(habilitar);
        }

        private void limpiarFormulario() {
            txtCedula.setText("");
            txtNombres.setText("");
            txtApellidos.setText("");
            dcFechaNacimiento.setDate(null);
            cmbSexo.setSelectedIndex(0);
            txtCorreo.setText("");
            txtContrasena.setText("");
            txtAlergias.setText("");
            txtOxigenacion.setText("");
            txtIdAntecedentes.setText("");
        }

        private void guardarCambiosPaciente() {
            // 1. Obtener todos los datos del formulario
            String cedula = txtCedula.getText().trim();
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            Date fechaNacimiento = dcFechaNacimiento.getDate();
            String sexo = cmbSexo.getSelectedItem().toString();
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();
            String alergias = txtAlergias.getText().trim();
            String oxigenacion = txtOxigenacion.getText().trim();
            String idAntecedentes = txtIdAntecedentes.getText().trim();

            // 2. Validaciones básicas de campos obligatorios
            if (nombres.isEmpty() || apellidos.isEmpty() || fechaNacimiento == null) {
                JOptionPane.showMessageDialog(this, 
                    "Los campos Nombres, Apellidos y Fecha de Nacimiento son obligatorios",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Validación de formato de correo
            if (!correo.isEmpty() && !Validaciones.validarEmail(correo)) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese un correo electrónico válido",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4. Validación de contraseña (si se modificó)
            if (!contrasena.isEmpty() && !Validaciones.validarContrasena(contrasena)) {
                JOptionPane.showMessageDialog(this, 
                    "La contraseña debe tener al menos 6 caracteres",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 5. Validación especial para ID de antecedentes
            if (!idAntecedentes.isEmpty()) {
                try {
                    // Verificar que sea un número válido
                    int id = Integer.parseInt(idAntecedentes);

                    // Opcional: Verificar que exista en la base de datos
                    if (!adminController.existeAntecedente(id)) {
                        int respuesta = JOptionPane.showConfirmDialog(this,
                            "El ID de antecedentes no existe. ¿Desea crearlo automáticamente?",
                            "Antecedente no encontrado", JOptionPane.YES_NO_OPTION);

                        if (respuesta == JOptionPane.YES_OPTION) {
                            if (!adminController.crearAntecedente(id)) {
                                JOptionPane.showMessageDialog(this,
                                    "No se pudo crear el antecedente automáticamente",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            return; // El usuario decidió no crear el antecedente
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                        "El ID de antecedentes debe ser un número válido",
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 6. Confirmación antes de guardar
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea guardar los cambios para el paciente con cédula " + cedula + "?",
                "Confirmar Actualización", JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            // 7. Intentar guardar los cambios
            try {
                boolean exito = adminController.actualizarPaciente(
                    cedula, nombres, apellidos, fechaNacimiento,
                    sexo, correo, contrasena.isEmpty() ? null : contrasena,
                    alergias, oxigenacion, idAntecedentes.isEmpty() ? null : idAntecedentes
                );

                if (exito) {
                    JOptionPane.showMessageDialog(this,
                        "Los datos del paciente se actualizaron correctamente",
                        "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    cargarPacientesEnTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el paciente. Verifique los datos e intente nuevamente.",
                        "Error al Actualizar", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar paciente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        
        

        private void eliminarPaciente() {
            String cedula = txtCedula.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar al paciente con cédula " + cedula + "?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = adminController.eliminarPaciente(cedula) ; // Reutiliza el método de AdminController
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPacientesEnTabla();
                    limpiarFormulario();
                    habilitarCamposFormulario(false);
                    btnEditar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                    btnGuardarCambios.setEnabled(false);
                    btnCancelar.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar paciente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Método main para probar la ventana (opcional)
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new GestionPacientesWindow().setVisible(true);
            });
        }
    }
    
