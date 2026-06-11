package modelo;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.MoreObjects;

public class Estudiante {

    private String carnet;
    private String nombre;
    private String carrera;

    public Estudiante(String carnet, String nombre, String carrera) {
        this.carnet = checkNotNull(carnet, "El carnet no puede ser nulo");
        this.nombre = checkNotNull(nombre, "El nombre no puede ser nulo");
        this.carrera = checkNotNull(carrera, "La carrera no puede ser nula");
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = checkNotNull(carnet, "El carnet no puede ser nulo");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = checkNotNull(nombre, "El nombre no puede ser nulo");
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = checkNotNull(carrera, "La carrera no puede ser nula");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("carnet", carnet)
                .add("nombre", nombre)
                .add("carrera", carrera)
                .toString();
    }
}
