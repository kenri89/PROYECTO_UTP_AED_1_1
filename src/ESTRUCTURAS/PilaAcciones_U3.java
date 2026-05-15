package estructuras;

public class PilaAcciones_U3 {

    private Nodo tope;

    private static class Nodo {
        AccionMatricula accion;
        Nodo siguiente;

        Nodo(AccionMatricula accion) {
            this.accion = accion;
            this.siguiente = null;
        }
    }

    public void apilar(AccionMatricula accion) {
        Nodo nuevo = new Nodo(accion);
        nuevo.siguiente = tope;
        tope = nuevo;
    }

    public AccionMatricula desapilar() {
        if (tope == null) return null;
        AccionMatricula accion = tope.accion;
        tope = tope.siguiente;
        return accion;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public void recorrer(java.util.function.Consumer<AccionMatricula> callback) {
        Nodo actual = tope;
        while (actual != null) {
            callback.accept(actual.accion);
            actual = actual.siguiente;
        }
    }

    // ✅ Versión sin Stack: devuelve una nueva pila con los mismos elementos en el mismo orden
    public PilaAcciones_U3 copiar() {
        PilaAcciones_U3 auxiliar = new PilaAcciones_U3();
        PilaAcciones_U3 copia = new PilaAcciones_U3();

        Nodo actual = tope;

        // Paso 1: apilar en auxiliar (invierte orden)
        while (actual != null) {
            auxiliar.apilar(actual.accion);
            actual = actual.siguiente;
        }

        // Paso 2: volver a apilar en copia (recupera orden original)
        actual = auxiliar.tope;
        while (actual != null) {
            copia.apilar(actual.accion);
            actual = actual.siguiente;
        }

        return copia;
    }
}
