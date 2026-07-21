package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class MigrationEstudiantesAUsuarios {
    public static void main(String[] args) {
        String hash = BCrypt.hashpw("1234", BCrypt.gensalt());
        int insertados = 0, omitidos = 0;
        String select = "SELECT carnet FROM Estudiantes WHERE carnet NOT IN (SELECT username FROM usuarios)";
        String insert = "INSERT INTO usuarios (username, password, rol) VALUES (?, ?, 'Estudiante')";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement psSelect = cn.prepareStatement(select);
             ResultSet rs = psSelect.executeQuery();
             PreparedStatement psInsert = cn.prepareStatement(insert)) {
            while (rs.next()) {
                String carnet = rs.getString("carnet");
                psInsert.setString(1, carnet);
                psInsert.setString(2, hash);
                psInsert.executeUpdate();
                insertados++;
                System.out.println("Creado usuario para carnet: " + carnet);
            }
            System.out.println("--- Migración completada ---");
            System.out.println("Insertados: " + insertados);
            System.out.println("Omitidos (ya existían): " + omitidos);
        } catch (Exception e) {
            System.err.println("Error en migración: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
