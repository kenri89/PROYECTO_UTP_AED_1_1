package modelo;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.MoreObjects;

public class Matricula {

    private Estudiante estudiante;
    private Curso curso;

    public Matricula(Estudiante estudiante, Curso curso) {
        this.estudiante = checkNotNull(estudiante, "El estudiante no puede ser nulo");
        this.curso = checkNotNull(curso, "El curso no puede ser nulo");
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = checkNotNull(estudiante, "El estudiante no puede ser nulo");
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = checkNotNull(curso, "El curso no puede ser nulo");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("estudiante", estudiante.getNombre())
                .add("curso", curso.getNombre())
                .toString();
    }
}
