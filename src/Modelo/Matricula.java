package modelo;

/**
 * Modelo de datos para representar una matrícula
 * Tema: Unidad 2 – Modelado y estructuras de datos
 */
public class Matricula {

    private Estudiante estudiante;
    private Curso curso;

    public Matricula(Estudiante estudiante, Curso curso) {
        this.estudiante = estudiante;
        this.curso = curso;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return "Matrícula de " + estudiante.getNombre() + " al curso " + curso.getNombre();
    }
}
