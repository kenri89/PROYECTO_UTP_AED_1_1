package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.ConexionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioDAO implements IUsuarioDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioDAO.class);

    public boolean validarUsuario(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE LOWER(username) = LOWER(?)";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passwordBD = rs.getString("password").trim();
                    return passwordBD.equals(password.trim());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error en DAO Usuario: {}", e.getMessage(), e);
        }
        return false;
    }
}
