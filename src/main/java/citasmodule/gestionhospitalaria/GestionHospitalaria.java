package citasmodule.gestionhospitalaria;

import javax.swing.SwingUtilities;
import citasmodule.Vistas.DiagnosticoTratamiento;

public class GestionHospitalaria {

    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
        DiagnosticoTratamiento diag = new DiagnosticoTratamiento();
        diag.setVisible(true);
    });
    }
    
}
