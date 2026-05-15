package estructuras;

import modelo.Curso;

public class MatrizSemestres {
    private Curso[][] matriz;
    private int filas;     // Filas representan semestres
    private int columnas;  // Columnas son posiciones disponibles por semestre

    public MatrizSemestres() {
        this(10, 10); // Por defecto 10 semestres con 10 cursos c/u
    }

    public MatrizSemestres(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        matriz = new Curso[filas][columnas];
    }

    public void limpiar() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                matriz[f][c] = null;
            }
        }
    }

    public boolean insertarPorSemestre(Curso curso) {
        int fila = curso.getSemestre() - 1; // índice base 0
        if (fila < 0 || fila >= filas) return false;

        for (int col = 0; col < columnas; col++) {
            if (matriz[fila][col] == null) {
                matriz[fila][col] = curso;
                return true;
            }
        }
        return false; // No hay espacio en ese semestre
    }

    public boolean eliminarPorCodigo(String codigo, int semestre) {
        int fila = semestre - 1;
        if (fila < 0 || fila >= filas) return false;

        for (int col = 0; col < columnas; col++) {
            if (matriz[fila][col] != null &&
                matriz[fila][col].getCodigo().equalsIgnoreCase(codigo)) {
                matriz[fila][col] = null;
                return true;
            }
        }
        return false;
    }

    // ✅ NUEVO: método para actualizar curso directamente en la matriz
    public boolean actualizarCurso(String codigo, Curso actualizado) {
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                Curso actual = matriz[fila][col];
                if (actual != null && actual.getCodigo().equalsIgnoreCase(codigo)) {
                    matriz[fila][col] = actualizado;
                    return true;
                }
            }
        }
        return false;
    }
}
