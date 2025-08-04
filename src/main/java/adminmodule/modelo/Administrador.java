package adminmodule.modelo;

import java.util.Date;

public class Administrador extends Usuario {
    public Administrador(String cedula, String nombres, String apellidos, 
                       Date fechaNacimiento, String sexo, String correo, 
                       String contrasena, String rol) {
        super(cedula, nombres, apellidos, fechaNacimiento, sexo, correo, contrasena, rol);
    }

    @Override
    public String toString() {
        return "Administrador: " + getNombres() + " " + getApellidos() + " (" + getCedula() + ")";
    }
}