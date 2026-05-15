package estructuras;

import modelo.Curso;

public class ArregloCursos {
    private Curso[] cursos;
    private int contador;

    public ArregloCursos() {
        cursos = new Curso[100]; // Tamaño por defecto
        contador = 0;
    }

    public void limpiar() {
        for (int i = 0; i < contador; i++) {
            cursos[i] = null;
        }
        contador = 0;
    }

    public void insertar(Curso curso) {
        if (contador < cursos.length) {
            cursos[contador++] = curso;
        }
    }

    public boolean eliminarPorCodigo(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (cursos[i] != null && cursos[i].getCodigo().equalsIgnoreCase(codigo)) {
                // Desplazar los elementos para mantener el arreglo compacto
                for (int j = i; j < contador - 1; j++) {
                    cursos[j] = cursos[j + 1];
                }
                cursos[--contador] = null;
                return true;
            }
        }
        return false;
    }

    public Curso[] obtenerCursos() {
        Curso[] copia = new Curso[contador];
        System.arraycopy(cursos, 0, copia, 0, contador);
        return copia;
    }

    // ✅ NUEVO: método para actualizar un curso existente
    public boolean actualizarCurso(String codigo, Curso actualizado) {
        for (int i = 0; i < contador; i++) {
            if (cursos[i] != null && cursos[i].getCodigo().equalsIgnoreCase(codigo)) {
                cursos[i] = actualizado;
                return true;
            }
        }
        return false;
    }

    public boolean existe(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (cursos[i] != null && cursos[i].getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }
}
