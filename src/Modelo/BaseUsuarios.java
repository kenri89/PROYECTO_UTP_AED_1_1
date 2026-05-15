package modelo;

import java.util.ArrayList;
import java.util.List;

public class BaseUsuarios {
    private static List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Usuario("admin", "1234", "Administrador"));
        usuarios.add(new Usuario("docente", "1234", "Docente"));
        usuarios.add(new Usuario("estudiante", "1234", "Estudiante", "2024001"));
    }

    public static Usuario autenticar(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
