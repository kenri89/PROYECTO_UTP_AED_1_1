package estructuras;

import modelo.Matricula;

public class AccionMatricula {
    private String tipo; // REGISTRO, ACTUALIZACIÓN, ELIMINACIÓN
    private Matricula matricula;

    public AccionMatricula(String tipo, Matricula matricula) {
        this.tipo = tipo;
        this.matricula = matricula;
    }

    public String getTipo() {
        return tipo;
    }

    public Matricula getMatricula() {
        return matricula;
    }
}
