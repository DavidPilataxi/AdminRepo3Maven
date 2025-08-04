/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package medicosmodule.vistas;

import medicosmodule.conexion.ConectorDB;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.DatePicker;
import java.time.LocalDate;
import java.awt.BorderLayout;

public class Medicos extends javax.swing.JPanel {

    DefaultTableModel modelo;
    int idSeleccionado = -1;

    public Medicos() {
        initComponents();
        cmbGenero.removeAllItems();
        cmbGenero.addItem("Masculino");
        cmbGenero.addItem("Femenino");
        cmbGenero.addItem("Otro");

        setLayout(new BorderLayout());
        add(jPanel1, BorderLayout.CENTER);

        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Apellido", "Cédula", "Correo", "Especialidad",
            "Universidad", "Teléfono", "Dirección", "Fecha Graduación", "Licencia Médica", "Género"
        }, 0);

        tablaMedicos.setModel(modelo);
        tablaMedicos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaMedicos.setRowHeight(25);

// Ajusta el ancho de columnas individualmente
        tablaMedicos.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        tablaMedicos.getColumnModel().getColumn(1).setPreferredWidth(100); // Nombre
        tablaMedicos.getColumnModel().getColumn(2).setPreferredWidth(100); // Apellido
        tablaMedicos.getColumnModel().getColumn(3).setPreferredWidth(100); // Cédula
        tablaMedicos.getColumnModel().getColumn(4).setPreferredWidth(180); // Correo
        tablaMedicos.getColumnModel().getColumn(5).setPreferredWidth(120); // Especialidad
        tablaMedicos.getColumnModel().getColumn(6).setPreferredWidth(140); // Universidad
        tablaMedicos.getColumnModel().getColumn(7).setPreferredWidth(100); // Teléfono
        tablaMedicos.getColumnModel().getColumn(8).setPreferredWidth(160); // Dirección
        tablaMedicos.getColumnModel().getColumn(9).setPreferredWidth(120); // Fecha Graduación
        tablaMedicos.getColumnModel().getColumn(10).setPreferredWidth(140); // Licencia
        tablaMedicos.getColumnModel().getColumn(11).setPreferredWidth(90);  // Género

        cargarEspecialidadesEnCombo();
        cargarDatosMedicos();
        validarCamposMedico();
        //cargarMedicosEnCombo();

        txtNombreMedico.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });
        txtApellidoMedico.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });
        txtCedulaMedico.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });
        txtCorreoMedico.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });

        txtNombreMedico.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume(); // bloquea el caracter
                }
            }
        });

        txtApellidoMedico.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });

        tablaMedicos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaMedicos.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = Integer.parseInt(tablaMedicos.getValueAt(fila, 0).toString());

                    txtNombreMedico.setText(tablaMedicos.getValueAt(fila, 1).toString());
                    txtApellidoMedico.setText(tablaMedicos.getValueAt(fila, 2).toString());
                    txtCedulaMedico.setText(tablaMedicos.getValueAt(fila, 3).toString());
                    txtCorreoMedico.setText(tablaMedicos.getValueAt(fila, 4).toString());
                    cmbEspecialidad.setSelectedItem(tablaMedicos.getValueAt(fila, 5).toString());

                    txtUniversidad.setText(tablaMedicos.getValueAt(fila, 6) != null ? tablaMedicos.getValueAt(fila, 6).toString() : "");
                    txtTelfContacto.setText(tablaMedicos.getValueAt(fila, 7) != null ? tablaMedicos.getValueAt(fila, 7).toString() : "");
                    txtDomicilio.setText(tablaMedicos.getValueAt(fila, 8) != null ? tablaMedicos.getValueAt(fila, 8).toString() : "");
                    String fecha = tablaMedicos.getValueAt(fila, 9) != null ? tablaMedicos.getValueAt(fila, 9).toString() : null;
                    if (fecha != null) {
                        pickerFechaGraduacion.setDate(LocalDate.parse(fecha));
                    } else {
                        pickerFechaGraduacion.clear();
                    }
                    txtNumeroLicenciaMed.setText(tablaMedicos.getValueAt(fila, 10) != null ? tablaMedicos.getValueAt(fila, 10).toString() : "");
                    cmbGenero.setSelectedItem(tablaMedicos.getValueAt(fila, 11) != null ? tablaMedicos.getValueAt(fila, 11).toString() : "");

                    validarCamposMedico();
                }
            }
        });

        //btnRegistrarVacaciones.addActionListener(e -> registrarVacaciones());
        btnGuardarMedico.addActionListener(e -> guardarMedico());
        btnActualizarMedico.addActionListener(e -> actualizarMedico());
        btnLimpiarMedico.addActionListener(e -> limpiarCamposMedico());

        txtUniversidad.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });

        txtTelfContacto.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });

        txtDomicilio.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });

        txtNumeroLicenciaMed.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCamposMedico();
            }
        });

        pickerFechaGraduacion.addDateChangeListener(e -> {
            if (pickerFechaGraduacion.getDate() != null
                    && pickerFechaGraduacion.getDate().isAfter(java.time.LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha de graduación no puede ser posterior a hoy.");
                pickerFechaGraduacion.clear();
            }
            validarCamposMedico();
        });

        cmbGenero.addActionListener(e -> validarCamposMedico());

    }

    private void actualizarMedico() {
        if (idSeleccionado == -1) {
            return;
        }

        String nombre = txtNombreMedico.getText().trim();
        String apellido = txtApellidoMedico.getText().trim();
        String cedula = txtCedulaMedico.getText().trim();
        String correo = txtCorreoMedico.getText().trim();
        String universidad = txtUniversidad.getText().trim();
        String telefono = txtTelfContacto.getText().trim();
        String direccion = txtDomicilio.getText().trim();
        LocalDate fechaGraduacion = pickerFechaGraduacion.getDate();
        String numeroLicencia = txtNumeroLicenciaMed.getText().trim();
        String genero = (String) cmbGenero.getSelectedItem();
        String especialidadNombre = (String) cmbEspecialidad.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || correo.isEmpty()
                || universidad.isEmpty() || telefono.isEmpty() || direccion.isEmpty()
                || fechaGraduacion == null || numeroLicencia.isEmpty() || genero == null || especialidadNombre == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        if (!validarCedulaEcuatoriana(cedula)) {
            JOptionPane.showMessageDialog(this, "La cédula ingresada no es válida para Ecuador.");
            return;
        }

        try (Connection conn = ConectorDB.conectar(); PreparedStatement psId = conn.prepareStatement("SELECT IdEspecialidad FROM Especialidades WHERE Nombre = ?"); PreparedStatement psUpdate = conn.prepareStatement(
                "UPDATE Medicos SET Nombre = ?, Apellido = ?, Cedula = ?, Correo = ?, IdEspecialidad = ?, "
                + "UniversidadGraduacion = ?, TelefonoContacto = ?, Direccion = ?, FechaGraduacion = ?, "
                + "NumeroLicenciaMedica = ?, Genero = ? WHERE IdMedico = ?")) {

            psId.setString(1, especialidadNombre);
            ResultSet rs = psId.executeQuery();
            int idEspecialidad = -1;
            if (rs.next()) {
                idEspecialidad = rs.getInt("IdEspecialidad");
            } else {
                JOptionPane.showMessageDialog(this, "Especialidad no válida.");
                return;
            }

            psUpdate.setString(1, nombre);
            psUpdate.setString(2, apellido);
            psUpdate.setString(3, cedula);
            psUpdate.setString(4, correo);
            psUpdate.setInt(5, idEspecialidad);
            psUpdate.setString(6, universidad);
            psUpdate.setString(7, telefono);
            psUpdate.setString(8, direccion);
            psUpdate.setDate(9, Date.valueOf(fechaGraduacion));
            psUpdate.setString(10, numeroLicencia);
            psUpdate.setString(11, genero);
            psUpdate.setInt(12, idSeleccionado);

            psUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Médico actualizado exitosamente.");
            cargarDatosMedicos();
            limpiarCamposMedico();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar médico: " + ex.getMessage());
        }
    }

    private void guardarMedico() {
        String nombre = txtNombreMedico.getText().trim();
        String apellido = txtApellidoMedico.getText().trim();
        String cedula = txtCedulaMedico.getText().trim();
        String correo = txtCorreoMedico.getText().trim();
        String universidad = txtUniversidad.getText().trim();
        String telefono = txtTelfContacto.getText().trim();
        String direccion = txtDomicilio.getText().trim();
        LocalDate fechaGraduacion = pickerFechaGraduacion.getDate();
        String numeroLicencia = txtNumeroLicenciaMed.getText().trim();
        String genero = (String) cmbGenero.getSelectedItem();
        String especialidadNombre = (String) cmbEspecialidad.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || correo.isEmpty()
                || universidad.isEmpty() || telefono.isEmpty() || direccion.isEmpty()
                || fechaGraduacion == null || numeroLicencia.isEmpty() || genero == null || especialidadNombre == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        if (!validarCedulaEcuatoriana(cedula)) {
            JOptionPane.showMessageDialog(this, "La cédula ingresada no es válida para Ecuador.");
            return;
        }

        try (Connection conn = ConectorDB.conectar(); PreparedStatement psId = conn.prepareStatement("SELECT IdEspecialidad FROM Especialidades WHERE Nombre = ?"); PreparedStatement psInsert = conn.prepareStatement(
                "INSERT INTO Medicos (Nombre, Apellido, Cedula, Correo, IdEspecialidad, "
                + "UniversidadGraduacion, TelefonoContacto, Direccion, FechaGraduacion, "
                + "NumeroLicenciaMedica, Genero) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); PreparedStatement psCheck = conn.prepareStatement(
                        "SELECT COUNT(*) FROM Medicos WHERE Cedula = ? OR Correo = ?")) {

            // Validar cédula o correo duplicado
            psCheck.setString(1, cedula);
            psCheck.setString(2, correo);
            ResultSet rsCheck = psCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Ya existe un médico con esa cédula o correo.");
                return;
            }

            // Obtener ID de especialidad
            psId.setString(1, especialidadNombre);
            ResultSet rs = psId.executeQuery();
            int idEspecialidad = -1;
            if (rs.next()) {
                idEspecialidad = rs.getInt("IdEspecialidad");
            } else {
                JOptionPane.showMessageDialog(this, "Especialidad no válida.");
                return;
            }

            // Insertar nuevo médico
            psInsert.setString(1, nombre);
            psInsert.setString(2, apellido);
            psInsert.setString(3, cedula);
            psInsert.setString(4, correo);
            psInsert.setInt(5, idEspecialidad);
            psInsert.setString(6, universidad);
            psInsert.setString(7, telefono);
            psInsert.setString(8, direccion);
            psInsert.setDate(9, Date.valueOf(fechaGraduacion));
            psInsert.setString(10, numeroLicencia);
            psInsert.setString(11, genero);

            psInsert.executeUpdate();

            JOptionPane.showMessageDialog(this, "Médico guardado exitosamente.");
            cargarDatosMedicos();
            limpiarCamposMedico();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar médico: " + ex.getMessage());
        }
    }

    /*
    private void registrarVacaciones() {
        String medicoSeleccionado = (String) cmbMedicoVacaciones.getSelectedItem();
        LocalDate desde = pickerDesde.getDate();
        LocalDate hasta = pickerHasta.getDate();
        LocalDate hoy = LocalDate.now();
        if (desde.isBefore(hoy)) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio debe ser igual o posterior a hoy.");
            return;
        }

        if (medicoSeleccionado == null || desde == null || hasta == null) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos para registrar vacaciones.");
            return;
        }

        if (desde.isAfter(hasta)) {
            JOptionPane.showMessageDialog(this, "La fecha 'desde' no puede ser posterior a la fecha 'hasta'.");
            return;
        }

        try (Connection conn = ConectorDB.conectar(); PreparedStatement ps = conn.prepareStatement("INSERT INTO EstadoMedico (IdMedico, FechaDesde, FechaHasta, Estado) VALUES ((SELECT IdMedico FROM Medicos WHERE CONCAT(Nombre, ' ', Apellido) = ?), ?, ?, 'Vacaciones')")) {
            ps.setString(1, medicoSeleccionado);
            ps.setDate(2, Date.valueOf(desde));
            ps.setDate(3, Date.valueOf(hasta));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Vacaciones registradas correctamente.");
            pickerDesde.clear();
            pickerHasta.clear();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar vacaciones: " + ex.getMessage());
        }
    }
     */
 /* private void cargarMedicosEnCombo() {
        cmbMedicoVacaciones.removeAllItems();
        try (Connection conn = ConectorDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT Nombre, Apellido FROM Medicos")) {
            while (rs.next()) {
                String nombreCompleto = rs.getString("Nombre") + " " + rs.getString("Apellido");
                cmbMedicoVacaciones.addItem(nombreCompleto);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar médicos en combo: " + ex.getMessage());
        }
    }
     */
    private void cargarEspecialidadesEnCombo() {
        cmbEspecialidad.removeAllItems();
        try (Connection conn = ConectorDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT IdEspecialidad, Nombre FROM Especialidades")) {
            while (rs.next()) {
                cmbEspecialidad.addItem(rs.getString("Nombre"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar especialidades: " + ex.getMessage());
        }
    }

    private boolean validarCedulaEcuatoriana(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        try {
            int provincia = Integer.parseInt(cedula.substring(0, 2));
            int tercerDigito = Integer.parseInt(cedula.substring(2, 3));

            if (provincia < 1 || provincia > 24 || tercerDigito >= 6) {
                return false;
            }

            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int suma = 0;

            for (int i = 0; i < 9; i++) {
                int valor = Integer.parseInt(cedula.substring(i, i + 1)) * coeficientes[i];
                if (valor >= 10) {
                    valor -= 9;
                }
                suma += valor;
            }

            int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
            int decenaSuperior = ((suma + 9) / 10) * 10;
            int resultado = decenaSuperior - suma;

            if (resultado == 10) {
                resultado = 0;
            }

            return resultado == digitoVerificador;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void validarCamposMedico() {
        LocalDate fechaGraduacion = pickerFechaGraduacion.getDate();
        LocalDate hoy = LocalDate.now();

        boolean habilitado = !txtNombreMedico.getText().trim().isEmpty()
                && !txtApellidoMedico.getText().trim().isEmpty()
                && !txtCedulaMedico.getText().trim().isEmpty()
                && !txtCorreoMedico.getText().trim().isEmpty()
                && !txtUniversidad.getText().trim().isEmpty()
                && !txtTelfContacto.getText().trim().isEmpty()
                && !txtDomicilio.getText().trim().isEmpty()
                && fechaGraduacion != null
                && !fechaGraduacion.isAfter(hoy)
                && !txtNumeroLicenciaMed.getText().trim().isEmpty()
                && cmbGenero.getSelectedItem() != null;

        btnGuardarMedico.setEnabled(habilitado);
        btnActualizarMedico.setEnabled(habilitado && idSeleccionado != -1);
    }

    private void cargarDatosMedicos() {
        modelo.setRowCount(0);
        try (Connection conn = ConectorDB.conectar(); PreparedStatement ps = conn.prepareStatement(
                "SELECT M.IdMedico, M.Nombre, M.Apellido, M.Cedula, M.Correo, "
                + "E.Nombre AS EspecialidadNombre, "
                + "M.UniversidadGraduacion, M.TelefonoContacto, M.Direccion, M.FechaGraduacion, "
                + "M.NumeroLicenciaMedica, M.Genero "
                + "FROM Medicos M LEFT JOIN Especialidades E ON M.IdEspecialidad = E.IdEspecialidad"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("IdMedico"),
                    rs.getString("Nombre"),
                    rs.getString("Apellido"),
                    rs.getString("Cedula"),
                    rs.getString("Correo"),
                    rs.getString("EspecialidadNombre"),
                    rs.getString("UniversidadGraduacion"),
                    rs.getString("TelefonoContacto"),
                    rs.getString("Direccion"),
                    rs.getDate("FechaGraduacion").toString(),
                    rs.getString("NumeroLicenciaMedica"),
                    rs.getString("Genero")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar médicos: " + ex.getMessage());
        }
    }

    private void limpiarCamposMedico() {
        txtNombreMedico.setText("");
        txtApellidoMedico.setText("");
        txtCedulaMedico.setText("");
        txtCorreoMedico.setText("");
        txtUniversidad.setText("");
        txtTelfContacto.setText("");
        txtDomicilio.setText("");
        pickerFechaGraduacion.clear();
        txtNumeroLicenciaMed.setText("");
        cmbGenero.setSelectedIndex(0);
        cmbEspecialidad.setSelectedIndex(0);

        idSeleccionado = -1;
        tablaMedicos.clearSelection();
        validarCamposMedico();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreMedico = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCorreoMedico = new javax.swing.JTextField();
        txtApellidoMedico = new javax.swing.JTextField();
        txtCedulaMedico = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbEspecialidad = new javax.swing.JComboBox<>();
        btnActualizarMedico = new javax.swing.JButton();
        btnGuardarMedico = new javax.swing.JButton();
        btnEliminarMedico = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtUniversidad = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNumeroLicenciaMed = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtDomicilio = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTelfContacto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbGenero = new javax.swing.JComboBox<>();
        btnLimpiarMedico = new javax.swing.JButton();
        pickerFechaGraduacion = new com.github.lgooddatepicker.components.DatePicker();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMedicos = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(204, 238, 252));

        jPanel2.setBackground(new java.awt.Color(204, 238, 252));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 395));

        jLabel1.setText("Datos del Médico(Todos los campos deben ser completados obligatoriamente)");
        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));

        jLabel2.setText("Nombre *");
        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel3.setText("Apellido *");
        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel4.setText("Cédula *");
        jLabel4.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel5.setText("Correo *");
        jLabel5.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel6.setText("Especialidad *");
        jLabel6.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        cmbEspecialidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnActualizarMedico.setText("Actualizar");
        btnActualizarMedico.setBackground(new java.awt.Color(0, 51, 255));
        btnActualizarMedico.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnActualizarMedico.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarMedicoActionPerformed(evt);
            }
        });

        btnGuardarMedico.setText("Guardar");
        btnGuardarMedico.setBackground(new java.awt.Color(0, 51, 255));
        btnGuardarMedico.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnGuardarMedico.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarMedicoActionPerformed(evt);
            }
        });

        btnEliminarMedico.setText("Eliminar");
        btnEliminarMedico.setBackground(new java.awt.Color(0, 51, 255));
        btnEliminarMedico.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnEliminarMedico.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarMedicoActionPerformed(evt);
            }
        });

        jLabel8.setText("Universidad *");
        jLabel8.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel9.setText("Fecha de Graduación *");
        jLabel9.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel10.setText("Número de Licencia Médica *");
        jLabel10.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel11.setText("Dirección de Domicilio *");
        jLabel11.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel12.setText("Teléfono de Contacto *");
        jLabel12.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel7.setText("Género *");
        jLabel7.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        cmbGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnLimpiarMedico.setText("Limpiar");
        btnLimpiarMedico.setBackground(new java.awt.Color(0, 51, 255));
        btnLimpiarMedico.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnLimpiarMedico.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarMedicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnGuardarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnActualizarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbEspecialidad, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApellidoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCedulaMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCorreoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pickerFechaGraduacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelfContacto)
                            .addComponent(txtDomicilio)
                            .addComponent(txtNumeroLicenciaMed)
                            .addComponent(txtUniversidad)
                            .addComponent(cmbGenero, 0, 340, Short.MAX_VALUE)))
                    .addComponent(jLabel1))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUniversidad, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel3))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(pickerFechaGraduacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCedulaMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroLicenciaMed, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCorreoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbEspecialidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelfContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtApellidoMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarMedico)
                    .addComponent(btnActualizarMedico)
                    .addComponent(btnEliminarMedico)
                    .addComponent(btnLimpiarMedico))
                .addGap(20, 20, 20))
        );

        tablaMedicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaMedicos);

        jScrollPane2.setViewportView(jScrollPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarMedicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarMedicoActionPerformed

    private void btnEliminarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarMedicoActionPerformed
        // TODO add your handling code here:
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un médico para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el médico seleccionado?\nEsto también eliminará sus vacaciones registradas.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = ConectorDB.conectar()) {
                // Primero eliminar vacaciones asociadas
                PreparedStatement psVac = conn.prepareStatement("DELETE FROM EstadoMedico WHERE IdMedico = ?");
                psVac.setInt(1, idSeleccionado);
                psVac.executeUpdate();

                // Luego eliminar médico
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Medicos WHERE IdMedico = ?");
                ps.setInt(1, idSeleccionado);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Médico eliminado exitosamente.");
                cargarDatosMedicos();
                //cargarMedicosEnCombo(); // actualiza el combo de vacaciones
                limpiarCamposMedico();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar médico: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnEliminarMedicoActionPerformed

    private void btnGuardarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarMedicoActionPerformed

    }//GEN-LAST:event_btnGuardarMedicoActionPerformed

    private void btnActualizarMedicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarMedicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarMedicoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarMedico;
    private javax.swing.JButton btnEliminarMedico;
    private javax.swing.JButton btnGuardarMedico;
    private javax.swing.JButton btnLimpiarMedico;
    private javax.swing.JComboBox<String> cmbEspecialidad;
    private javax.swing.JComboBox<String> cmbGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.github.lgooddatepicker.components.DatePicker pickerFechaGraduacion;
    private javax.swing.JTable tablaMedicos;
    private javax.swing.JTextField txtApellidoMedico;
    private javax.swing.JTextField txtCedulaMedico;
    private javax.swing.JTextField txtCorreoMedico;
    private javax.swing.JTextField txtDomicilio;
    private javax.swing.JTextField txtNombreMedico;
    private javax.swing.JTextField txtNumeroLicenciaMed;
    private javax.swing.JTextField txtTelfContacto;
    private javax.swing.JTextField txtUniversidad;
    // End of variables declaration//GEN-END:variables
}
