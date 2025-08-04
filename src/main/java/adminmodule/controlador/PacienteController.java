
package adminmodule.controlador;

import adminmodule.dao.PacienteDAO;
import adminmodule.modelo.Paciente;

public class PacienteController {
    private final PacienteDAO pacienteDAO;

    public PacienteController() {
        this.pacienteDAO = new PacienteDAO();
    }

    public Paciente obtenerDatosPaciente(String cedula) {
        return pacienteDAO.obtenerPorCedula(cedula);
    }

    public boolean actualizarDatosPersonales(Paciente paciente) {
        return pacienteDAO.actualizar(paciente);
    }
}