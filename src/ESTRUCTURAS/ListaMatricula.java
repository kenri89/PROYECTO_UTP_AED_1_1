package estructuras;

import modelo.Matricula;
import modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ListaMatricula {

    private static class Nodo {
        Matricula matricula;
        Nodo siguiente;

        Nodo(Matricula matricula) {
            this.matricula = matricula;
            this.siguiente = null;
        }
    }

    private Nodo inicio;
    private int tamaño;

    public ListaMatricula() {
        this.inicio = null;
        this.tamaño = 0;
    }

    public void limpiar() {
        inicio = null;
        tamaño = 0;
    }

    public int getTamaño() {
        return tamaño;
    }

    public boolean estaVacia() {
        return inicio == null;
    }

    // ✅ SINCRO: Alias de inserción requerido por la interfaz gráfica
    public void agregar(Matricula matricula) {
        Nodo nuevo = new Nodo(matricula);
        if (estaVacia()) {
            inicio = nuevo;
        } else {
            Nodo actual = inicio;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    public boolean eliminar(String carnet, String codigoCurso) {
        if (estaVacia()) return false;

        if (inicio.matricula.getEstudiante().getCarnet().equalsIgnoreCase(carnet)
                && inicio.matricula.getCurso().getCodigo().equalsIgnoreCase(codigoCurso)) {
            inicio = inicio.siguiente;
            tamaño--;
            return true;
        }

        Nodo actual = inicio;
        while (actual.siguiente != null) {
            if (actual.siguiente.matricula.getEstudiante().getCarnet().equalsIgnoreCase(carnet)
                    && actual.siguiente.matricula.getCurso().getCodigo().equalsIgnoreCase(codigoCurso)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

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

    // ✅ NUEVO: Requerido para buscar registros vigentes en PanelMatricula
    public Matricula buscar(String carnet, String codigoCurso) {
        Nodo actual = inicio;
        while (actual != null) {
            if (actual.matricula.getEstudiante().getCarnet().equalsIgnoreCase(carnet)
                    && actual.matricula.getCurso().getCodigo().equalsIgnoreCase(codigoCurso)) {
                return actual.matricula;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    // ✅ NUEVO: Permite iterar la lista en los hilos visuales y persistencia
    public void recorrer(Consumer<Matricula> accion) {
        Nodo actual = inicio;
        while (actual != null) {
            accion.accept(actual.matricula);
            actual = actual.siguiente;
        }
    }

    // ✅ NUEVO: Filtrado requerido para el módulo PanelMisMatriculas
    public List<Matricula> buscarPorCarnet(String carnet) {
        List<Matricula> filtradas = new ArrayList<>();
        Nodo actual = inicio;
        while (actual != null) {
            if (actual.matricula.getEstudiante().getCarnet().equalsIgnoreCase(carnet)) {
                filtradas.add(actual.matricula);
            }
            actual = actual.siguiente;
        }
        return filtradas;
    }
}