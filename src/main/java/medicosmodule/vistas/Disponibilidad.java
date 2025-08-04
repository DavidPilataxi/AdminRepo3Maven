/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package medicosmodule.vistas;

import  medicosmodule.conexion.ConectorDB;
import java.awt.BorderLayout;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Disponibilidad extends javax.swing.JPanel {
    DefaultTableModel modelo;
    
    public Disponibilidad() {
        
        initComponents();
        setLayout(new BorderLayout());
        add(jPanel1, BorderLayout.CENTER);

        modelo = new DefaultTableModel(new String[]{"Desde", "Hasta", "Estado"}, 0);
        tablaVacacionesMedico.setModel(modelo);

        cargarMedicosEnCombo();

        cmbMedicoDisponibilidad.addActionListener(e -> cargarVacacionesDeMedico());
        btnVerificarDisponibilidad.addActionListener(e -> verificarDisponibilidad());
    }
     private void cargarMedicosEnCombo() {
        cmbMedicoDisponibilidad.removeAllItems();
        try (Connection conn = ConectorDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Nombre, Apellido FROM Medicos")) {
            while (rs.next()) {
                String nombreCompleto = rs.getString("Nombre") + " " + rs.getString("Apellido");
                cmbMedicoDisponibilidad.addItem(nombreCompleto);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar médicos: " + ex.getMessage());
        }
    }

    private void cargarVacacionesDeMedico() {
        modelo.setRowCount(0);
        String medicoSeleccionado = (String) cmbMedicoDisponibilidad.getSelectedItem();
        if (medicoSeleccionado == null) return;

        try (Connection conn = ConectorDB.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT FechaDesde, FechaHasta, Estado " +
                "FROM EstadoMedico " +
                "WHERE IdMedico = (SELECT IdMedico FROM Medicos WHERE CONCAT(Nombre, ' ', Apellido) = ?) " +
                "AND Estado = 'Vacaciones' " +
                "ORDER BY FechaDesde"
            )) {

            ps.setString(1, medicoSeleccionado);
            ResultSet rs = ps.executeQuery();

            java.util.List<IntervaloVacaciones> intervalos = new java.util.ArrayList<>();

            while (rs.next()) {
                LocalDate desde = rs.getDate("FechaDesde").toLocalDate();
                LocalDate hasta = rs.getDate("FechaHasta").toLocalDate();

                boolean agregado = false;
                for (IntervaloVacaciones intervalo : intervalos) {
                    if (intervalo.seSuperponeCon(desde, hasta)) {
                        intervalo.unirCon(desde, hasta);
                        agregado = true;
                        break;
                    }
                }
                if (!agregado) {
                    intervalos.add(new IntervaloVacaciones(desde, hasta));
                }
            }

            for (IntervaloVacaciones intervalo : intervalos) {
                modelo.addRow(new Object[]{
                    intervalo.getDesde(),
                    intervalo.getHasta(),
                    "Vacaciones"
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar vacaciones: " + ex.getMessage());
        }
    }
    private void verificarDisponibilidad() {
        String medicoSeleccionado = (String) cmbMedicoDisponibilidad.getSelectedItem();
        LocalDate fecha = pickerFechaConsulta.getDate();

        if (medicoSeleccionado == null || fecha == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un médico y una fecha.");
            return;
        }

        try (Connection conn = ConectorDB.conectar();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT FechaDesde, FechaHasta FROM EstadoMedico " +
                "WHERE Estado = 'Vacaciones' AND IdMedico = (SELECT IdMedico FROM Medicos WHERE CONCAT(Nombre, ' ', Apellido) = ?) " +
                "ORDER BY FechaDesde"
            )) {

            ps.setString(1, medicoSeleccionado);
            ResultSet rs = ps.executeQuery();

            java.util.List<IntervaloVacaciones> intervalos = new java.util.ArrayList<>();

            while (rs.next()) {
                LocalDate desde = rs.getDate("FechaDesde").toLocalDate();
                LocalDate hasta = rs.getDate("FechaHasta").toLocalDate();

                boolean agregado = false;
                for (IntervaloVacaciones intervalo : intervalos) {
                    if (intervalo.seSuperponeCon(desde, hasta)) {
                        intervalo.unirCon(desde, hasta);
                        agregado = true;
                        break;
                    }
                }
                if (!agregado) {
                    intervalos.add(new IntervaloVacaciones(desde, hasta));
                }
            }

            boolean disponible = true;
            for (IntervaloVacaciones intervalo : intervalos) {
                if (!fecha.isBefore(intervalo.getDesde()) && !fecha.isAfter(intervalo.getHasta())) {
                    disponible = false;
                    break;
                }
            }

            if (disponible) {
                lblResultadoDisponibilidad.setText("Disponible");
                lblResultadoDisponibilidad.setForeground(java.awt.Color.GREEN);
            } else {
                lblResultadoDisponibilidad.setText("NO disponible (Vacaciones)");
                lblResultadoDisponibilidad.setForeground(java.awt.Color.RED);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al verificar disponibilidad: " + ex.getMessage());
        }
    }
    
    class IntervaloVacaciones {
        private LocalDate desde;
        private LocalDate hasta;

        public IntervaloVacaciones(LocalDate desde, LocalDate hasta) {
            this.desde = desde;
            this.hasta = hasta;
        }

        public boolean seSuperponeCon(LocalDate otroDesde, LocalDate otroHasta) {
            return !(otroHasta.isBefore(this.desde) || otroDesde.isAfter(this.hasta));
        }

        public void unirCon(LocalDate otroDesde, LocalDate otroHasta) {
            if (otroDesde.isBefore(this.desde)) this.desde = otroDesde;
            if (otroHasta.isAfter(this.hasta)) this.hasta = otroHasta;
        }

        public LocalDate getDesde() {
            return desde;
        }

        public LocalDate getHasta() {
            return hasta;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbMedicoDisponibilidad = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        pickerFechaConsulta = new com.github.lgooddatepicker.components.DatePicker();
        btnVerificarDisponibilidad = new javax.swing.JButton();
        lblResultadoDisponibilidad = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaVacacionesMedico = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(204, 238, 252));
        jPanel1.setForeground(new java.awt.Color(204, 238, 252));

        jLabel1.setText("Consultar disponibilidad por fecha");
        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));

        jLabel2.setText("Seleccione el médico:");
        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N

        cmbMedicoDisponibilidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Seleccione una fecha:");
        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N

        btnVerificarDisponibilidad.setText("Verificar Disponibilidad");
        btnVerificarDisponibilidad.setBackground(new java.awt.Color(0, 51, 255));
        btnVerificarDisponibilidad.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btnVerificarDisponibilidad.setForeground(new java.awt.Color(255, 255, 255));

        lblResultadoDisponibilidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblResultadoDisponibilidad.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N

        jLabel5.setText("Vacaciones registradas del médico");
        jLabel5.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 255));

        tablaVacacionesMedico.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaVacacionesMedico);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblResultadoDisponibilidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVerificarDisponibilidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pickerFechaConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbMedicoDisponibilidad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbMedicoDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pickerFechaConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnVerificarDisponibilidad)
                        .addGap(18, 18, 18)
                        .addComponent(lblResultadoDisponibilidad))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVerificarDisponibilidad;
    private javax.swing.JComboBox<String> cmbMedicoDisponibilidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblResultadoDisponibilidad;
    private com.github.lgooddatepicker.components.DatePicker pickerFechaConsulta;
    private javax.swing.JTable tablaVacacionesMedico;
    // End of variables declaration//GEN-END:variables
}
