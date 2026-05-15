// Clase ListaCursos.java
package estructuras;

import modelo.Curso;

public class ListaCursos {

    private NodoCurso cabeza;

    // Nodo interno para la lista
    private static class NodoCurso {
        Curso curso;
        NodoCurso siguiente;

        NodoCurso(Curso curso) {
            this.curso = curso;
        }
    }

    public void limpiar() {
        cabeza = null;
    }

    // Insertar un nuevo curso al final
    public void insertar(Curso curso) {
        NodoCurso nuevo = new NodoCurso(curso);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoCurso actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

    // Obtener todos los cursos como arreglo
    public Curso[] obtenerCursos() {
        int cantidad = contarCursos();
        Curso[] cursos = new Curso[cantidad];
        NodoCurso actual = cabeza;
        int i = 0;
        while (actual != null) {
            cursos[i++] = actual.curso;
            actual = actual.siguiente;
        }
        return cursos;
    }

    // Contar cursos
    public int contarCursos() {
        int contador = 0;
        NodoCurso actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.siguiente;
        }
        return contador;
    }

    // Método para actualizar un curso por código
    public boolean actualizarCurso(String codigo, Curso actualizado) {
        NodoCurso actual = cabeza;
        while (actual != null) {
            if (actual.curso.getCodigo().equalsIgnoreCase(codigo)) {
                actual.curso = actualizado;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    // Método para eliminar un curso por código
    public boolean eliminarPorCodigo(String codigo) {
        if (cabeza == null) return false;

        if (cabeza.curso.getCodigo().equalsIgnoreCase(codigo)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        NodoCurso anterior = cabeza;
        NodoCurso actual = cabeza.siguiente;

        while (actual != null) {
            if (actual.curso.getCodigo().equalsIgnoreCase(codigo)) {
                anterior.siguiente = actual.siguiente;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }
    
    // Recorre todos los cursos aplicando una acción (usado para llenar combos, tablas, etc.)
    public void recorrer(java.util.function.Consumer<Curso> accion) {
        NodoCurso actual = cabeza;
        while (actual != null) {
            accion.accept(actual.curso);
            actual = actual.siguiente;
        }
    }

// Buscar un curso por su código
    public Curso buscar(String codigo) {
        NodoCurso actual = cabeza;
        while (actual != null) {
            if (actual.curso.getCodigo().equalsIgnoreCase(codigo)) {
                return actual.curso;
            }
            actual = actual.siguiente;
        }
        return null;
    }
}
