package util;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordMigration {

    private static final String SELECT = "SELECT id, username, password FROM usuarios";
    private static final String UPDATE = "UPDATE usuarios SET password = ? WHERE id = ?";

    public static void main(String[] args) {
        int migrados = 0;
        int omitidos = 0;
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement sel = cn.prepareStatement(SELECT);
             ResultSet rs = sel.executeQuery();
             PreparedStatement upd = cn.prepareStatement(UPDATE)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                if (password == null || password.isEmpty()) continue;

                if (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) {
                    omitidos++;
                    System.out.println("  [OMITIDO] " + username + " ya tiene hash");
                    continue;
                }

                String hash = BCrypt.hashpw(password.trim(), BCrypt.gensalt());
                upd.setString(1, hash);
                upd.setInt(2, id);
                upd.executeUpdate();
                migrados++;
                System.out.println("  [MIGRADO] " + username);
            }
            System.out.println();
            System.out.println("Migrados: " + migrados);
            System.out.println("Omitidos (ya hasheados): " + omitidos);

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
