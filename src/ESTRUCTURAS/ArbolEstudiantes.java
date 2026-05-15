package estructuras;

import modelo.Estudiante;

import java.util.function.Consumer;

public class ArbolEstudiantes {

    private class Nodo {
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

    // Insertar estudiante en el ABB
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

    // Buscar estudiante por carnet
    public Estudiante buscar(String carnet) {
        Nodo actual = raiz;
        while (actual != null) {
            int cmp = carnet.compareTo(actual.estudiante.getCarnet());
            if (cmp == 0) return actual.estudiante;
            else if (cmp < 0) actual = actual.izquierda;
            else actual = actual.derecha;
        }
        return null;
    }

    // Eliminar estudiante por carnet
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

            Nodo sucesor = encontrarMin(actual.derecha);
            actual.estudiante = sucesor.estudiante;
            actual.derecha = eliminarRec(actual.derecha, sucesor.estudiante.getCarnet());
        }

        return actual;
    }

    private Nodo encontrarMin(Nodo nodo) {
        while (nodo.izquierda != null) {
            nodo = nodo.izquierda;
        }
        return nodo;
    }

    // Recorrido inorden
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

    // Recorrido preorden
    public void preorden(Consumer<Estudiante> accion) {
        preordenRec(raiz, accion);
    }

    private void preordenRec(Nodo actual, Consumer<Estudiante> accion) {
        if (actual != null) {
            accion.accept(actual.estudiante);
            preordenRec(actual.izquierda, accion);
            preordenRec(actual.derecha, accion);
        }
    }

    // Recorrido postorden
    public void postorden(Consumer<Estudiante> accion) {
        postordenRec(raiz, accion);
    }

    private void postordenRec(Nodo actual, Consumer<Estudiante> accion) {
        if (actual != null) {
            postordenRec(actual.izquierda, accion);
            postordenRec(actual.derecha, accion);
            accion.accept(actual.estudiante);
        }
    }

    // Actualizar estudiante
    public void actualizar(String carnet, String nuevoNombre, String nuevaCarrera) {
        Nodo actual = raiz;
        while (actual != null) {
            int cmp = carnet.compareTo(actual.estudiante.getCarnet());
            if (cmp == 0) {
                actual.estudiante.setNombre(nuevoNombre);
                actual.estudiante.setCarrera(nuevaCarrera);
                return;
            } else if (cmp < 0) {
                actual = actual.izquierda;
            } else {
                actual = actual.derecha;
            }
        }
    }
}
