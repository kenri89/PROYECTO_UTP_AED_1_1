// ================================
// CLASE Usuario
// Tema: Unidad 1 - Arreglo de Objetos
// Uso: Estructura básica para almacenar usuarios con roles en un array
// ================================
package modelo;

public class Usuario {
    private String username;
    private String password;
    private String rol;
    /** Carnet del estudiante en el sistema académico (null si no aplica). */
    private String carnet;

    public Usuario(String username, String password, String rol) {
        this(username, password, rol, null);
    }

    public Usuario(String username, String password, String rol, String carnet) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.carnet = carnet;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }

    public String getCarnet() {
        return carnet;
    }
}
