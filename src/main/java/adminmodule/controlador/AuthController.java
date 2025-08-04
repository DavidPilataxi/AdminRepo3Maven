
package adminmodule.controlador;

import adminmodule.dao.*;
import adminmodule.modelo.*;
import adminmodule.util.CedulaEcuatoriana;
import adminmodule.util.Validaciones;
import java.util.Date;

public class AuthController {
    private final PacienteDAO pacienteDAO;
    private final DoctorDAO doctorDAO;
    private final AdministradorDAO adminDAO;

    public AuthController() {
        this.pacienteDAO = new PacienteDAO();
        this.doctorDAO = new DoctorDAO();
        this.adminDAO = new AdministradorDAO();
    }

    public boolean autenticarUsuario(String cedula, String contrasena, String tipoUsuario) {
        switch (tipoUsuario.toLowerCase()) {
            case "paciente":
                Paciente paciente = pacienteDAO.obtenerPorCedula(cedula);
                return paciente != null && paciente.getContrasena().equals(contrasena);
            case "doctor":
                Doctor doctor = doctorDAO.obtenerPorCedula(cedula);
                return doctor != null && doctor.getContrasena().equals(contrasena);
            case "administrador":
                Administrador admin = adminDAO.obtenerPorCedula(cedula);
                return admin != null && admin.getContrasena().equals(contrasena);
            default:
                return false;
        }
    }

    public boolean registrarPaciente(String cedula, String nombres, String apellidos, 
                                   Date fechaNacimiento, String sexo, String correo, 
                                   String contrasena) {
        if (!CedulaEcuatoriana.validar(cedula)) return false;
        if (!Validaciones.validarNombresCompletos(nombres)) return false;
        if (!Validaciones.validarEmail(correo)) return false;
        if (pacienteDAO.existe(cedula)) return false;

        Paciente nuevoPaciente = new Paciente(
            cedula, nombres, apellidos, fechaNacimiento,
            sexo, correo, contrasena, "paciente",
            null, null, null
        );

        return pacienteDAO.guardar(nuevoPaciente);
    }
}
