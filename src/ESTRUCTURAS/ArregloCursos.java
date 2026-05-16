package estructuras;

import modelo.Curso;
import java.util.ArrayList;
import java.util.List;

public class ArregloCursos {

    private Curso[] cursos;
    private int contador;

    public ArregloCursos() {
        cursos = new Curso[100];
        contador = 0;
    }

    public void limpiar() {
        for (int i = 0; i < contador; i++) {
            cursos[i] = null;
        }
        contador = 0;
    }

    public int getContador() {
        return contador;
    }

    public Curso obtener(int indice) {
        if (indice >= 0 && indice < contador) {
            return cursos[indice];
        }
        return null;
    }

    // ✅ NUEVO: Método exigido por PanelCursos y PersistenciaAcademica
    public List<Curso> obtenerCursos() {
        List<Curso> lista = new ArrayList<>();
        for (int i = 0; i < contador; i++) {
            if (cursos[i] != null) {
                lista.add(cursos[i]);
            }
        }
        return lista;
    }

    public void insertar(Curso curso) {
        if (contador >= cursos.length) {
            redimensionar();
        }
        cursos[contador++] = curso;
    }

    private void redimensionar() {
        Curso[] nuevoArreglo = new Curso[cursos.length * 2];
        System.arraycopy(cursos, 0, nuevoArreglo, 0, cursos.length);
        cursos = nuevoArreglo;
    }

    public Curso buscar(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (cursos[i] != null && cursos[i].getCodigo().equalsIgnoreCase(codigo)) {
                return cursos[i];
            }
        }
        return null;
    }

    // ✅ SINCRO: Nombre exacto exigido por PanelCursos
    public boolean eliminarPorCodigo(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (cursos[i] != null && cursos[i].getCodigo().equalsIgnoreCase(codigo)) {
                for (int j = i; j < contador - 1; j++) {
                    cursos[j] = cursos[j + 1];
                }
                cursos[--contador] = null;
                return true;
            }
        }
        return false;
    }

    // ✅ NUEVO: Permite redirigir la actualización del curso en el arreglo
    public boolean actualizarCurso(String codigo, Curso actualizado) {
        for (int i = 0; i < contador; i++) {
            if (cursos[i] != null && cursos[i].getCodigo().equalsIgnoreCase(codigo)) {
                cursos[i] = actualizado;
                return true;
            }
        }
        return false;
    }
}