
package adminmodule.util;

public class CedulaEcuatoriana {
    public static boolean validar(String cedula) {
        if (cedula == null || cedula.length() != 10 || !cedula.matches("\\d+")) {
            return false;
        }
        
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int total = 0;
        
        for (int i = 0; i < 9; i++) {
            int valor = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            total += (valor > 9) ? valor - 9 : valor;
        }
        
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int calculado = (total % 10 == 0) ? 0 : 10 - (total % 10);
        
        return digitoVerificador == calculado;
    }
}
