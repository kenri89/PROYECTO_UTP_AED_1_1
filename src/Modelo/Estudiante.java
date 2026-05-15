package modelo;

/**
 * Modelo de datos para estudiante
 * Tema: Unidad 3 – Árbol Binario de Búsqueda
 */
public class Estudiante {

    private String carnet;
    private String nombre;
    private String carrera;

    public Estudiante(String carnet, String nombre, String carrera) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    @Override
    public String toString() {
        return carnet + " - " + nombre;
    }
}
