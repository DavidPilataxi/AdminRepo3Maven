
package adminmodule.modelo;

import java.util.Date;

public class Paciente extends Usuario {
    private String alergias;
    private String oxigenacion;
    private String idAntecedentes;

    public Paciente(String cedula, String nombres, String apellidos, Date fechaNacimiento,
                   String sexo, String correo, String contrasena, String rol,
                   String alergias, String oxigenacion, String idAntecedentes) {
        super(cedula, nombres, apellidos, fechaNacimiento, sexo, correo, contrasena, rol);
        this.alergias = alergias;
        this.oxigenacion = oxigenacion;
        this.idAntecedentes = idAntecedentes;
    }

    // Getters y Setters
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    
    public String getOxigenacion() { return oxigenacion; }
    public void setOxigenacion(String oxigenacion) { this.oxigenacion = oxigenacion; }
    
    public String getIdAntecedentes() { return idAntecedentes; }
    public void setIdAntecedentes(String idAntecedentes) { this.idAntecedentes = idAntecedentes; }
}
