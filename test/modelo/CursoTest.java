package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CursoTest {

    @Test
    void testConstructorYGetters() {
        Curso curso = new Curso("CS101", "Programacion I", 4, 1);
        assertEquals("CS101", curso.getCodigo());
        assertEquals("Programacion I", curso.getNombre());
        assertEquals(4, curso.getCreditos());
        assertEquals(1, curso.getSemestre());
    }

    @Test
    void testSetters() {
        Curso curso = new Curso("CS101", "Programacion I", 4, 1);
        curso.setCodigo("CS102");
        curso.setNombre("Programacion II");
        curso.setCreditos(5);
        curso.setSemestre(2);
        assertEquals("CS102", curso.getCodigo());
        assertEquals("Programacion II", curso.getNombre());
        assertEquals(5, curso.getCreditos());
        assertEquals(2, curso.getSemestre());
    }

    @Test
    void testToString() {
        Curso curso = new Curso("CS101", "Programacion I", 4, 1);
        assertTrue(curso.toString().contains("CS101"));
        assertTrue(curso.toString().contains("Programacion I"));
    }
}
