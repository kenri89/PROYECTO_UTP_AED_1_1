package estructuras;

import modelo.Matricula;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import modelo.Estudiante;

/**
 * Estructura dinámica para manejar matrículas
 * Tema: Unidad 2 – Lista enlazada simple
 */
public class ListaMatricula {

    private Nodo inicio;

    private static class Nodo {
        Matricula matricula;
        Nodo siguiente;

        Nodo(Matricula matricula) {
            this.matricula = matricula;
        }
    }

    public void limpiar() {
        inicio = null;
    }

    // Registrar una nueva matrícula
    public void agregar(Matricula nueva) {
        Nodo nodo = new Nodo(nueva);
        if (inicio == null) {
            inicio = nodo;
        } else {
            Nodo actual = inicio;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nodo;
        }
    }

    // Buscar matrícula por carnet
    public List<Matricula> buscarPorCarnet(String carnet) {
        List<Matricula> resultado = new ArrayList<>();
        Nodo actual = inicio;
        while (actual != null) {
            if (actual.matricula.getEstudiante().getCarnet().equalsIgnoreCase(carnet)) {
                resultado.add(actual.matricula);
            }
            actual = actual.siguiente;
        }
        return resultado;
    }

    // Eliminar matrícula específica por carnet y código de curso
    public boolean eliminar(String carnet, String codigoCurso) {
        Nodo actual = inicio;
        Nodo anterior = null;

        while (actual != null) {
            if (actual.matricula.getEstudiante().getCarnet().equalsIgnoreCase(carnet)
                    && actual.matricula.getCurso().getCodigo().equalsIgnoreCase(codigoCurso)) {

                if (anterior == null) {
                    inicio = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                return true;
            }

            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    // Actualizar matrícula (nota)
    public boolean actualizar(String carnet, String codigoCurso, Estudiante nuevoEstudiante) {
    Nodo actual = inicio;
    while (actual != null) {
        if (actual.matricula.getEstudiante().getCarnet().equalsIgnoreCase(carnet)
                && actual.matricula.getCurso().getCodigo().equalsIgnoreCase(codigoCurso)) {
            actual.matricula.setEstudiante(nuevoEstudiante);
            return true;
        }
        actual = actual.siguiente;
    }
    return false;
}

    // Recorrer todas las matrículas (para mostrar en tabla)
    public void recorrer(Consumer<Matricula> accion) {
        Nodo actual = inicio;
        while (actual != null) {
            accion.accept(actual.matricula);
            actual = actual.siguiente;
        }
    }
   
    // Buscar una matrícula por carnet y código de curso
    public Matricula buscar(String carnet, String codigoCurso) {
        Nodo actual = inicio;
        while (actual != null) {
            Matricula m = actual.matricula;
            if (m.getEstudiante().getCarnet().equalsIgnoreCase(carnet)
                    && m.getCurso().getCodigo().equalsIgnoreCase(codigoCurso)) {
                return m;
            }
            actual = actual.siguiente;
        }
        return null;
    }
}
