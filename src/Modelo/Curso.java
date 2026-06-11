package modelo;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.MoreObjects;

public class Curso {

    private String codigo;
    private String nombre;
    private int creditos;
    private int semestre;

    public Curso(String codigo, String nombre, int creditos, int semestre) {
        this.codigo = checkNotNull(codigo, "El código no puede ser nulo");
        this.nombre = checkNotNull(nombre, "El nombre no puede ser nulo");
        checkArgument(creditos >= 0, "Los créditos no pueden ser negativos: %s", creditos);
        checkArgument(semestre >= 1 && semestre <= 10, "Semestre debe estar entre 1 y 10: %s", semestre);
        this.creditos = creditos;
        this.semestre = semestre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = checkNotNull(codigo, "El código no puede ser nulo");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = checkNotNull(nombre, "El nombre no puede ser nulo");
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        checkArgument(creditos > 0, "Los créditos deben ser positivos: %s", creditos);
        this.creditos = creditos;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        checkArgument(semestre >= 1 && semestre <= 10, "Semestre debe estar entre 1 y 10: %s", semestre);
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("codigo", codigo)
                .add("nombre", nombre)
                .add("creditos", creditos)
                .add("semestre", semestre)
                .toString();
    }
}
