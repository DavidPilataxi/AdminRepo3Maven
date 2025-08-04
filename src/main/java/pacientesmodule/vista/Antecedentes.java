/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pacientesmodule.vista;

import pacientesmodule.conexion.ConexionSQLServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class Antecedentes extends javax.swing.JFrame {
    private String familiares;
    private String patologicos;
    private String fisiologicos;
    private String enfermedades_actuales;
    private String cedulaPaciente;
            
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Antecedentes.class.getName());

    /**
     * Creates new form Antecedentes
     */
    public Antecedentes() {
        initComponents();
    }

    public Antecedentes(String familiares, String patologicos, String fisiologicos, 
                   String enfermedades_actuales, String cedulaPaciente) {
    
    // Inicializar componentes (si es un JFrame/JDialog)
    initComponents();
    
    // Validar y asignar familiares
    if(familiares != null) {
        this.familiares = familiares;
        System.out.println("Familiares: " + this.familiares);
        this.jTFFamiliares.setText(this.familiares);
    } else {
        this.familiares = "";
        this.jTFFamiliares.setText("");
    }
    
    // Validar y asignar patológicos
    if(patologicos != null) {
        this.patologicos = patologicos;
        System.out.println("Patológicos: " + this.patologicos);
        this.jTFPatologicos.setText(this.patologicos);
    } else {
        this.patologicos = "";
        this.jTFPatologicos.setText("");
    }
    
    // Validar y asignar fisiológicos
    if(fisiologicos != null) {
        this.fisiologicos = fisiologicos;
        System.out.println("Fisiológicos: " + this.fisiologicos);
        this.jTFFisiologicos.setText(this.fisiologicos);
    } else {
        this.fisiologicos = "";
        this.jTFFisiologicos.setText("");
    }
    
    // Validar y asignar enfermedades actuales
    if(enfermedades_actuales != null) {
        this.enfermedades_actuales = enfermedades_actuales;
        System.out.println("Enfermedades actuales: " + this.enfermedades_actuales);
        this.jTFEnfermedadesActuales.setText(this.enfermedades_actuales);
    } else {
        this.enfermedades_actuales = "";
        this.jTFEnfermedadesActuales.setText("");
    }
    
    // Validar y asignar cédula del paciente
    if(cedulaPaciente != null) {
        this.cedulaPaciente = cedulaPaciente;
        System.out.println("Cédula paciente: " + this.cedulaPaciente);
    } else {
        this.cedulaPaciente = "";
        System.out.println("Advertencia: Cédula de paciente es null");
    }
    
    // Centrar la ventana
    this.setLocationRelativeTo(null);
    
    // Opcional: Configurar título de la ventana con la cédula
    if(!this.cedulaPaciente.isEmpty()) {
        this.setTitle("Antecedentes del paciente: " + this.cedulaPaciente);
    }
}
    
public static boolean guardarAntecedentesPaciente(String cedulaPaciente, 
                                                String familiares, 
                                                String patologicos, 
                                                String fisiologicos, 
                                                String enfermedadesActuales) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    boolean resultado = false;

    try {
        // 1. Conectar a la base de datos
        conn = ConexionSQLServer.conectar();
        
        // 2. Verificar si el paciente ya tiene antecedentes registrados
        int idAntecedentes = obtenerIdAntecedentes(cedulaPaciente);
        
        // 3. Preparar la consulta SQL según si es nuevo registro o actualización
        String sql;
        if (idAntecedentes == -1) {
            // Insertar nuevo registro
            sql = "INSERT INTO dbo.Antecedentes (familiares, patologicos, fisiologicos, enfermedades_actuales) " +
                  "VALUES (?, ?, ?, ?); " +
                  "UPDATE dbo.Paciente SET id_antecedetes = SCOPE_IDENTITY() WHERE cedula = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, familiares);
            pstmt.setString(2, patologicos);
            pstmt.setString(3, fisiologicos);
            pstmt.setString(4, enfermedadesActuales);
            pstmt.setString(5, cedulaPaciente);
        } else {
            // Actualizar registro existente
            sql = "UPDATE dbo.Antecedentes " +
                  "SET familiares = ?, patologicos = ?, fisiologicos = ?, enfermedades_actuales = ? " +
                  "WHERE id_antecedentes = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, familiares);
            pstmt.setString(2, patologicos);
            pstmt.setString(3, fisiologicos);
            pstmt.setString(4, enfermedadesActuales);
            pstmt.setInt(5, idAntecedentes);
        }
        
        // 4. Ejecutar la consulta
        int filasAfectadas = pstmt.executeUpdate();
        resultado = (filasAfectadas > 0);
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al guardar antecedentes: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null, 
            "Error: Driver JDBC no encontrado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        // 5. Cerrar recursos
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    return resultado;
}

// Método auxiliar para obtener el id_antecedentes del paciente
private static int obtenerIdAntecedentes(String cedulaPaciente) throws SQLException, ClassNotFoundException {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int idAntecedentes = -1;

    try {
        conn = ConexionSQLServer.conectar();
        String sql = "SELECT id_antecedetes FROM dbo.Paciente WHERE cedula = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cedulaPaciente);
        rs = pstmt.executeQuery();
        
        if (rs.next()) {
            idAntecedentes = rs.getInt("id_antecedetes");
        }
    } finally {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (conn != null) conn.close();
    }
    
    return idAntecedentes;
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTFPatologicos = new javax.swing.JTextField();
        jTFFisiologicos = new javax.swing.JTextField();
        jTFEnfermedadesActuales = new javax.swing.JTextField();
        jTFFamiliares = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Familiares");

        jLabel2.setText("Patológicos");

        jLabel3.setText("Fisiológicos");

        jLabel4.setText("Enfermedades actuales");

        jTFPatologicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFPatologicosActionPerformed(evt);
            }
        });

        jTFFisiologicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFFisiologicosActionPerformed(evt);
            }
        });

        jTFEnfermedadesActuales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFEnfermedadesActualesActionPerformed(evt);
            }
        });

        jTFFamiliares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFFamiliaresActionPerformed(evt);
            }
        });

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFPatologicos, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFFisiologicos, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFEnfermedadesActuales, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFFamiliares, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFFamiliares, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFPatologicos, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFFisiologicos, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFEnfermedadesActuales, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFPatologicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFPatologicosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPatologicosActionPerformed

    private void jTFFisiologicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFisiologicosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFFisiologicosActionPerformed

    private void jTFEnfermedadesActualesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFEnfermedadesActualesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFEnfermedadesActualesActionPerformed

    private void jTFFamiliaresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFamiliaresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFFamiliaresActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // 1. Obtener los valores de los campos
    String familiares =this.jTFFamiliares.getText();
    String patologicos = this.jTFPatologicos.getText();
    String fisiologicos = this.jTFFisiologicos.getText();
    String enfermedadesActuales = this.jTFEnfermedadesActuales.getText();
    
    boolean guardadoExitoso = guardarAntecedentesPaciente(
        cedulaPaciente,
        familiares,
        patologicos,
        fisiologicos,
        enfermedadesActuales
    );
    
    if (guardadoExitoso) {
        JOptionPane.showMessageDialog(this, 
            "Antecedentes médicos guardados correctamente", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        this.dispose(); // Cerrar la ventana si es necesario
    } else {
        JOptionPane.showMessageDialog(this, 
            "No se pudo guardar los antecedentes médicos", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Antecedentes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTFEnfermedadesActuales;
    private javax.swing.JTextField jTFFamiliares;
    private javax.swing.JTextField jTFFisiologicos;
    private javax.swing.JTextField jTFPatologicos;
    // End of variables declaration//GEN-END:variables
}
