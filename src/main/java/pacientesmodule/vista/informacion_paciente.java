/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import pacientesmodule.pacientesDAO.AntecedentesDAO;
import pacientesmodule.pacientesDAO.PacienteDAO;
import pacientesmodule.pacientesDAO.CitaDAO;
import pacientesmodule.pacientesDAO.ConsultaPrevia;

/**
 *
 * @author USUARIO
 */
public class informacion_paciente extends javax.swing.JFrame {
    private int cedula;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(informacion_paciente.class.getName());

    /**
     * Creates new form informacion_paciente
     * */
    public informacion_paciente() {
        initComponents();
        this.setLocationRelativeTo(this);
    }
    

    public informacion_paciente(int cedula) {
        this.cedula = cedula;
        initComponents();
        this.setLocationRelativeTo(this);
        informacionPaciente();
        cargarConsultasPrevias();
        cargarAntecedentes();
        
    }
    
    private void informacionPaciente(){
    String cedulaStr= String.valueOf(this.cedula);
    Map<String,String> datosPaciente = PacienteDAO.obtenerInformacionPacientePorCedula(cedulaStr);
    if (!datosPaciente.isEmpty()) {
            // Mostrar los datos en los componentes de la interfaz
            mostrarDatosEnInterfaz(datosPaciente);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontró ningún paciente con la cédula: " + this.cedula, 
                "Paciente no encontrado", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void mostrarDatosEnInterfaz(Map<String, String> datosPaciente){
    try {
            // Asumiendo que tienes componentes como jTextFieldNombres, jTextFieldApellidos, etc.
            this.jTFCedula.setText(datosPaciente.get("cedula"));
            this.jTFNombres.setText(datosPaciente.get("nombres"));
            this.jTFApellidos.setText(datosPaciente.get("apellidos"));
            this.jTFechaNacimiento.setText(datosPaciente.get("fecha_nacimiento"));
            this.jTFGenero.setText(datosPaciente.get("sexo"));
            this.jTFCorreo.setText(datosPaciente.get("correo"));
            this.jTFAlergias.setText(datosPaciente.get("alergias"));
            this.jTFEdad.setText(datosPaciente.get("edad"));
            this.jTFTelefono1.setText(datosPaciente.get("telefono"));
            this.jCBEstadoCivil.setSelectedItem(datosPaciente.get("estado_civil"));
            this.jTFSangre.setText(datosPaciente.get("sangre"));
            this.jCBTipoIdentificador.setSelectedItem(datosPaciente.getOrDefault("tipo_identificador", ""));
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al mostrar datos del paciente", e);
            JOptionPane.showMessageDialog(this, 
                "Error al mostrar los datos del paciente", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cargarAntecedentes() {
        String cedulaStr= String.valueOf(this.cedula);
    if (cedulaStr == null || cedulaStr.isEmpty()) {
        limpiarTablaAntecedentes();
        return;
    }

    // Llamamos al DAO actualizado
    Map<String, String> antecedentes = AntecedentesDAO.obtenerAntecedentesPorPaciente(cedulaStr);

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    if (antecedentes != null && !antecedentes.isEmpty()) {
        // Aseguramos que la tabla tenga al menos una fila para escribir
        if (model.getRowCount() == 0) {
            model.addRow(new Object[model.getColumnCount()]);
        }
        // Llenamos la primera fila (fila 0) con los datos
        model.setValueAt(antecedentes.get("familiares"), 0, 0);
        model.setValueAt(antecedentes.get("patologicos"), 0, 1);
        model.setValueAt(antecedentes.get("fisiologicos"), 0, 2);
        model.setValueAt(antecedentes.get("enfermedades_actuales"), 0, 3); // Nueva columna
    } else {
        // Si no hay antecedentes, limpiamos la tabla
        limpiarTablaAntecedentes();
    }
}
    private void cargarConsultasPrevias() {
    String cedulaStr= String.valueOf(this.cedula);
    if (cedulaStr == null || cedulaStr.isEmpty()) return;

    jPanelConsultasContainer.removeAll();

    List<ConsultaPrevia> consultas = CitaDAO.obtenerConsultasPrevias(cedulaStr);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    if (consultas.isEmpty()) {
        jPanelConsultasContainer.add(new JLabel("No se encontraron consultas previas."));
    } else {
        for (ConsultaPrevia consulta : consultas) {
            JPanel consultaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

            JTextField fechaField = new JTextField(sdf.format(consulta.getFecha()), 10);
            fechaField.setEditable(false);
            
            // CORRECCIÓN: Se vuelve a añadir el campo para la especialidad.
            JTextField espField = new JTextField(consulta.getEspecialidad(), 15);
            espField.setEditable(false);

            JTextField docField = new JTextField(consulta.getNombreDoctor(), 20);
            docField.setEditable(false);

            JButton btnResultados = new JButton("Resultados");
            
            btnResultados.putClientProperty("id_cita", consulta.getIdCita());

            btnResultados.addActionListener(e -> {
                int idCitaSeleccionada = (int) ((JButton) e.getSource()).getClientProperty("id_cita");
                resultados ventanaResultados = new resultados(idCitaSeleccionada);
                ventanaResultados.setVisible(true);
            });

            consultaPanel.add(new JLabel("Fecha:"));
            consultaPanel.add(fechaField);
            // CORRECCIÓN: Se vuelve a añadir la etiqueta y el campo de especialidad.
            consultaPanel.add(new JLabel("Especialidad:"));
            consultaPanel.add(espField);
            consultaPanel.add(new JLabel("Médico:"));
            consultaPanel.add(docField);
            consultaPanel.add(btnResultados);

            jPanelConsultasContainer.add(consultaPanel);
        }
    }

    jPanelConsultasContainer.revalidate();
    jPanelConsultasContainer.repaint();
}
   private void limpiarTablaAntecedentes() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    // Limpia todas las filas
    model.setRowCount(0);
    // Opcional: añade una fila vacía para que no se vea desolada
    model.addRow(new Object[]{"", "", ""});
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFNombres = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTFApellidos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTFCedula = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTFechaNacimiento = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFCorreo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTFAlergias = new javax.swing.JTextField();
        jTFGenero = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTFEdad = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTFSangre = new javax.swing.JTextField();
        jCBEstadoCivil = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTFTelefono1 = new javax.swing.JTextField();
        jCBTipoIdentificador = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jPHistoriaClinica = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanelConsultasContainer = new javax.swing.JPanel();

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

        jLabel3.setText("Identificador");

        jTFCedula.setEditable(false);
        jTFCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCedulaActionPerformed(evt);
            }
        });

        jLabel4.setText("Fecha de nacimiento");

        jTFechaNacimiento.setEditable(false);
        jTFechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFechaNacimientoActionPerformed(evt);
            }
        });

        jLabel5.setText("Genero");

        jLabel6.setText("correo electrónico");

        jTFCorreo.setEditable(false);
        jTFCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCorreoActionPerformed(evt);
            }
        });

        jLabel26.setText("Alergias");

        jTFAlergias.setEditable(false);

        jTFGenero.setEditable(false);
        jTFGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFGeneroActionPerformed(evt);
            }
        });

        jLabel10.setText("Edad");

        jTFEdad.setEditable(false);
        jTFEdad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFEdadActionPerformed(evt);
            }
        });

        jLabel11.setText("Teléfono");

        jTFSangre.setEditable(false);
        jTFSangre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFSangreActionPerformed(evt);
            }
        });

        jCBEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Casado/a", "Divorciado/a", "Viduo/a", "Soltero/a" }));

        jLabel27.setText("Estado civil");

        jLabel12.setText("Tipo de sangre");

        jTFTelefono1.setEditable(false);
        jTFTelefono1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFTelefono1ActionPerformed(evt);
            }
        });

        jCBTipoIdentificador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "Otro" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(302, 302, 302)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTFCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTFEdad)
                                    .addComponent(jTFCorreo)
                                    .addComponent(jTFGenero)
                                    .addComponent(jTFAlergias)
                                    .addComponent(jCBEstadoCivil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTFTelefono1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTFSangre, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31)
                        .addComponent(jCBTipoIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 140, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTFNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBTipoIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTFAlergias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTFEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jTFTelefono1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jCBEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTFSangre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(351, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mi información", jPanel1);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Antecedentes"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Abuelo paterno: cáncer renal", "Hipertensión arterial", "10 horas de sueño", null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                " Familiares", " Patológicos", "Fisiológicos", "Enfermedades actuales"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 862, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consultas previas"));

        jPanelConsultasContainer.setLayout(new javax.swing.BoxLayout(jPanelConsultasContainer, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(jPanelConsultasContainer);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPHistoriaClinicaLayout = new javax.swing.GroupLayout(jPHistoriaClinica);
        jPHistoriaClinica.setLayout(jPHistoriaClinicaLayout);
        jPHistoriaClinicaLayout.setHorizontalGroup(
            jPHistoriaClinicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPHistoriaClinicaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPHistoriaClinicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPHistoriaClinicaLayout.setVerticalGroup(
            jPHistoriaClinicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPHistoriaClinicaLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 890, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPHistoriaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 752, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPHistoriaClinica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Historia clínica", jPanel3);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNombresActionPerformed

    private void jTFApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFApellidosActionPerformed

    private void jTFCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCedulaActionPerformed

    private void jTFechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFechaNacimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFechaNacimientoActionPerformed

    private void jTFCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCorreoActionPerformed

    private void jTFGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFGeneroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFGeneroActionPerformed

    private void jTFEdadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFEdadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFEdadActionPerformed

    private void jTFSangreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSangreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFSangreActionPerformed

    private void jTFTelefono1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFTelefono1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFTelefono1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new informacion_paciente().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jCBEstadoCivil;
    private javax.swing.JComboBox<String> jCBTipoIdentificador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPHistoriaClinica;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelConsultasContainer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTFAlergias;
    private javax.swing.JTextField jTFApellidos;
    private javax.swing.JTextField jTFCedula;
    private javax.swing.JTextField jTFCorreo;
    private javax.swing.JTextField jTFEdad;
    private javax.swing.JTextField jTFGenero;
    private javax.swing.JTextField jTFNombres;
    private javax.swing.JTextField jTFSangre;
    private javax.swing.JTextField jTFTelefono1;
    private javax.swing.JTextField jTFechaNacimiento;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
