package estructuras;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import modelo.Curso;
import org.junit.jupiter.api.Test;

class ArregloCursosTest {

    @Test
    void testInsertarYObtener() {
        ArregloCursos arreglo = new ArregloCursos();
        Curso c1 = new Curso("CS101", "Programacion", 4, 1);
        arreglo.insertar(c1);
        List<Curso> cursos = arreglo.obtenerCursos();
        assertEquals(c1, cursos.get(0));
    }

    @Test
    void testLimpiar() {
        ArregloCursos arreglo = new ArregloCursos();
        arreglo.insertar(new Curso("CS101", "Programacion", 4, 1));
        arreglo.limpiar();
        List<Curso> cursos = arreglo.obtenerCursos();
        assertTrue(cursos.isEmpty());
    }
}
