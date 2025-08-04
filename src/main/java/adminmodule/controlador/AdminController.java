package adminmodule.controlador;

import adminmodule.dao.*;
import adminmodule.modelo.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AdminController {
    private final DoctorDAO doctorDAO;
    private final PacienteDAO pacienteDAO;

    public AdminController() {
        this.doctorDAO = new DoctorDAO();
        this.pacienteDAO = new PacienteDAO();
    }

    // Métodos para Doctores (se mantienen igual)
    public boolean registrarDoctor(String cedula, String nombres, String apellidos,
                                 Date fechaNacimiento, String sexo, String correo,
                                 String contrasena, String especialidad) {
        Doctor nuevoDoctor = new Doctor(
            cedula, nombres, apellidos, fechaNacimiento,
            sexo, correo, contrasena, "doctor", especialidad
        );
        return doctorDAO.guardar(nuevoDoctor);
    }
    
    public boolean actualizarDoctor(String cedula, String nombres, String apellidos,
                                   Date fechaNacimiento, String sexo, String correo,
                                   String contrasena, String especialidad) {
        Doctor doctorExistente = doctorDAO.obtenerPorCedula(cedula);
        if (doctorExistente == null) {
            System.out.println("Error: No existe un médico con esa cédula.");
            return false;
        }

        doctorExistente.setNombres(nombres);
        doctorExistente.setApellidos(apellidos);
        doctorExistente.setFechaNacimiento(fechaNacimiento);
        doctorExistente.setSexo(sexo);
        doctorExistente.setCorreo(correo);
        doctorExistente.setContrasena(contrasena);
        doctorExistente.setEspecialidad(especialidad);

        return doctorDAO.actualizar(doctorExistente);
    }
    
    public boolean eliminarDoctorConConfirmacion(String cedula) {
        Scanner scanner = new Scanner(System.in);
        if (!doctorDAO.existe(cedula)) {
            System.out.println("El doctor con cédula " + cedula + " no está registrado en el sistema.");
            return false;
        }

        System.out.println("¿Está seguro de que desea eliminar al doctor con cédula " + cedula + "? (s/n)");
        String confirmacion = scanner.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación cancelada por el usuario.");
            return false;
        }

        boolean eliminado = doctorDAO.eliminar(cedula);
        if (eliminado) {
            System.out.println("Doctor eliminado correctamente del sistema.");
            return true;
        } else {
            System.out.println("Hubo un error al intentar eliminar al doctor.");
            return false;
        }
    }

    // Métodos mejorados para Pacientes
    public boolean actualizarPaciente(String cedula, String nombres, String apellidos,
                                   Date fechaNacimiento, String sexo, String correo,
                                   String contrasena, String alergias, 
                                   String oxigenacion, String idAntecedentes) {
        try {
            Paciente pacienteExistente = pacienteDAO.obtenerPorCedula(cedula);
            if (pacienteExistente == null) {
                JOptionPane.showMessageDialog(null, 
                    "No existe un paciente con la cédula " + cedula, 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Actualizar datos del paciente
            pacienteExistente.setNombres(nombres);
            pacienteExistente.setApellidos(apellidos);
            pacienteExistente.setFechaNacimiento(fechaNacimiento);
            pacienteExistente.setSexo(sexo);
            pacienteExistente.setCorreo(correo);
            
            // Solo actualizar contraseña si se proporcionó una nueva
            if (contrasena != null && !contrasena.isEmpty()) {
                pacienteExistente.setContrasena(contrasena);
            }
            
            pacienteExistente.setAlergias(alergias);
            pacienteExistente.setOxigenacion(oxigenacion);
            
            // Manejo especial de antecedentes
            if (idAntecedentes != null && !idAntecedentes.isEmpty()) {
                try {
                    int idAntecedentesInt = Integer.parseInt(idAntecedentes);
                    
                    // Verificar si existe el antecedente
                    if (!existeAntecedente(idAntecedentesInt)) {
                        int respuesta = JOptionPane.showConfirmDialog(null,
                            "El ID de antecedentes no existe. ¿Desea crearlo automáticamente?",
                            "Antecedente no encontrado", JOptionPane.YES_NO_OPTION);
                        
                        if (respuesta == JOptionPane.YES_OPTION) {
                            if (!crearAntecedente(idAntecedentesInt)) {
                                JOptionPane.showMessageDialog(null,
                                    "No se pudo crear el antecedente automáticamente",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                                return false;
                            }
                        } else {
                            return false; // Usuario canceló la operación
                        }
                    }
                    
                    pacienteExistente.setIdAntecedentes(idAntecedentes);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                        "El ID de antecedentes debe ser un número válido",
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                pacienteExistente.setIdAntecedentes(null); // No hay antecedente
            }

            return pacienteDAO.actualizar(pacienteExistente);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al actualizar paciente: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean eliminarPaciente(String cedula) {
        try {
            // Verificar primero si existe el paciente
            if (!pacienteDAO.existe(cedula)) {
                JOptionPane.showMessageDialog(null,
                    "No existe un paciente con la cédula " + cedula,
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return pacienteDAO.eliminar(cedula);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Error al eliminar paciente: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // Nuevos métodos para manejar antecedentes
    public boolean existeAntecedente(int idAntecedentes) {
        try {
            return pacienteDAO.existeAntecedente(idAntecedentes);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean crearAntecedente(int idAntecedentes) {
        try {
            return pacienteDAO.crearAntecedente(idAntecedentes);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // Métodos para obtener listados
    public List<Doctor> obtenerTodosDoctores() {
        return doctorDAO.obtenerTodos();
    }

    public List<Paciente> obtenerTodosPacientes() {
        return pacienteDAO.obtenerTodos();
    }
    
    // Método adicional para obtener un paciente específico
    public Paciente obtenerPacientePorCedula(String cedula) {
        return pacienteDAO.obtenerPorCedula(cedula);
    }
}