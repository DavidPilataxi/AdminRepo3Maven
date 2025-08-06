package adminmodule.modelo;

import java.util.Date;

public class Paciente extends Usuario {
    private String alergias;
    private String oxigenacion;
    private String idAntecedentes;

    private String estadoCivil;
    private String telefono;
    private String tipoSangre;
    private String tipoIdentificador;

    public Paciente(String cedula, String nombres, String apellidos, Date fechaNacimiento,
                    String sexo, String correo, String contrasena, String rol,
                    String alergias, String idAntecedentes,
                    String estadoCivil, String telefono,
                    String tipoSangre, String tipoIdentificador) {

        super(cedula, nombres, apellidos, fechaNacimiento, sexo, correo, contrasena, rol);
        this.alergias = alergias;
        this.idAntecedentes = idAntecedentes;
        this.estadoCivil = estadoCivil;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
        this.tipoIdentificador = tipoIdentificador;
    }

    // Getters y Setters
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getIdAntecedentes() { return idAntecedentes; }
    public void setIdAntecedentes(String idAntecedentes) { this.idAntecedentes = idAntecedentes; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoSangre() { return tipoSangre; }
    public void setTipoSangre(String tipoSangre) { this.tipoSangre = tipoSangre; }

    public String getTipoIdentificador() { return tipoIdentificador; }
    public void setTipoIdentificador(String tipoIdentificador) { this.tipoIdentificador = tipoIdentificador; }
}

