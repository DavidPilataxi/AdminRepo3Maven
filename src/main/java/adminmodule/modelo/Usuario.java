package adminmodule.modelo;

import java.util.Date;

public class Usuario {
    protected String cedula;
    protected String nombres;
    protected String apellidos;
    protected Date fechaNacimiento;
    protected String sexo;
    protected String correo;
    protected String contrasena;
    protected String rol;

    public Usuario(String cedula, String nombres, String apellidos, 
                  Date fechaNacimiento, String sexo, String correo, 
                  String contrasena, String rol) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters
    public String getCedula() { return cedula; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public String getSexo() { return sexo; }
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }

    // Setters
    public void setCedula(String cedula) { this.cedula = cedula; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRol(String rol) { this.rol = rol; }
}