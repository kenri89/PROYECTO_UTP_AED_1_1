package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Util.ConexionSQL;

public class UsuarioDAO {

    public boolean validarUsuario(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE LOWER(username) = LOWER(?)";
        
        // Llamamos directamente a ConexionSQL.getConexion() porque es estático
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
            System.out.println("Error en DAO: " + e.getMessage());
        }
        return false;
    }
}