package dao;

import java.util.List;

public interface IEstudianteDAO {
    boolean insertar(String carnet, String nombre, String carrera);
    List<String[]> listar();
    boolean actualizar(String carnet, String nombre, String carrera);
    boolean eliminar(String carnet);
}
