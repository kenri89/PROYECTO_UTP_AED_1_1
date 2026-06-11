package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.ConexionSQL;
import modelo.Usuario;
import util.CuentasEstudiantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioDAO implements IUsuarioDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioDAO.class);

    public Usuario autenticar(String username, String password, String rolSeleccionado) {
        String sql = "SELECT * FROM usuarios WHERE LOWER(username) = LOWER(?)";
        LOGGER.debug("Validando usuario: {}", username);
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passwordBD = rs.getString("password").trim();
                    if (passwordBD.equals(password.trim())) {
                        String rol = rs.getString("rol");
                        String carnet = null;
                        try { carnet = rs.getString("carnet"); } catch (Exception ignored) {}
                        LOGGER.info("Usuario {} autenticado con rol {}", username, rol);
                        return new Usuario(username, password, rol, carnet);
                    }
                }
                LOGGER.warn("Usuario {} no encontrado o contraseña incorrecta", username);
            }
        } catch (Exception e) {
            LOGGER.warn("BD no disponible: {}", e.getMessage());
        }
        return autenticarFallback(username, password, rolSeleccionado);
    }

    private Usuario autenticarFallback(String username, String password, String rolSeleccionado) {
        if (username == null || password == null) return null;
        if ("Estudiante".equalsIgnoreCase(rolSeleccionado)) {
            if (CuentasEstudiantes.autenticar(username, password)) {
                LOGGER.info("Estudiante {} autenticado localmente", username);
                return new Usuario(username, password, "Estudiante", username);
            }
            return null;
        }
        String u = username.trim().toLowerCase();
        String p = password.trim();
        if (u.equals("admin") && p.equals("1234")) {
            LOGGER.info("Usuario {} autenticado localmente como Administrador", u);
            return new Usuario(u, p, "Administrador");
        }
        if (u.equals("secretaria") && p.equals("1234")) {
            LOGGER.info("Usuario {} autenticado localmente como Secretaría", u);
            return new Usuario(u, p, "Secretaría");
        }
        if (u.equals("estudiante") && p.equals("1234")) {
            LOGGER.info("Usuario {} autenticado localmente como Estudiante", u);
            return new Usuario(u, p, "Estudiante", u);
        }
        return null;
    }
}
