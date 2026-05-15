// ================================
// CLASE NodoEstudiante
// Tema: Unidad 3 - ABB
// Uso: Nodo del árbol binario
// ================================
package estructuras;

import modelo.Estudiante;

public class NodoEstudiante {
    public Estudiante estudiante;
    public NodoEstudiante izquierda;
    public NodoEstudiante derecha;

    public NodoEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
