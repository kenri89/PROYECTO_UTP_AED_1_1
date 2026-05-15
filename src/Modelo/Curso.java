package modelo;

/**
 * Modelo de datos para curso
 * Tema: Unidad 1 – Arreglos, listas, y estructuras básicas
 */
public class Curso {

    private String codigo;
    private String nombre;
    private int creditos;
    private int semestre;

    public Curso(String codigo, String nombre, int creditos, int semestre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
