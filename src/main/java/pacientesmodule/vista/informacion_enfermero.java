/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import pacientesmodule.pacientesDAO.CitaDAO;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import pacientesmodule.conexion.ConexionSQLServer;

/**
 *
 * @author USUARIO
 */
public class informacion_enfermero extends javax.swing.JFrame {
    private String cedulaEnfermero;
    private String cedulaPaciente;
    private String idCita;
    private int id_cita;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(informacion_enfermero.class.getName());

    /**
     * Creates new form informacion_enfermero
     */

            
    public informacion_enfermero() {
        initComponents();
    }

    public informacion_enfermero(String cedulaEnfermero) {
        initComponents();
        this.cedulaEnfermero = cedulaEnfermero;
        informacion(this.cedulaEnfermero);
        cargarDatosDelPaciente();
        cargarDatosContacto();
        cargarDatosAnamnesis(this.cedulaPaciente);
        this.setLocationRelativeTo(this);
        
    }
    
    public void informacion(String cedulaEnfermero) {
        this.setLocationRelativeTo(this);
        this.cedulaEnfermero = cedulaEnfermero;
        Map<String, String> datosPaciente = CitaDAO.obtenerPacienteConCitaMasProximaEnfermero(cedulaEnfermero);
        if (datosPaciente != null && !datosPaciente.isEmpty()) {
            this.cedulaPaciente=datosPaciente.getOrDefault("cedula", "");
            System.out.println("la cedula del paciente es");
            System.out.println(this.cedulaPaciente);
        }
        cargarDatosDelPaciente();
    }
   private void cargarDatosDelPaciente() {
    Map<String, String> datos = CitaDAO.obtenerPacienteConCitaMasProximaEnfermero(cedulaEnfermero);
    System.out.println("----------------------------------");
    System.out.println(datos);
    if (datos != null && !datos.isEmpty()) {
        System.out.println(datos.get("cedula"));
        this.cedulaPaciente = datos.get("cedula");
        this.jTFNombres.setText(datos.getOrDefault("nombres", ""));
        this.jTFApellidos.setText(datos.getOrDefault("apellidos", ""));
        this.jTFEdad.setText(datos.getOrDefault("edad", ""));
        this.jTFCedula.setText(datos.getOrDefault("cedula", ""));
        this.jTFFechaNacimiento.setText(datos.getOrDefault("fecha_nacimiento", ""));
        this.jTFGenero.setText(datos.getOrDefault("sexo", ""));
        this.jTFCorreo.setText(datos.getOrDefault("correo", ""));
        this.jTFTelefono.setText(datos.getOrDefault("telefono", ""));
        this.id_cita = Integer.parseInt(datos.getOrDefault("id_cita", "0"));
        //this.jTFSangre.setText(datos.getOrDefault("sangre", ""));
        this.jCBEstadoCivil.setSelectedItem(datos.get("estado_civil"));
        this.jTFNombres.setText(datos.getOrDefault("nombres", "hola"));
        this.jCBTipoIdentificador.setSelectedItem(datos.getOrDefault("tipo_identificador", ""));

    } else {
        limpiarCamposPaciente(); 
    }
}
   private void limpiarCamposPaciente() {
        jTFNombres.setText("");
        jTFApellidos.setText("");
        jTFCedula.setText("");
        jTFFechaNacimiento.setText("");
        jTFGenero.setText("");
        jTFCorreo.setText("");
    }
   private void cargarDatosContacto() {
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) {
        return;
    }
    
    
    // Obtener datos de contacto desde la BD (incluyendo ahora el teléfono)
    Map<String, String> datosContacto = CitaDAO.obtenerDatosContacto(this.cedulaPaciente);
    
    if (datosContacto != null && !datosContacto.isEmpty()) {
        jTFCedulaContacto.setText(datosContacto.getOrDefault("cedula", ""));
        jTFNombresContacto.setText(datosContacto.getOrDefault("nombres", ""));
        jTFApellidosContacto.setText(datosContacto.getOrDefault("apellidos", ""));
        jTFTelefonoContacto.setText(datosContacto.getOrDefault("telefono", "")); // Nuevo campo
    }
    
    // Deshabilitar campos inicialmente
    jTFCedulaContacto.setEditable(false);
    jTFNombresContacto.setEditable(false);
    jTFApellidosContacto.setEditable(false);
    jTFTelefonoContacto.setEditable(false); // Nuevo campo
}
   private void cargarDatosAnamnesis(String cedulaPaciente1) {
    Map<String, String> datos = CitaDAO.obtenerAnamnesis(cedulaPaciente);
    
    if (datos != null && !datos.isEmpty()) {
        // --- Datos físicos y signos vitales ---
        this.jTFEstatura.setText(datos.getOrDefault("estatura", ""));
        this.jTFPresionSistolica.setText(datos.getOrDefault("presion_sistolica", ""));
        this.jTFPresionDiastolica.setText(datos.getOrDefault("presion_diastolica", ""));
        this.jTFPeso.setText(datos.getOrDefault("peso", ""));
        this.jTFFrecuenciaCardiaca.setText(datos.getOrDefault("frecuencia_cardiaca", ""));
        this.jTAExamenFisico.setText(datos.getOrDefault("examen_fisico", ""));
        this.jTAExamenFisico.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        this.jTFEstatura.setText(datos.getOrDefault("estatura", ""));
        this.jTFTemperatura.setText(datos.getOrDefault("temperatura", ""));
        this.jTFOxigenacion.setText(datos.getOrDefault("oxigenacion", ""));
        this.jTAMotivo.setText(datos.getOrDefault("motivo", ""));
        this.jTAMotivo.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
    }
}
   private boolean actualizarAnamnesisYCita() {
    // Primero obtenemos el id_anamnesis de la cita actual
    String obtenerIdAnamnesis = "SELECT id_anamnesis FROM Cita WHERE id_cita = ?";
    int idAnamnesis = -1;
    
    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(obtenerIdAnamnesis)) {
        
        pstmt.setInt(1, this.id_cita);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            idAnamnesis = rs.getInt("id_anamnesis");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la cita especificada", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
    } catch (SQLException | ClassNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Error al obtener datos de la cita: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
    
    // Si no tiene anamnesis asociada, creamos una nueva
    if (idAnamnesis == -1) {
        String insertAnamnesis = "INSERT INTO Anamnesis ( id_historiaClinica, " +
                                "estatura, presion_sistolica, presion_diastolica, peso, frecuencia_cardiaca, examen_fisico, temperatura, oxigenacion) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); " +
                                "UPDATE Cita SET id_anamnesis = SCOPE_IDENTITY()" +"motivo = ?"+
                                "WHERE id_cita = ?";
        
        try (Connection conn = ConexionSQLServer.conectar();
             PreparedStatement pstmt = conn.prepareStatement(insertAnamnesis)) {
            
            
            pstmt.setInt(1, obtenerIdHistoriaClinica(this.id_cita)); 
            pstmt.setDouble(2, Double.parseDouble(this.jTFEstatura.getText()));
            pstmt.setInt(3, Integer.parseInt(this.jTFPresionSistolica.getText()));
            pstmt.setInt(4, Integer.parseInt(this.jTFPresionDiastolica.getText()));
            pstmt.setDouble(5, Double.parseDouble(this.jTFPeso.getText()));
            pstmt.setInt(6, Integer.parseInt(this.jTFFrecuenciaCardiaca.getText()));
            
            pstmt.setString(7, this.jTAExamenFisico.getText());
            pstmt.setInt(8, Integer.parseInt(this.jTFTemperatura.getText()));
            
            
            pstmt.setInt(9, Integer.parseInt(this.jTFOxigenacion.getText()));
            pstmt.setString(10, this.jTAMotivo.getText());
            pstmt.setInt(11, this.id_cita);
            
            int filasAfectadas = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cambios guardados con éxito","Guardar",JOptionPane.INFORMATION_MESSAGE);
            return filasAfectadas > 0;
                
            
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al crear nueva anamnesis: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    } else {
        String sql = "BEGIN TRANSACTION; " +
                     "UPDATE Anamnesis SET " +
                     "estatura = ?, " +
                     "presion_sistolica = ?, " +
                     "presion_diastolica = ?, " +
                     "peso = ?, " +
                     "frecuencia_cardiaca = ?, " +
                     "examen_fisico = ?, " +
                     "temperatura = ?, " +
                     "oxigenacion = ? " +
                     "WHERE id_anamnesis = ?; " +
                     
                     "UPDATE Cita SET " +
                     "motivo = ? " +
                     "WHERE id_cita = ?; " +
                     "COMMIT;";
        
        try (Connection conn = ConexionSQLServer.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            
               pstmt.setDouble(1, Double.parseDouble(this.jTFEstatura.getText()));
            
                   pstmt.setInt(2, Integer.parseInt(this.jTFPresionSistolica.getText()));
                    
                    pstmt.setInt(3, Integer.parseInt(this.jTFPresionDiastolica.getText()));
                
                pstmt.setDouble(4, Double.parseDouble(this.jTFPeso.getText()));
                
                pstmt.setInt(5, Integer.parseInt(this.jTFFrecuenciaCardiaca.getText()));
                
                    pstmt.setString(6, this.jTAExamenFisico.getText());
                        pstmt.setInt(7, Integer.parseInt(this.jTFTemperatura.getText()));
                    pstmt.setInt(8, Integer.parseInt(this.jTFOxigenacion.getText()));
                pstmt.setInt(9, idAnamnesis);
            
            
            pstmt.setString(10, this.jTAMotivo.getText()); 
            pstmt.setInt(11, this.id_cita);
            
            int filasAfectadas = pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cambios guardados con éxito","Guardar",JOptionPane.INFORMATION_MESSAGE);
            return filasAfectadas > 0;
            
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}

// Método auxiliar para obtener id_historiaClinica (debes implementarlo según tu esquema)
private int obtenerIdHistoriaClinica(int idCita) {
    String sql = "SELECT p.id_historiaClinica " +
                 "FROM Cita c " +
                 "JOIN Paciente p ON c.id_paciente = p.cedula " +
                 "WHERE c.id_cita = ?";
    
    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, idCita);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("id_historiaClinica");
        } else {
            JOptionPane.showMessageDialog(null, 
                "No se encontró la historia clínica asociada a esta cita", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return -1; // Retorna -1 si no encuentra la historia clínica
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al obtener historia clínica: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return -1;
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null, 
            "Error: Driver JDBC no encontrado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return -1;
    }
}
private void habilitarCamposEdicion(boolean habilitar) {
    this.jTFPresionSistolica.setEditable(habilitar);
    this.jTFPresionDiastolica.setEditable(habilitar);
    this.jTFPeso.setEditable(habilitar);
    this.jTFFrecuenciaCardiaca.setEditable(habilitar);
    this.jTFTemperatura.setEditable(habilitar);
    this.jTFOxigenacion.setEditable(habilitar);
    this.jTFEstatura.setEditable(habilitar);
    this.jTAExamenFisico.setEditable(habilitar);
    this.jTAMotivo.setEditable(habilitar);
}
private boolean validarCamposAnamnesis() {
    try {
        // Validar campos numéricos
        validarCampoNumerico(jTFPresionSistolica, "Presión sistólica");
        validarCampoNumerico(jTFPresionDiastolica, "Presión diastólica");
        validarCampoNumerico(jTFPeso, "Peso");
        validarCampoNumerico(jTFEstatura, "Estatura");
        validarCampoNumerico(jTFFrecuenciaCardiaca, "Frecuencia cardíaca");
        validarCampoNumerico(jTFTemperatura, "Temperatura");
        validarCampoNumerico(jTFOxigenacion, "Oxigenación");

        if (jTAMotivo.getText().trim().isEmpty()) {
            mostrarError("El motivo no puede estar vacío", jTAMotivo);
            return false;
        }
        if (jTAExamenFisico.getText().trim().isEmpty()) {
            mostrarError("El examen físico no puede estar vacío", jTAExamenFisico);
            return false;
        }

        return true;
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
private void validarCampoNumerico(JTextField campo, String nombreCampo) {
    String texto = campo.getText().trim();
    if (texto.isEmpty()) {
        throw new NumberFormatException(nombreCampo + " no puede estar vacío");
    }
    try {
        Double.parseDouble(texto); 
    } catch (NumberFormatException e) {
        throw new NumberFormatException(nombreCampo + " debe ser un número válido");
    }
}

private void mostrarError(String mensaje, JComponent componente) {
    JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    componente.requestFocus(); // Enfoca el campo problemático
}



        
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPInformacionPaciente = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFNombres = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTFApellidos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTFCedula = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTFFechaNacimiento = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFCorreo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jTFGenero = new javax.swing.JTextField();
        jTFEdad = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jCBEstadoCivil = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jTFTelefono = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTFCedulaContacto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTFNombresContacto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTFTelefonoContacto = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTFApellidosContacto = new javax.swing.JTextField();
        jCBTipoIdentificador = new javax.swing.JComboBox<>();
        jPAnamnesis = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTFPresionSistolica = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTAMotivo = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTAExamenFisico = new javax.swing.JTextArea();
        jTFFrecuenciaCardiaca = new javax.swing.JTextField();
        jTFPresionDiastolica = new javax.swing.JTextField();
        jTFPeso = new javax.swing.JTextField();
        jTFEstatura = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTFOxigenacion = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTFTemperatura = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jBEditar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nombres");

        jTFNombres.setEditable(false);
        jTFNombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNombresActionPerformed(evt);
            }
        });

        jLabel2.setText("Apellidos");

        jTFApellidos.setEditable(false);
        jTFApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFApellidosActionPerformed(evt);
            }
        });

        jLabel3.setText("Tipo_identificador");

        jTFCedula.setEditable(false);
        jTFCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCedulaActionPerformed(evt);
            }
        });

        jLabel4.setText("Fecha de nacimiento");

        jTFFechaNacimiento.setEditable(false);
        jTFFechaNacimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFFechaNacimientoFocusLost(evt);
            }
        });
        jTFFechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFFechaNacimientoActionPerformed(evt);
            }
        });

        jLabel5.setText("Género");

        jLabel6.setText("Correo electrónico");

        jTFCorreo.setEditable(false);
        jTFCorreo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCorreoFocusLost(evt);
            }
        });
        jTFCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCorreoActionPerformed(evt);
            }
        });

        jLabel26.setText("Edad");

        jTFGenero.setEditable(false);
        jTFGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFGeneroActionPerformed(evt);
            }
        });

        jTFEdad.setEditable(false);
        jTFEdad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFEdadFocusLost(evt);
            }
        });
        jTFEdad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFEdadActionPerformed(evt);
            }
        });

        jLabel27.setText("Estado civil");

        jCBEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Casado/a", "Divorciado/a", "Viduo/a", "Soltero/a" }));

        jLabel8.setText("Teléfono");

        jTFTelefono.setEditable(false);
        jTFTelefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFTelefonoFocusLost(evt);
            }
        });
        jTFTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFTelefonoActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Persona de contacto"));

        jLabel7.setText("Cédula");

        jLabel9.setText("Nombres");

        jLabel10.setText("Apellidos");

        jLabel11.setText("Telefono");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(316, 316, 316)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTFNombresContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123)
                        .addComponent(jTFCedulaContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTFApellidosContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTFTelefonoContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(198, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTFCedulaContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTFNombresContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTFApellidosContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTFTelefonoContacto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );

        jCBTipoIdentificador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "Otro" }));

        javax.swing.GroupLayout jPInformacionPacienteLayout = new javax.swing.GroupLayout(jPInformacionPaciente);
        jPInformacionPaciente.setLayout(jPInformacionPacienteLayout);
        jPInformacionPacienteLayout.setHorizontalGroup(
            jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInformacionPacienteLayout.createSequentialGroup()
                .addGap(262, 262, 262)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPInformacionPacienteLayout.createSequentialGroup()
                        .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTFNombres)
                            .addComponent(jTFApellidos)
                            .addComponent(jTFFechaNacimiento)
                            .addComponent(jTFCedula)
                            .addComponent(jTFGenero)
                            .addComponent(jTFCorreo)
                            .addComponent(jTFEdad)
                            .addComponent(jCBEstadoCivil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addComponent(jCBTipoIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInformacionPacienteLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInformacionPacienteLayout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(375, 375, 375))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPInformacionPacienteLayout.setVerticalGroup(
            jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInformacionPacienteLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTFNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTFCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBTipoIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTFFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPInformacionPacienteLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel6))
                    .addGroup(jPInformacionPacienteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(jTFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))))
                .addGap(18, 18, 18)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jCBEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145)
                .addComponent(jLabel42)
                .addGap(359, 359, 359))
        );

        jTabbedPane1.addTab("Información del paciente", jPInformacionPaciente);

        jLabel13.setText("Presión sistólica");

        jLabel14.setText("Presión diastólica");

        jLabel15.setText("Peso");

        jLabel16.setText("Frecuencia cardíaca");

        jLabel17.setText("Motivo");

        jLabel18.setText("Temperatura");

        jLabel19.setText("Examen físico");

        jTFPresionSistolica.setEditable(false);
        jTFPresionSistolica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFPresionSistolicaActionPerformed(evt);
            }
        });

        jTAMotivo.setEditable(false);
        jTAMotivo.setColumns(20);
        jTAMotivo.setRows(5);
        jScrollPane4.setViewportView(jTAMotivo);

        jTAExamenFisico.setEditable(false);
        jTAExamenFisico.setColumns(20);
        jTAExamenFisico.setRows(5);
        jScrollPane5.setViewportView(jTAExamenFisico);

        jTFFrecuenciaCardiaca.setEditable(false);
        jTFFrecuenciaCardiaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFFrecuenciaCardiacaActionPerformed(evt);
            }
        });

        jTFPresionDiastolica.setEditable(false);
        jTFPresionDiastolica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFPresionDiastolicaActionPerformed(evt);
            }
        });

        jTFPeso.setEditable(false);
        jTFPeso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFPesoActionPerformed(evt);
            }
        });

        jTFEstatura.setEditable(false);
        jTFEstatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFEstaturaActionPerformed(evt);
            }
        });

        jLabel20.setText("Oxigenación");

        jTFOxigenacion.setEditable(false);

        jLabel21.setText("Estatura");

        jTFTemperatura.setEditable(false);
        jTFTemperatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFTemperaturaActionPerformed(evt);
            }
        });

        jLabel22.setText("mmhg");

        jLabel23.setText("mmhg");

        jLabel24.setText("°C");

        jLabel25.setText("kg");

        jLabel28.setText("m");

        jLabel29.setText("lpm");

        jLabel30.setText("%");

        jBEditar.setText("Editar");
        jBEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPAnamnesisLayout = new javax.swing.GroupLayout(jPAnamnesis);
        jPAnamnesis.setLayout(jPAnamnesisLayout);
        jPAnamnesisLayout.setHorizontalGroup(
            jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPAnamnesisLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPAnamnesisLayout.createSequentialGroup()
                        .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPAnamnesisLayout.createSequentialGroup()
                                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPAnamnesisLayout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPAnamnesisLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(65, 65, 65)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(281, 281, 281))
                            .addGroup(jPAnamnesisLayout.createSequentialGroup()
                                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTFTemperatura, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFPeso, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFPresionSistolica, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFEstatura))
                                .addGap(18, 18, 18)
                                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(64, 64, 64)
                                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTFFrecuenciaCardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTFPresionDiastolica, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTFOxigenacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))))
                    .addGroup(jPAnamnesisLayout.createSequentialGroup()
                        .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPAnamnesisLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBEditar)
                .addGap(419, 419, 419))
        );
        jPAnamnesisLayout.setVerticalGroup(
            jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPAnamnesisLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFPresionSistolica, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jTFPresionDiastolica, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTFPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jTFFrecuenciaCardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel29))
                .addGap(19, 19, 19)
                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20)
                    .addComponent(jTFOxigenacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFEstatura, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel28))
                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPAnamnesisLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPAnamnesisLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel17)))
                .addGroup(jPAnamnesisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPAnamnesisLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPAnamnesisLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jLabel19)))
                .addGap(45, 45, 45)
                .addComponent(jBEditar)
                .addContainerGap(666, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Anamnesis", jPAnamnesis);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFTemperaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFTemperaturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFTemperaturaActionPerformed

    private void jTFEstaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFEstaturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFEstaturaActionPerformed

    private void jTFPesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFPesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPesoActionPerformed

    private void jTFPresionDiastolicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFPresionDiastolicaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPresionDiastolicaActionPerformed

    private void jTFFrecuenciaCardiacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFrecuenciaCardiacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFFrecuenciaCardiacaActionPerformed

    private void jTFPresionSistolicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFPresionSistolicaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPresionSistolicaActionPerformed

    private void jTFTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFTelefonoActionPerformed

    private void jTFTelefonoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFTelefonoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFTelefonoFocusLost

    private void jTFEdadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFEdadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFEdadActionPerformed

    private void jTFEdadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFEdadFocusLost

    }//GEN-LAST:event_jTFEdadFocusLost

    private void jTFGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFGeneroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFGeneroActionPerformed

    private void jTFCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCorreoActionPerformed

    private void jTFCorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCorreoFocusLost

    }//GEN-LAST:event_jTFCorreoFocusLost

    private void jTFFechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFechaNacimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFFechaNacimientoActionPerformed

    private void jTFFechaNacimientoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFFechaNacimientoFocusLost

    }//GEN-LAST:event_jTFFechaNacimientoFocusLost

    private void jTFCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCedulaActionPerformed

    private void jTFApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFApellidosActionPerformed

    private void jTFNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNombresActionPerformed

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEditarActionPerformed
         if (this.jBEditar.getText().equals("Editar")) {
        // Modo edición
        this.jBEditar.setText("Finalizar");
        habilitarCamposEdicion(true);
        JOptionPane.showMessageDialog(this, "Editar con precaución", "Editar", JOptionPane.INFORMATION_MESSAGE);
    } else {
        // Modo finalizar - validar antes de guardar
        if (validarCamposAnamnesis()) {
            if (actualizarAnamnesisYCita()) {
                habilitarCamposEdicion(false);
                this.jBEditar.setText("Editar");
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    }//GEN-LAST:event_jBEditarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new informacion_enfermero().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBEditar;
    private javax.swing.JComboBox<String> jCBEstadoCivil;
    private javax.swing.JComboBox<String> jCBTipoIdentificador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPAnamnesis;
    private javax.swing.JPanel jPInformacionPaciente;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTAExamenFisico;
    private javax.swing.JTextArea jTAMotivo;
    private javax.swing.JTextField jTFApellidos;
    private javax.swing.JTextField jTFApellidosContacto;
    private javax.swing.JTextField jTFCedula;
    private javax.swing.JTextField jTFCedulaContacto;
    private javax.swing.JTextField jTFCorreo;
    private javax.swing.JTextField jTFEdad;
    private javax.swing.JTextField jTFEstatura;
    private javax.swing.JTextField jTFFechaNacimiento;
    private javax.swing.JTextField jTFFrecuenciaCardiaca;
    private javax.swing.JTextField jTFGenero;
    private javax.swing.JTextField jTFNombres;
    private javax.swing.JTextField jTFNombresContacto;
    private javax.swing.JTextField jTFOxigenacion;
    private javax.swing.JTextField jTFPeso;
    private javax.swing.JTextField jTFPresionDiastolica;
    private javax.swing.JTextField jTFPresionSistolica;
    private javax.swing.JTextField jTFTelefono;
    private javax.swing.JTextField jTFTelefonoContacto;
    private javax.swing.JTextField jTFTemperatura;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
