package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EstudianteTest {

    @Test
    void testConstructorYGetters() {
        Estudiante est = new Estudiante("2024001", "Juan Perez", "Ingenieria");
        assertEquals("2024001", est.getCarnet());
        assertEquals("Juan Perez", est.getNombre());
        assertEquals("Ingenieria", est.getCarrera());
    }

    @Test
    void testSetters() {
        Estudiante est = new Estudiante("2024001", "Juan Perez", "Ingenieria");
        est.setCarnet("2024002");
        est.setNombre("Maria Lopez");
        est.setCarrera("Medicina");
        assertEquals("2024002", est.getCarnet());
        assertEquals("Maria Lopez", est.getNombre());
        assertEquals("Medicina", est.getCarrera());
    }
}
