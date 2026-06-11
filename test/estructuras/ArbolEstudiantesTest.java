package estructuras;

import static org.junit.jupiter.api.Assertions.*;
import modelo.Estudiante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArbolEstudiantesTest {

    private ArbolEstudiantes arbol;

    @BeforeEach
    void setUp() {
        arbol = new ArbolEstudiantes();
        arbol.insertar(new Estudiante("E003", "Carlos Ruiz", "Ingenieria"));
        arbol.insertar(new Estudiante("E001", "Ana Lopez", "Medicina"));
        arbol.insertar(new Estudiante("E002", "Luis Gomez", "Derecho"));
    }

    @Test
    void testInsertarYBuscar() {
        Estudiante encontrado = arbol.buscar("E002");
        assertNotNull(encontrado);
        assertEquals("Luis Gomez", encontrado.getNombre());
    }

    @Test
    void testBuscarNoExistente() {
        assertNull(arbol.buscar("E999"));
    }

    @Test
    void testActualizar() {
        assertTrue(arbol.actualizar("E001", "Ana Maria Lopez", "Cirugia"));
        Estudiante actualizado = arbol.buscar("E001");
        assertEquals("Ana Maria Lopez", actualizado.getNombre());
        assertEquals("Cirugia", actualizado.getCarrera());
    }

    @Test
    void testEliminar() {
        arbol.eliminar("E002");
        assertNull(arbol.buscar("E002"));
        assertNotNull(arbol.buscar("E001"));
        assertNotNull(arbol.buscar("E003"));
    }

    @Test
    void testLimpiar() {
        arbol.limpiar();
        assertNull(arbol.buscar("E001"));
    }

    @Test
    void testInorden() {
        StringBuilder sb = new StringBuilder();
        arbol.inorden(e -> sb.append(e.getCarnet()).append(","));
        assertEquals("E001,E002,E003,", sb.toString());
    }
}
