package pacientesmodule.vista;

import java.awt.Image;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import pacientesmodule.vista.resultados;
import pacientesmodule.vista.evolucion;
import pacientesmodule.vista.editar_paciente;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import pacientesmodule.conexion.ConexionSQLServer;
import pacientesmodule.pacientesDAO.AntecedentesDAO;
import pacientesmodule.pacientesDAO.CitaDAO;
import pacientesmodule.pacientesDAO.ConsultaPrevia;
import pacientesmodule.pacientesDAO.InternacionDAO;
import pacientesmodule.pacientesDAO.PacienteDAO;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author USUARIO
 */
public class informacion_doctor extends javax.swing.JFrame {
    private String cedulaDoctor;
    private String cedulaPaciente;
    private int id_cita;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(informacion_doctor.class.getName());

    /**
     * Creates new form informaciion_doctor
     */
   
           

    public informacion_doctor(String cedulaDoctor) {
        initComponents();
        this.setLocationRelativeTo(this);
        this.cedulaDoctor = cedulaDoctor;
        Map<String, String> datosPaciente = CitaDAO.obtenerPacienteConCitaMasProxima(cedulaDoctor);
        if (datosPaciente != null && !datosPaciente.isEmpty()) {
            this.cedulaPaciente=datosPaciente.getOrDefault("cedula", "");
            System.out.println("la cedula del paciente es");
            System.out.println(this.cedulaPaciente);
        }
        cargarDatosDelPaciente();
        cargarEvolucion();
        configurarValidaciones();
        cargarDatosContacto();
        cargarDatosAnamnesis();
        cargarInternaciones();
    }
    private void cargarInternaciones() {
        List<Map<String, Object>> internaciones = InternacionDAO.obtenerInternacionesPorPaciente(cedulaPaciente);
        DefaultTableModel model = (DefaultTableModel) jTableInternaciones.getModel();
        model.setRowCount(0); // Limpiar tabla
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Map<String, Object> internacion : internaciones) {
            Object[] row = {
                internacion.get("id_internacion"),
                internacion.get("tipo"),
                internacion.get("nivelDeCuidado"),
                sdf.format((Date) internacion.get("fecha_apertura")),
                internacion.get("diagnostico") != null ? internacion.get("diagnostico") : "Sin diagnóstico"
            };
            model.addRow(row);
        }
    }
    
    // Clase interna para formatear fechas en la tabla
    private class DateCellRenderer extends DefaultTableCellRenderer {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Date) {
                value = sdf.format((Date) value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
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
    
    private void cargarEvolucion() {
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) {
        // Limpiar la tabla si no hay paciente seleccionado
        DefaultTableModel model = (DefaultTableModel) jTEvolucion.getModel();
        model.setRowCount(0);
        model.addRow(new Object[]{"Seleccione un paciente primero", "", "", "", "", ""});
        return;
    }
   
   

    // Obtener los registros de evolución para el paciente
    List<Map<String, String>> evoluciones = CitaDAO.obtenerEvolucion(this.cedulaPaciente);

    // Obtener el modelo de la tabla
    DefaultTableModel model = (DefaultTableModel) jTEvolucion.getModel();
    model.setRowCount(0); // Limpiar datos existentes

    if (evoluciones.isEmpty()) {
        // Agregar una fila indicando que no hay datos
        model.addRow(new Object[]{"No hay registros", "", "", "", "", ""});
    } else {
        // Llenar la tabla con los datos
        for (Map<String, String> evolucion : evoluciones) {
            model.addRow(new Object[]{
                evolucion.getOrDefault("fecha", "N/A"),
                evolucion.getOrDefault("hora", "N/A"),
                evolucion.getOrDefault("doctor", "N/A"),
                evolucion.getOrDefault("especialidad", "N/A"),
                evolucion.getOrDefault("diagnostico", "N/A"),
                evolucion.getOrDefault("pronostico", "N/A")
            });
        }
    }

    // Configurar el ancho de las columnas
    TableColumnModel columnModel = jTEvolucion.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(80);  // Fecha
    columnModel.getColumn(1).setPreferredWidth(60);  // Hora
    columnModel.getColumn(2).setPreferredWidth(150); // Doctor
    columnModel.getColumn(3).setPreferredWidth(100); // Especialidad
    columnModel.getColumn(4).setPreferredWidth(250); // Diagnóstico
    columnModel.getColumn(5).setPreferredWidth(250); // Pronóstico
    
    // Hacer que la tabla sea seleccionable pero no editable
    jTEvolucion.setDefaultEditor(Object.class, null);
}
    
    private void cargarDatosDelPaciente() {
    Map<String, String> datos = CitaDAO.obtenerPacienteConCitaMasProxima(cedulaDoctor);
    
    if (datos != null && !datos.isEmpty()) {
        this.cedulaPaciente = datos.get("cedula");

        // --- Panel de Información del paciente ---
        jTFNombres.setText(datos.getOrDefault("nombres", ""));
        jTFApellidos.setText(datos.getOrDefault("apellidos", ""));
        this.jTFEdad.setText(datos.getOrDefault("edad", ""));
        this.jTFCedula.setText(datos.getOrDefault("cedula", ""));
        this.jTFFechaNacimiento.setText(datos.getOrDefault("fecha_nacimiento", ""));
        this.jTFGenero.setText(datos.getOrDefault("sexo", ""));
        this.jTFCorreo.setText(datos.getOrDefault("correo", ""));
        this.jTFTelefono.setText(datos.getOrDefault("telefono", ""));
        this.id_cita = Integer.parseInt(datos.getOrDefault("id_cita", "0"));
        this.jTFSangre.setText(datos.getOrDefault("sangre", ""));
        this.jCBEstadoCivil.setSelectedItem(datos.get("estado_civil"));
      
        cargarAntecedentes();
        cargarConsultasPrevias();
        cargarEvolucion();

    } else {
        limpiarCamposPaciente(); 
        cargarEvolucion();
    }
}
 private void cargarDatosDelPacienteCedula() {
    Map<String, String> datos = PacienteDAO.obtenerInformacionPacientePorCedula(cedulaPaciente);
    
    if (datos != null && !datos.isEmpty()) {
        this.cedulaPaciente = datos.get("cedula");

        // --- Panel de Información del paciente ---
        jTFNombres.setText(datos.getOrDefault("nombres", ""));
        jTFApellidos.setText(datos.getOrDefault("apellidos", ""));
        this.jTFEdad.setText(datos.getOrDefault("edad", ""));
        this.jTFCedula.setText(datos.getOrDefault("cedula", ""));
        this.jTFFechaNacimiento.setText(datos.getOrDefault("fecha_nacimiento", ""));
        this.jTFGenero.setText(datos.getOrDefault("sexo", ""));
        this.jTFCorreo.setText(datos.getOrDefault("correo", ""));
        this.jTFTelefono.setText(datos.getOrDefault("telefono", ""));
        this.id_cita = Integer.parseInt(datos.getOrDefault("id_cita", "0"));
        this.jTFSangre.setText(datos.getOrDefault("sangre", ""));
        if(datos.get("estado_civil") == ""){
            System.out.println(" no se ha escogido estado civil");
        }else if(datos.get("estado_civil")=="Casado/a"){
            this.jCBEstadoCivil.setSelectedIndex(0);
        
        }else if(datos.get("estado_civil")== "Divorciado/a"){
            this.jCBEstadoCivil.setSelectedIndex(1);
        }else if(datos.get("estado_civil")== "Viudo/a"){
            this.jCBEstadoCivil.setSelectedIndex(2);
        }else if(datos.get("estado_civil")== "Soltero/a"){
            this.jCBEstadoCivil.setSelectedIndex(3);
        }
       
        cargarAntecedentes();
        cargarConsultasPrevias();
        cargarEvolucion();

    } else {
        limpiarCamposPaciente(); 
        cargarEvolucion();
    }
}

   
    private void cargarPacienteConCitaProxima() {
        Map<String, String> datosPaciente = CitaDAO.obtenerPacienteConCitaMasProxima(cedulaDoctor);
        
        if (datosPaciente != null && !datosPaciente.isEmpty()) {
            jTFNombres.setText(datosPaciente.getOrDefault("nombres", ""));
            jTFApellidos.setText(datosPaciente.getOrDefault("apellidos", ""));
            jTFCedula.setText(datosPaciente.getOrDefault("cedula", ""));
            jTFFechaNacimiento.setText(datosPaciente.getOrDefault("fecha_nacimiento", ""));
            jTFGenero.setText(datosPaciente.getOrDefault("sexo", ""));
            jTFCorreo.setText(datosPaciente.getOrDefault("correo", ""));
            this.jTFEdad.setText(datosPaciente.getOrDefault("edad", ""));
            this.jTFTelefono.setText(datosPaciente.getOrDefault("telefono", ""));
            this.jCBTipoIdentificador.setSelectedItem(datosPaciente.getOrDefault("tipo_identificador", ""));
        if(datosPaciente.get("estado_civil") == ""){
            System.out.println(" no se ha escogido estado civil");
        }else if(datosPaciente.get("estado_civil")=="Casado/a"){
            this.jCBEstadoCivil.setSelectedIndex(0);
        
        }else if(datosPaciente.get("estado_civil")== "Divorciado/a"){
            this.jCBEstadoCivil.setSelectedIndex(1);
        }else if(datosPaciente.get("estado_civil")== "Viudo/a"){
            this.jCBEstadoCivil.setSelectedIndex(2);
        }else if(datosPaciente.get("estado_civil")== "Soltero/a"){
            this.jCBEstadoCivil.setSelectedIndex(3);
        }
       
            cargarAntecedentes();
        } else {
            limpiarCamposPaciente();
            JOptionPane.showMessageDialog(this, 
                "No tiene citas programadas para hoy o días futuros", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    private void cargarDatosAnamnesis() {
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
    
    private void limpiarCamposPaciente() {
        jTFNombres.setText("");
        jTFApellidos.setText("");
        jTFCedula.setText("");
        jTFFechaNacimiento.setText("");
        jTFGenero.setText("");
        jTFCorreo.setText("");
        this.jTFEdad.setText("");
    }
private void cargarAntecedentes() {
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) {
        limpiarTablaAntecedentes();
        return;
    }

    
    Map<String, String> antecedentes = AntecedentesDAO.obtenerAntecedentesPorPaciente(this.cedulaPaciente);

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
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) return;

    jPanelConsultasContainer.removeAll();

    List<ConsultaPrevia> consultas = CitaDAO.obtenerConsultasPrevias(this.cedulaPaciente);
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
/**
 * Obtiene el número de cédula de la fila seleccionada en la tabla de resultados
 * @return String con el número de cédula o null si no hay fila seleccionada
 */
public String obtenerCedulaSeleccionada() {
    // Verificar si hay una fila seleccionada en la tabla
    int filaSeleccionada = jTableResultados.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
            "No hay ninguna fila seleccionada",
            "Advertencia",
            JOptionPane.WARNING_MESSAGE);
        return null;
    }
    
    // Obtener el modelo de la tabla
    DefaultTableModel modelo = (DefaultTableModel) jTableResultados.getModel();
    
    // Asumimos que la cédula está en la primera columna (índice 0)
    // Si está en otra columna, ajusta el índice
    Object valorCedula = modelo.getValueAt(filaSeleccionada, 0);
    
    // Verificar que el valor no sea nulo
    if (valorCedula == null) {
        JOptionPane.showMessageDialog(this,
            "La cédula en la fila seleccionada está vacía",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return null;
    }
    
    // Convertir a String y retornar
    return valorCedula.toString();
}

private void limpiarTablaAntecedentes() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    // Limpia todas las filas
    model.setRowCount(0);
    // Opcional: añade una fila vacía para que no se vea desolada
    model.addRow(new Object[]{"", "", ""});
}
    private informacion_doctor() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField24 = new javax.swing.JTextField();
        jtpinformacionpaciente = new javax.swing.JTabbedPane();
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
        jbeditar = new javax.swing.JButton();
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
        jBEditarContacto = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTFApellidosContacto = new javax.swing.JTextField();
        jCBTipoIdentificador = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
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
        jPHistoriaClinica = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanelConsultasContainer = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTFSangre = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPEvolucion = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTEvolucion = new javax.swing.JTable();
        jPInternaciones = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableInternaciones = new javax.swing.JTable();
        jPbuscarpaciente = new javax.swing.JPanel();
        jcbbusqueda = new javax.swing.JComboBox<>();
        jtfbusqueda = new javax.swing.JTextField();
        jbbuscar = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableResultados = new javax.swing.JTable();

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

        jbeditar.setText("Editar");
        jbeditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbeditarActionPerformed(evt);
            }
        });

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

        jBEditarContacto.setText("Editar");
        jBEditarContacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarContactoActionPerformed(evt);
            }
        });

        jLabel11.setText("Telefono");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(319, 319, 319)
                        .addComponent(jBEditarContacto))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(241, 241, 241)
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
                                .addComponent(jTFTelefonoContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(276, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
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
                .addGap(45, 45, 45)
                .addComponent(jBEditarContacto)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jCBTipoIdentificador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "Otro" }));

        javax.swing.GroupLayout jPInformacionPacienteLayout = new javax.swing.GroupLayout(jPInformacionPaciente);
        jPInformacionPaciente.setLayout(jPInformacionPacienteLayout);
        jPInformacionPacienteLayout.setHorizontalGroup(
            jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInformacionPacienteLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInformacionPacienteLayout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(375, 375, 375))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInformacionPacienteLayout.createSequentialGroup()
                        .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                        .addComponent(jTFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel4)
                                .addGroup(jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInformacionPacienteLayout.createSequentialGroup()
                                .addComponent(jbeditar)
                                .addGap(199, 199, 199)))
                        .addGap(57, 57, 57)
                        .addComponent(jCBTipoIdentificador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(148, 148, 148))))
        );
        jPInformacionPacienteLayout.setVerticalGroup(
            jPInformacionPacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInformacionPacienteLayout.createSequentialGroup()
                .addGap(47, 47, 47)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(18, 18, 18)
                .addComponent(jbeditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145)
                .addComponent(jLabel42)
                .addGap(359, 359, 359))
        );

        jtpinformacionpaciente.addTab("Información del paciente", jPInformacionPaciente);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(65, 65, 65)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(281, 281, 281))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTFTemperatura, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFPeso, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFPresionSistolica, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFEstatura))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(64, 64, 64)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTFFrecuenciaCardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTFPresionDiastolica, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTFOxigenacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFPresionSistolica, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jTFPresionDiastolica, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTFPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jTFFrecuenciaCardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel29))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20)
                    .addComponent(jTFOxigenacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFEstatura, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel28))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel17)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jLabel19)))
                .addContainerGap(727, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Anamnesis", jPanel1);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 882, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel12.setText("Tipo de Sangre");

        jTFSangre.setEditable(false);

        jButton1.setText("Agregar antecedente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPHistoriaClinicaLayout = new javax.swing.GroupLayout(jPHistoriaClinica);
        jPHistoriaClinica.setLayout(jPHistoriaClinicaLayout);
        jPHistoriaClinicaLayout.setHorizontalGroup(
            jPHistoriaClinicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPHistoriaClinicaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPHistoriaClinicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPHistoriaClinicaLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jTFSangre, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPHistoriaClinicaLayout.createSequentialGroup()
                .addGap(358, 358, 358)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPHistoriaClinicaLayout.setVerticalGroup(
            jPHistoriaClinicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPHistoriaClinicaLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPHistoriaClinicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTFSangre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(97, 97, 97)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(505, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Historia clínica", jPHistoriaClinica);

        jPEvolucion.setAutoscrolls(true);

        jButton2.setText("Agregar evolución");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTEvolucion.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jTEvolucion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Hora", "Médico", "Especialidad", "Diagnóstico", "Pronóstico"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTEvolucion.setShowHorizontalLines(true);
        jTEvolucion.setShowVerticalLines(true);
        jScrollPane3.setViewportView(jTEvolucion);

        javax.swing.GroupLayout jPEvolucionLayout = new javax.swing.GroupLayout(jPEvolucion);
        jPEvolucion.setLayout(jPEvolucionLayout);
        jPEvolucionLayout.setHorizontalGroup(
            jPEvolucionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEvolucionLayout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPEvolucionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 904, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPEvolucionLayout.setVerticalGroup(
            jPEvolucionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEvolucionLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jButton2)
                .addContainerGap(747, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Evolución", jPEvolucion);

        jTableInternaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Internación", "Tipo", "Nivel de cuidado", "Fecha Apertura", "Diagnóstico"
            }
        ));
        jScrollPane7.setViewportView(jTableInternaciones);

        javax.swing.GroupLayout jPInternacionesLayout = new javax.swing.GroupLayout(jPInternaciones);
        jPInternaciones.setLayout(jPInternacionesLayout);
        jPInternacionesLayout.setHorizontalGroup(
            jPInternacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInternacionesLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPInternacionesLayout.setVerticalGroup(
            jPInternacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInternacionesLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(929, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Internaciones", jPInternaciones);

        jcbbusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "Nombre" }));

        jbbuscar.setText("Buscar");
        jbbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbbuscarActionPerformed(evt);
            }
        });

        jTableResultados.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableResultados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableResultadosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTableResultados);

        javax.swing.GroupLayout jPbuscarpacienteLayout = new javax.swing.GroupLayout(jPbuscarpaciente);
        jPbuscarpaciente.setLayout(jPbuscarpacienteLayout);
        jPbuscarpacienteLayout.setHorizontalGroup(
            jPbuscarpacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPbuscarpacienteLayout.createSequentialGroup()
                .addGap(219, 219, 219)
                .addComponent(jcbbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jtfbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jbbuscar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPbuscarpacienteLayout.createSequentialGroup()
                .addContainerGap(136, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        jPbuscarpacienteLayout.setVerticalGroup(
            jPbuscarpacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPbuscarpacienteLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPbuscarpacienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbbuscar))
                .addGap(42, 42, 42)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(756, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Buscar paciente", jPbuscarpaciente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtpinformacionpaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 890, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtpinformacionpaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbbuscarActionPerformed
        String opcion = this.jcbbusqueda.getSelectedItem().toString();
        boolean bandera= true;

        switch (opcion) {
            case "Cédula":
            String ced = this.jtfbusqueda.getText().trim();
            if (!ced.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(this,
                    "Cédula inválida. Debe contener 10 dígitos numéricos.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                    bandera=false;
                break;
            }
            int prov = Integer.parseInt(ced.substring(0, 2));
            int tercer = ced.charAt(2) - '0';
            if (prov < 1 || prov > 24 || tercer >= 6) {
                JOptionPane.showMessageDialog(this,
                    "Cédula inválida. Provincia o tercer dígito no válidos.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                    bandera=false;
                break;
            }
            int[] coef = {2,1,2,1,2,1,2,1,2};
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int d = ced.charAt(i) - '0';
                int prod = d * coef[i];
                suma += (prod > 9) ? prod - 9 : prod;
            }
            int dv = (10 - (suma % 10)) % 10;
            if (dv != (ced.charAt(9) - '0')) {
                JOptionPane.showMessageDialog(this,
                    "Cédula inválida. Dígito verificador incorrecto.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                bandera= false;
            }
            break;

            case "Nombre":
            String nom = this.jtfbusqueda.getText().trim();
            if (!nom.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
                JOptionPane.showMessageDialog(this,
                    "Nombre inválido. No debe contener números ni caracteres especiales.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                bandera= false;
            }
            break;
        }
        
        if (bandera == true) {
    String criterio = (String) jcbbusqueda.getSelectedItem();
    String valor = jtfbusqueda.getText().trim();

    if (valor.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un término de búsqueda.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    DefaultTableModel model = PacienteDAO.buscarPacientesPorDoctor(criterio, valor, this.cedulaDoctor);
    jTableResultados.setModel(model); 
    
    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "No se encontraron pacientes que coincidan con la búsqueda.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
    } else {
        // Agregar listener para cuando se seleccione una fila
        jTableResultados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String cedulaSeleccionada = obtenerCedulaSeleccionada();
                if (cedulaSeleccionada != null) {
                    // Aquí puedes usar la cédula para lo que necesites
                    System.out.println("Cédula seleccionada: " + cedulaSeleccionada);
                    this.cedulaPaciente = cedulaSeleccionada;
                    cargarDatosDelPacienteCedula();
                    cargarEvolucion();
                    configurarValidaciones();
                    cargarDatosContacto();
                    cargarDatosAnamnesis();
                    
                    
                }
            }
        });
    }
}
    }//GEN-LAST:event_jbbuscarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       // evolucion vistaEvolucion = new evolucion(this.id_cita);
        //vistaEvolucion.setVisible(true);
        
        //cargarDatosDelPaciente();
        evolucion vistaEvolucion = new evolucion(this.id_cita);
        vistaEvolucion.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e){
                cargarDatosDelPaciente();
            }
        });
        vistaEvolucion.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

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

    private void jBEditarContactoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEditarContactoActionPerformed
        if (this.jBEditarContacto.getText().equals("Finalizar")) {
            // Validar todos los campos antes de finalizar (incluyendo teléfono)
            if (validarCamposContacto()) {
                // Guardar cambios en la BD
                if (guardarCambiosContacto()) {
                    JOptionPane.showMessageDialog(this,
                        "Cambios guardados correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                    // Deshabilitar edición
                    jTFCedulaContacto.setEditable(false);
                    jTFNombresContacto.setEditable(false);
                    jTFApellidosContacto.setEditable(false);
                    jTFTelefonoContacto.setEditable(false); // Nuevo campo
                    jBEditarContacto.setText("Editar");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Editar con precaución","Editar",JOptionPane.INFORMATION_MESSAGE);
            // Habilitar edición
            if (this.jTFCedulaContacto.getText() == ""){
                jTFCedulaContacto.setEditable(true);
            }else{
                this.jTFCedulaContacto.setEditable(false);
            }

            jTFNombresContacto.setEditable(true);
            jTFApellidosContacto.setEditable(true);
            jTFTelefonoContacto.setEditable(true); // Nuevo campo
            jBEditarContacto.setText("Finalizar");
        }
    }//GEN-LAST:event_jBEditarContactoActionPerformed

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
        String edadStr = this.jTFEdad.getText().trim();
        if (!edadStr.isEmpty() && validarEdad(edadStr)) {
            actualizarCampoEnBD("edad", Integer.parseInt(edadStr));
        }
    }//GEN-LAST:event_jTFEdadFocusLost

    private void jTFGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFGeneroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFGeneroActionPerformed

    private void jbeditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbeditarActionPerformed
        // vista.editar_paciente editarpaciente = new vista.editar_paciente();
        //editarpaciente.setVisible(true);
        if (this.jbeditar.getText().equals("Finalizar")) {
            // Validar todos los campos antes de finalizar

            if (validarTodosLosCampos()) {
                ActualizarTelefono();
                actualizarEstadoCivil();
                this.jTFFechaNacimiento.setEditable(false);
                this.jTFGenero.setEditable(false);
                this.jTFCorreo.setEditable(false);
                this.jTFEdad.setEditable(false);
                this.jbeditar.setText("Editar");
                this.jCBEstadoCivil.setEditable(false);
                JOptionPane.showMessageDialog(this, "Cambios guardados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            if(this.jTFFechaNacimiento.getText().equals("")){
                System.out.println("No hay fecha");
                this.jTFFechaNacimiento.setEditable(true);
            }else{
                this.jTFFechaNacimiento.setEditable(false);
                //this.jTFFechaNacimiento.setEnabled(false);
                System.out.println("else");

            }
            //this.jTFNombres.setEnabled(false);
            //this.jTFApellidos.setEnabled(false);
            // this.jTFCedula.setEnabled(false);
            this.jCBEstadoCivil.setEditable(true);
            this.jTFTelefono.setEditable(true);
            this.jTFGenero.setEditable(true);
            this.jTFCorreo.setEditable(true);
            this.jTFEdad.setEditable(true);
            JOptionPane.showMessageDialog(this, "Editar con precaución","Editar",JOptionPane.INFORMATION_MESSAGE);
            this.jbeditar.setText("Finalizar");
        }
    }//GEN-LAST:event_jbeditarActionPerformed

    private void jTFCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCorreoActionPerformed

    private void jTFCorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCorreoFocusLost
        String correo = this.jTFCorreo.getText().trim();
        if (!correo.isEmpty() && validarCorreo(correo)) {
            actualizarCampoEnBD("correo", correo);
        }
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

    private void jTableResultadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableResultadosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableResultadosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Map<String, String> antecedentes = AntecedentesDAO.obtenerAntecedentesPorPaciente(this.cedulaPaciente);
        String familiares = antecedentes.getOrDefault("familiares","");
        String patologicos = antecedentes.getOrDefault("patologicos", "");
        String fisiologicos = antecedentes.getOrDefault("fisiologicos","");
        String enfermedades_actuales = antecedentes.getOrDefault("enfermedades_actuales", "");
        Antecedentes antecedentesvista = new Antecedentes(familiares,patologicos,fisiologicos,enfermedades_actuales,this.cedulaPaciente);
        antecedentesvista.setVisible(true);
        antecedentesvista.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
        // Cuando la ventana se cierra, cargar los antecedentes actualizados
        cargarAntecedentes();
        }
});
        
                
    }//GEN-LAST:event_jButton1ActionPerformed

    private void configurarValidaciones() {
    // Configurar validación para todos los campos editables
    configurarValidacionFecha();
    configurarValidacionNombres();
    configurarValidacionApellidos();
    configurarValidacionSexo();
    configurarValidacionCorreo();
    configurarValidacionEdad();
}
    
private boolean validarTodosLosCampos() {
    boolean todosValidos = true;
    StringBuilder errores = new StringBuilder();
    
    // Validar fecha
    String fecha = jTFFechaNacimiento.getText().trim();
    if (!validarFechaSQL(fecha)) {
        errores.append("- Fecha de nacimiento inválida\n");
        this.jTFFechaNacimiento.setText("");
        todosValidos = false;
    }
    String telefono = this.jTFTelefono.getText().trim();
    if (!validarTelefono(telefono)) {
        errores.append("- Telefono inválida\n");
        this.jTFTelefono.setText("");
        todosValidos = false;
    }
    
    // Validar género
    String genero = jTFGenero.getText().trim();
    if (!validarSexo(genero)) {
        errores.append("- Género inválido\n");
        this.jTFGenero.setText("");
        todosValidos = false;
    }
    
    // Validar correo
    String correo = jTFCorreo.getText().trim();
    if (!validarCorreo(correo)) {
        errores.append("- Correo electrónico inválido\n");
        this.jTFCorreo.setText("");
        todosValidos = false;
    }
    
    // Validar edad
    String edad = jTFEdad.getText().trim();
    if (!validarEdad(edad)) {
        errores.append("- Edad inválida\n");
        this.jTFEdad.setText("");
        todosValidos = false;
    }
    
    if (!todosValidos) {
        JOptionPane.showMessageDialog(this, 
            "Por favor corrija los siguientes errores:\n" + errores.toString(), 
            "Errores de validación", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    return todosValidos;
}
private void validarCedula(String cedula) {
    if (!cedula.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(this, "Cédula inválida");
        return;
    }
}
private void configurarValidacionEdad() {
    jTFEdad.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            String edadStr = jTFEdad.getText().trim();
            if (!edadStr.isEmpty() && validarEdad(edadStr)) {
                try {
                    int edad = Integer.parseInt(edadStr);
                    actualizarCampoEnBD("edad", edad);
                } catch (NumberFormatException e) {
                    // Ya se maneja en validarEdad
                }
            }
        }
    });
}


private void configurarValidacionNombres() {
    jTFNombres.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            String nombres = jTFNombres.getText().trim();
            if (!nombres.isEmpty() && validarNombres(nombres)) {
                actualizarCampoEnBD("nombres", nombres);
            }
        }
    });
}

private void configurarValidacionApellidos() {
    jTFApellidos.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            String apellidos = jTFApellidos.getText().trim();
            if (!apellidos.isEmpty() && validarApellidos(apellidos)) {
                actualizarCampoEnBD("apellidos", apellidos);
            }
        }
    });
}

private void configurarValidacionSexo() {
    jTFGenero.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            String sexo = jTFGenero.getText().trim();
            if (!sexo.isEmpty() && validarSexo(sexo)) {
                actualizarCampoEnBD("sexo", sexo);
            }
        }
    });
}

private void configurarValidacionCorreo() {
    jTFCorreo.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            String correo = jTFCorreo.getText().trim();
            if (!correo.isEmpty() && validarCorreo(correo)) {
                actualizarCampoEnBD("correo", correo);
            }
        }
    });
}



// Métodos de validación
private boolean validarNombres(String nombres) {
    if (!nombres.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
        
        jTFNombres.requestFocus();
        return false;
    }
    return true;
}
private boolean validarEdad(String edadStr) {
    if (edadStr.isEmpty()) {
        return false;
    }
    try {
        int edad = Integer.parseInt(edadStr);
        if (edad < 0 || edad > 120) {
            
            return false;
        }
        return true;
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "Edad inválida. Debe ser un número entero", 
            "Error de validación", 
            JOptionPane.ERROR_MESSAGE);
        jTFEdad.requestFocus();
        return false;
    }
}

private boolean validarApellidos(String apellidos) {
    if (!apellidos.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
        
        jTFApellidos.requestFocus();
        return false;
    }
    return true;
}

private boolean validarSexo(String sexo) {
    if (!sexo.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
        
        this.jTFGenero.requestFocus();
        return false;
    }
    return true;
}

private boolean validarCorreo(String correo) {
    if (correo.isEmpty()) {
        return false;
    }
    if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        
        jTFCorreo.requestFocus();
        return false;
    }
    return true;
}

// Método para actualizar cualquier campo en la base de datos
private boolean actualizarCampoEnBD(String campo, Object valor) {
   if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "No hay paciente seleccionado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return false;
    }

    String sql = "UPDATE Paciente SET " + campo + " = ? WHERE cedula = ?";
    
    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        if (valor instanceof java.sql.Date) {
            pstmt.setDate(1, (java.sql.Date) valor);
        } else if (valor instanceof Integer) {
            pstmt.setInt(1, (Integer) valor);
        } else if (valor instanceof String) {
            pstmt.setString(1, (String) valor);
        } else {
            pstmt.setObject(1, valor);
        }
        
        pstmt.setString(2, this.cedulaPaciente);
        
        int filasAfectadas = pstmt.executeUpdate();
        return filasAfectadas > 0;
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, 
            "Error al actualizar " + campo + ": " + e.getMessage(), 
            "Error de base de datos", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(this, 
            "Error: Driver JDBC no encontrado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}
private void configurarValidacionFecha() {
    jTFFechaNacimiento.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusLost(java.awt.event.FocusEvent evt) {
            String fechaStr = jTFFechaNacimiento.getText().trim();
            
            if (!fechaStr.isEmpty() && validarFechaSQL(fechaStr)) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date fechaUtil = sdf.parse(fechaStr);
                    java.sql.Date fechaNacimiento = new java.sql.Date(fechaUtil.getTime());
                    
                    actualizarCampoEnBD("fecha_nacimiento", fechaNacimiento);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(informacion_doctor.this, 
                        "Error al procesar la fecha", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    });
}
private void ActualizarTelefono() {
    String telefono = jTFTelefono.getText().trim();
    if (telefono.isEmpty()) {
        actualizarCampoEnBD("telefono", telefono);
    } else {
        if (validarTelefono(telefono)) {
            actualizarCampoEnBD("telefono", telefono);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Teléfono inválido. Debe contener 10 dígitos.", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            jTFTelefono.requestFocus();
        }
    }
}

private void actualizarEstadoCivil() {
    String estadoCivil = (String) jCBEstadoCivil.getSelectedItem();
    actualizarCampoEnBD("estado_civil", estadoCivil);
}
private boolean validarTelefono(String telefono) {
    return telefono.matches("\\d{10}");
}


// Método de validación de fecha (como antes)
private boolean validarFechaSQL(String fecha) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date date = sdf.parse(fecha);
        
        // Verificar que no sea fecha futura
        if (date.after(new Date())) {
            if (fecha.isEmpty()) {
        return false;
    }
            JOptionPane.showMessageDialog(this, 
                "La fecha no puede ser futura", 
                "Error de fecha", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, 
            "Formato de fecha inválido. Use YYYY-MM-DD", 
            "Error de formato", 
            JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

   
    private boolean validarCamposContacto() {
     boolean validos = true;
    StringBuilder errores = new StringBuilder();
    
    // Validar cédula
    String cedula = jTFCedulaContacto.getText().trim();
    if (!validarCedulaContacto(cedula)) {
        errores.append("- Cédula de contacto inválida\n");
        validos = false;
    }
    
    // Validar nombres
    String nombres = jTFNombresContacto.getText().trim();
    if (!validarNombresContacto(nombres)) {
        errores.append("- Nombres de contacto inválidos\n");
        validos = false;
    }
    
    // Validar apellidos
    String apellidos = jTFApellidosContacto.getText().trim();
    if (!validarApellidosContacto(apellidos)) {
        errores.append("- Apellidos de contacto inválidos\n");
        validos = false;
    }
    
    // Validar teléfono (nueva validación)
    String telefono = jTFTelefonoContacto.getText().trim();
    if (!telefono.isEmpty() && !validarTelefono(telefono)) {
        errores.append("- Teléfono de contacto inválido\n");
        validos = false;
    }
    
    if (!validos) {
        JOptionPane.showMessageDialog(this, 
            "Por favor corrija los siguientes errores:\n" + errores.toString(), 
            "Errores de validación", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    return validos;
}
    

private boolean validarCedulaContacto(String cedula) {
    if (cedula.isEmpty()) {
        return false;
    }
    
    // Validación básica de cédula (puedes usar la misma que para el paciente)
    return cedula.matches("\\d{10}");
}

private boolean validarNombresContacto(String nombres) {
    return !nombres.isEmpty() && nombres.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+");
}

private boolean validarApellidosContacto(String apellidos) {
    return !apellidos.isEmpty() && apellidos.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+");
}
private boolean guardarCambiosContacto() {
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "No hay paciente seleccionado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return false;
    }

    String cedulaContacto = jTFCedulaContacto.getText().trim();
    String nombres = jTFNombresContacto.getText().trim();
    String apellidos = jTFApellidosContacto.getText().trim();
    String telefono = jTFTelefonoContacto.getText().trim();

    try (Connection conn = ConexionSQLServer.conectar()) {
        // Primero verificar si ya existe un contacto para este paciente
        String sqlCheck = "SELECT id_contacto FROM Paciente WHERE cedula = ?";
        try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setString(1, this.cedulaPaciente);
            ResultSet rs = pstmtCheck.executeQuery();
            
            if (rs.next()) {
                String idContactoExistente = rs.getString("id_contacto");
                
                if (idContactoExistente != null && !idContactoExistente.isEmpty()) {
                    // Actualizar contacto existente
                    String sqlUpdate = "UPDATE Contacto SET nombres = ?, apellidos = ?, telefono = ? WHERE cedula = ?";
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setString(1, nombres);
                        pstmtUpdate.setString(2, apellidos);
                        pstmtUpdate.setString(3, telefono);
                        pstmtUpdate.setString(4, idContactoExistente);
                        
                        int updated = pstmtUpdate.executeUpdate();
                        
                        if (updated > 0) {
                            // Si la cédula del contacto cambió, actualizamos también el id_contacto en Paciente
                            if (!idContactoExistente.equals(cedulaContacto)) {
                                String sqlUpdatePaciente = "UPDATE Paciente SET id_contacto = ? WHERE cedula = ?";
                                try (PreparedStatement pstmtUpdatePaciente = conn.prepareStatement(sqlUpdatePaciente)) {
                                    pstmtUpdatePaciente.setString(1, cedulaContacto);
                                    pstmtUpdatePaciente.setString(2, this.cedulaPaciente);
                                    return pstmtUpdatePaciente.executeUpdate() > 0;
                                }
                            }
                            return true;
                        }
                    }
                }
            }
            
            // Si no existe, crear nuevo contacto
            String sqlInsert = "INSERT INTO Contacto (cedula, nombres, apellidos, telefono) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, cedulaContacto);
                pstmtInsert.setString(2, nombres);
                pstmtInsert.setString(3, apellidos);
                pstmtInsert.setString(4, telefono);
                
                int affectedRows = pstmtInsert.executeUpdate();
                
                if (affectedRows > 0) {
                    // Actualizar referencia en Paciente usando la misma cédula como id_contacto
                    String sqlUpdatePaciente = "UPDATE Paciente SET id_contacto = ? WHERE cedula = ?";
                    try (PreparedStatement pstmtUpdatePaciente = conn.prepareStatement(sqlUpdatePaciente)) {
                        pstmtUpdatePaciente.setString(1, cedulaContacto);
                        pstmtUpdatePaciente.setString(2, this.cedulaPaciente);
                        return pstmtUpdatePaciente.executeUpdate() > 0;
                    }
                }
            }
        }
    } catch (SQLException | ClassNotFoundException e) {
        JOptionPane.showMessageDialog(this, 
            "Error al guardar datos de contacto: " + e.getMessage(), 
            "Error de base de datos", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    return false;
}
    /**
     * @param args the command line arguments
     */

    /**
     * @param args the command line arguments
     */

    /**
     * @param args the command line arguments
     */

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
        java.awt.EventQueue.invokeLater(() -> new informacion_doctor().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBEditarContacto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jCBEstadoCivil;
    private javax.swing.JComboBox<String> jCBTipoIdentificador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPEvolucion;
    private javax.swing.JPanel jPHistoriaClinica;
    private javax.swing.JPanel jPInformacionPaciente;
    private javax.swing.JPanel jPInternaciones;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelConsultasContainer;
    private javax.swing.JPanel jPbuscarpaciente;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextArea jTAExamenFisico;
    private javax.swing.JTextArea jTAMotivo;
    private javax.swing.JTable jTEvolucion;
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
    private javax.swing.JTextField jTFSangre;
    private javax.swing.JTextField jTFTelefono;
    private javax.swing.JTextField jTFTelefonoContacto;
    private javax.swing.JTextField jTFTemperatura;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableInternaciones;
    private javax.swing.JTable jTableResultados;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JButton jbbuscar;
    private javax.swing.JButton jbeditar;
    private javax.swing.JComboBox<String> jcbbusqueda;
    private javax.swing.JTextField jtfbusqueda;
    private javax.swing.JTabbedPane jtpinformacionpaciente;
    // End of variables declaration//GEN-END:variables
}
