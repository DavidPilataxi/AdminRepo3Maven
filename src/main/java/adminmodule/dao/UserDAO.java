
package adminmodule.dao;

import adminmodule.modelo.Usuario;

public interface UserDAO<T extends Usuario> {
    boolean existe(String cedula);
    T obtenerPorCedula(String cedula);
    boolean guardar(T usuario);
    boolean actualizar(T usuario);
}