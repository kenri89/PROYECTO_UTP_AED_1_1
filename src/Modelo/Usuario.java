package modelo;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.MoreObjects;

public class Usuario {
    private String username;
    private String password;
    private String rol;
    private String carnet;

    public Usuario(String username, String password, String rol) {
        this(username, password, rol, null);
    }

    public Usuario(String username, String password, String rol, String carnet) {
        this.username = checkNotNull(username, "Username no puede ser nulo");
        this.password = checkNotNull(password, "Password no puede ser nulo");
        this.rol = checkNotNull(rol, "Rol no puede ser nulo");
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("rol", rol)
                .toString();
    }
}
