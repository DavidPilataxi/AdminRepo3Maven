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
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class Especialidades extends javax.swing.JPanel {

    DefaultTableModel modelo;
    int idSeleccionado = -1;

    /**
     * Creates new form Especialidades
     */
    public Especialidades() {
        initComponents();
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción"}, 0);
        tablaEspecialidades.setModel(modelo);
        // Centramos los títulos de las columnas
        ((DefaultTableCellRenderer) tablaEspecialidades.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        // Estilo general de la tabla
        tablaEspecialidades.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Desactiva redimensionado automático
        tablaEspecialidades.setRowHeight(25); // Altura de cada fila

// Ancho personalizado de columnas
        tablaEspecialidades.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tablaEspecialidades.getColumnModel().getColumn(1).setPreferredWidth(180); // Nombre
        tablaEspecialidades.getColumnModel().getColumn(2).setPreferredWidth(800); // Descripción

// Estilo de cabecera
        tablaEspecialidades.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaEspecialidades.getTableHeader().setBackground(new Color(51, 102, 255));
        tablaEspecialidades.getTableHeader().setForeground(Color.WHITE);

// Centrar columna ID
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaEspecialidades.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        cargarDatos();
        validarCampos();

        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCampos();
            }
        });
        txtDescripcion.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validarCampos();
            }
        });

        tablaEspecialidades.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaEspecialidades.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = Integer.parseInt(tablaEspecialidades.getValueAt(fila, 0).toString());
                    txtNombre.setText(tablaEspecialidades.getValueAt(fila, 1).toString());
                    txtDescripcion.setText(tablaEspecialidades.getValueAt(fila, 2).toString());
                    validarCampos();
                }
            }
        });

        btnGuardar.addActionListener(e -> guardarEspecialidad());
        btnActualizar.addActionListener(e -> actualizarEspecialidad());
        //btnEliminar.addActionListener(e -> eliminarEspecialidad());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void validarCampos() {
        boolean habilitado = !txtNombre.getText().trim().isEmpty() && !txtDescripcion.getText().trim().isEmpty();
        btnGuardar.setEnabled(habilitado);
        btnActualizar.setEnabled(habilitado && idSeleccionado != -1);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        idSeleccionado = -1;
        tablaEspecialidades.clearSelection();
        validarCampos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        try (Connection conn = ConectorDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Especialidades")) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("IdEspecialidad"),
                    rs.getString("Nombre"),
                    rs.getString("Descripcion")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar especialidades: " + ex.getMessage());
        }
    }

    private boolean contieneSoloLetras(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
    }

    private void guardarEspecialidad() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        if (!contieneSoloLetras(nombre) || !contieneSoloLetras(descripcion)) {
            JOptionPane.showMessageDialog(this, "Solo se permiten letras y espacios en los campos.");
            return;
        }

        try (Connection conn = ConectorDB.conectar(); PreparedStatement ps = conn.prepareStatement("INSERT INTO Especialidades (Nombre, Descripcion) VALUES (?, ?)");) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Especialidad guardada exitosamente.");
            cargarDatos();
            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void actualizarEspecialidad() {
        if (idSeleccionado == -1) {
            return;
        }

        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        if (!contieneSoloLetras(nombre) || !contieneSoloLetras(descripcion)) {
            JOptionPane.showMessageDialog(this, "Solo se permiten letras y espacios en los campos.");
            return;
        }
        try (Connection conn = ConectorDB.conectar(); PreparedStatement ps = conn.prepareStatement("UPDATE Especialidades SET Nombre = ?, Descripcion = ? WHERE IdEspecialidad = ?");) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, idSeleccionado);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Especialidad actualizada.");
            cargarDatos();
            limpiarCampos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminarEspecialidad() {
        if (idSeleccionado == -1) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar la especialidad seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = ConectorDB.conectar()) {
                // Primero verifica si algún médico tiene asignada esta especialidad
                PreparedStatement psVerificar = conn.prepareStatement("SELECT COUNT(*) FROM Medicos WHERE IdEspecialidad = ?");
                psVerificar.setInt(1, idSeleccionado);
                ResultSet rs = psVerificar.executeQuery();
                rs.next();
                int cantidad = rs.getInt(1);

                if (cantidad > 0) {
                    JOptionPane.showMessageDialog(this, "No se puede eliminar. Esta especialidad está asignada a uno o más médicos.");
                    return;
                }

                // Si no está en uso, entonces sí eliminar
                PreparedStatement psEliminar = conn.prepareStatement("DELETE FROM Especialidades WHERE IdEspecialidad = ?");
                psEliminar.setInt(1, idSeleccionado);
                psEliminar.executeUpdate();

                JOptionPane.showMessageDialog(this, "Especialidad eliminada.");
                cargarDatos();
                limpiarCampos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEspecialidades = new javax.swing.JTable();

        setBackground(new java.awt.Color(204, 238, 252));

        jLabel3.setText("Descripción(Obligatorio):");
        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N

        btnGuardar.setText("Guardar");
        btnGuardar.setBackground(new java.awt.Color(0, 51, 255));
        btnGuardar.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));

        btnActualizar.setText("Actualizar");
        btnActualizar.setBackground(new java.awt.Color(0, 55, 251));
        btnActualizar.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setToolTipText("");

        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBackground(new java.awt.Color(0, 55, 251));
        btnLimpiar.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Datos de Especialidad:");
        jLabel2.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 255));

        jLabel1.setText("Nombre de la Especialidad(Obligatorio):");
        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(255, 204, 204));

        tablaEspecialidades.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaEspecialidades);

        jScrollPane2.setViewportView(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnActualizar)
                    .addComponent(btnLimpiar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaEspecialidades;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
