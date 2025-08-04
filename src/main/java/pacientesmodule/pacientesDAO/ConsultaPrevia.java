package pacientesmodule.pacientesDAO;

import java.util.Date;

public class ConsultaPrevia {
    private int idCita;
    private Date fecha;
    private String especialidad;
    private String nombreDoctor;

     public ConsultaPrevia(int idCita, Date fecha, String especialidad, String nombreDoctor) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.especialidad = especialidad; // <-- Se añade de nuevo
        this.nombreDoctor = nombreDoctor;
    }
    // --- Getters ---
    public int getIdCita() {
        return idCita;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }
}