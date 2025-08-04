
package adminmodule.util;

import java.util.Date; // Importación necesaria
import java.util.regex.Pattern;

public class Validaciones {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$");
    
    public static boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean validarNombresCompletos(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return false;
        }

        // Eliminar espacios extras y dividir en partes
        String[] partes = nombreCompleto.trim().split("\\s+");

        // Debe tener exactamente 4 partes (2 nombres + 2 apellidos)
        if (partes.length != 2) {
            return false;
        }

        // Validar que cada parte solo contenga letras y caracteres permitidos
        Pattern patronNombre = Pattern.compile("^[\\p{L}'-]+$");

        for (String parte : partes) {
            if (!patronNombre.matcher(parte).matches()) {
                return false;
            }

            // Validar longitud mínima (al menos 2 caracteres)
            if (parte.length() < 2) {
                return false;
            }
        }

        return true;
    }
    
    public static boolean validarCampoObligatorio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
    
    public static boolean validarFechaNacimiento(Date fecha) {
        if (fecha == null) return false;
        
        // Crear fecha mínima (120 años atrás)
        long msPorAnio = 1000L * 60 * 60 * 24 * 365;
        Date fechaMinima = new Date(System.currentTimeMillis() - (120 * msPorAnio));
        
        // Crear fecha máxima (12 años atrás)
        Date fechaMaxima = new Date(System.currentTimeMillis() - (12 * msPorAnio));
        
        return fecha.after(fechaMinima) && fecha.before(fechaMaxima);
    }
    
    // Método para validar contraseña (versión básica)
    public static boolean validarContrasena(String contrasena) {
        return contrasena != null && contrasena.length() >= 6;
    }
    
    // Método para validar contraseña segura (versión avanzada)
    public static boolean validarContrasenaSegura(String contrasena) {
        return contrasena != null && PASSWORD_PATTERN.matcher(contrasena).matches();
    }
}