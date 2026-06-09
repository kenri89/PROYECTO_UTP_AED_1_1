package dao;

import java.util.List;

public interface ICursoDAO {
    boolean insertar(String codigo, String nombre, int semestre);
    List<String[]> listar();
}
