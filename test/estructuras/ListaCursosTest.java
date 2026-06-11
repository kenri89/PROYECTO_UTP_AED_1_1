package estructuras;

import static org.junit.jupiter.api.Assertions.*;
import modelo.Curso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListaCursosTest {

    private ListaCursos lista;

    @BeforeEach
    void setUp() {
        lista = new ListaCursos();
        lista.insertar(new Curso("CS101", "Programacion I", 4, 1));
        lista.insertar(new Curso("CS102", "Programacion II", 4, 2));
        lista.insertar(new Curso("CS201", "Estructuras de Datos", 5, 3));
    }

    @Test
    void testInsertar() {
        assertEquals(3, lista.contarCursos());
        lista.insertar(new Curso("CS301", "Bases de Datos", 4, 4));
        assertEquals(4, lista.contarCursos());
    }

    @Test
    void testBuscar() {
        Curso encontrado = lista.buscar("CS102");
        assertNotNull(encontrado);
        assertEquals("Programacion II", encontrado.getNombre());
    }

    @Test
    void testBuscarNoExistente() {
        assertNull(lista.buscar("XX999"));
    }

    @Test
    void testEliminarPorCodigo() {
        assertTrue(lista.eliminarPorCodigo("CS102"));
        assertEquals(2, lista.contarCursos());
        assertNull(lista.buscar("CS102"));
    }

    @Test
    void testActualizarCurso() {
        Curso actualizado = new Curso("CS101", "Programacion I Avanzada", 5, 1);
        assertTrue(lista.actualizarCurso("CS101", actualizado));
        assertEquals("Programacion I Avanzada", lista.buscar("CS101").getNombre());
        assertEquals(5, lista.buscar("CS101").getCreditos());
    }

    @Test
    void testLimpiar() {
        lista.limpiar();
        assertEquals(0, lista.contarCursos());
    }

    @Test
    void testRecorrer() {
        int[] contador = {0};
        lista.recorrer(c -> contador[0]++);
        assertEquals(3, contador[0]);
    }
}
