package dao;

import java.util.List;

public interface IMatriculaDAO {
    boolean insertar(String carnet, String codigoCurso);
    List<String[]> listar();
    boolean eliminar(String carnet, String codigoCurso);
}
