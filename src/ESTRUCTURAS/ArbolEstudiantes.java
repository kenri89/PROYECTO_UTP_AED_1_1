package estructuras;

import modelo.Estudiante;
import java.util.function.Consumer;

public class ArbolEstudiantes {
    
    private static class Nodo {
        Estudiante estudiante;
        Nodo izquierda, derecha;

        Nodo(Estudiante estudiante) {
            this.estudiante = estudiante;
        }
    }

    private Nodo raiz;

    public void limpiar() {
        raiz = null;
    }

    public void insertar(Estudiante nuevo) {
        raiz = insertarRec(raiz, nuevo);
    }

    private Nodo insertarRec(Nodo actual, Estudiante nuevo) {
        if (actual == null) {
            return new Nodo(nuevo);
        }
        int cmp = nuevo.getCarnet().compareTo(actual.estudiante.getCarnet());
        if (cmp < 0) {
            actual.izquierda = insertarRec(actual.izquierda, nuevo);
        } else if (cmp > 0) {
            actual.derecha = insertarRec(actual.derecha, nuevo);
        }
        return actual;
    }

    public Estudiante buscar(String carnet) {
        return buscarRec(raiz, carnet);
    }

    private Estudiante buscarRec(Nodo actual, String carnet) {
        if (actual == null) return null;
        int cmp = carnet.compareTo(actual.estudiante.getCarnet());
        if (cmp == 0) return actual.estudiante;
        return cmp < 0 ? buscarRec(actual.izquierda, carnet) : buscarRec(actual.derecha, carnet);
    }

    // ✅ NUEVO: Método actualizar requerido por PanelEstudiantes
    public boolean actualizar(String carnet, String nuevoNombre, String nuevaCarrera) {
        Estudiante est = buscar(carnet);
        if (est != null) {
            est.setNombre(nuevoNombre);
            est.setCarrera(nuevaCarrera);
            return true;
        }
        return false;
    }

    // ✅ NUEVO: Método eliminar requerido por PanelEstudiantes
    public void eliminar(String carnet) {
        raiz = eliminarRec(raiz, carnet);
    }

    private Nodo eliminarRec(Nodo actual, String carnet) {
        if (actual == null) return null;
        int cmp = carnet.compareTo(actual.estudiante.getCarnet());
        if (cmp < 0) {
            actual.izquierda = eliminarRec(actual.izquierda, carnet);
        } else if (cmp > 0) {
            actual.derecha = eliminarRec(actual.derecha, carnet);
        } else {
            if (actual.izquierda == null) return actual.derecha;
            if (actual.derecha == null) return actual.izquierda;
            actual.estudiante = encontrarMinimo(actual.derecha);
            actual.derecha = eliminarRec(actual.derecha, actual.estudiante.getCarnet());
        }
        return actual;
    }

    private Estudiante encontrarMinimo(Nodo actual) {
        while (actual.izquierda != null) {
            actual = actual.izquierda;
        }
        return actual.estudiante;
    }

    // ✅ SINCRO: Método de recorrido exigido por los Paneles
    public void inorden(Consumer<Estudiante> accion) {
        inordenRec(raiz, accion);
    }

    private void inordenRec(Nodo actual, Consumer<Estudiante> accion) {
        if (actual != null) {
            inordenRec(actual.izquierda, accion);
            accion.accept(actual.estudiante);
            inordenRec(actual.derecha, accion);
        }
    }
}