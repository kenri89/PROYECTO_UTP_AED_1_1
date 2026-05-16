package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Util.ConexionSQL;

public class EstudianteDAO {

    public boolean insertar(String carnet, String nombre, String carrera) {
        String sql = "INSERT INTO Estudiantes (carnet, nombre, carrera) VALUES (?, ?, ?)";
        
        // CORREGIDO: Se cambió obtenerConexion() por getConexion()
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, carnet);
            ps.setString(2, nombre);
            ps.setString(3, carrera);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al insertar estudiante: " + e.getMessage());
            return false;
        }
    }

    public List<String[]> listar() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT carnet, nombre, carrera FROM Estudiantes";
        
        // CORREGIDO: Se cambió obtenerConexion() por getConexion()
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String[] estudiante = new String[3];
                estudiante[0] = rs.getString("carnet");
                estudiante[1] = rs.getString("nombre");
                estudiante[2] = rs.getString("carrera");
                lista.add(estudiante);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar estudiantes: " + e.getMessage());
        }
        
        return lista;
    }
}