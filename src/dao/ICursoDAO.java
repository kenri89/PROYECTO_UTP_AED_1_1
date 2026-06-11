package dao;

import java.util.List;

public interface ICursoDAO {
    boolean insertar(String codigo, String nombre, int creditos, int semestre);
    List<String[]> listar();
    boolean actualizar(String codigo, String nombre, int creditos, int semestre);
    boolean eliminar(String codigo);
}
