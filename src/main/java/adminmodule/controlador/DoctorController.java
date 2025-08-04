
package adminmodule.controlador;

import adminmodule.dao.PacienteDAO;
import adminmodule.modelo.Paciente;
import java.util.List;

public class DoctorController {
    private final PacienteDAO pacienteDAO;

    public DoctorController() {
        this.pacienteDAO = new PacienteDAO();
    }

    public List<Paciente> obtenerPacientesAsignados(String doctorId) {
        // Implementar lógica para obtener pacientes asignados a un doctor
        return pacienteDAO.obtenerTodos(); // Temporal
    }

    public boolean actualizarHistorialMedico(String pacienteId, String diagnostico) {
        Paciente paciente = pacienteDAO.obtenerPorCedula(pacienteId);
        if (paciente != null) {
            // Lógica para actualizar historial médico
            return true;
        }
        return false;
    }
}
