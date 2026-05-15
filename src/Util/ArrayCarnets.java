package util;

/**
 * Clase auxiliar que simula un arreglo dinámico de Strings (carnets).
 * Reemplaza el uso de List<String> usando solo arrays nativos.
 * Compatible con la restricción del proyecto (sin ArrayList).
 */
public class ArrayCarnets {

    private String[] datos;
    private int cantidad;

    public ArrayCarnets() {
        datos = new String[10]; // capacidad inicial
        cantidad = 0;
    }

    /**
     * Agrega un nuevo carnet al arreglo.
     */
    public void agregar(String carnet) {
        if (cantidad == datos.length) {
            redimensionar();
        }
        datos[cantidad++] = carnet;
    }

    /**
     * Devuelve la cantidad actual de carnets almacenados.
     */
    public int tamano() {
        return cantidad;
    }

    /**
     * Devuelve el carnet en una posición específica.
     */
    public String obtener(int index) {
        if (index < 0 || index >= cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }
        return datos[index];
    }

    /**
     * Devuelve todos los carnets como un arreglo fijo.
     */
    public String[] comoArreglo() {
        String[] copia = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = datos[i];
        }
        return copia;
    }

    /**
     * Aumenta la capacidad del arreglo interno.
     */
    private void redimensionar() {
        String[] nuevo = new String[datos.length * 2];
        for (int i = 0; i < datos.length; i++) {
            nuevo[i] = datos[i];
        }
        datos = nuevo;
    }
}
