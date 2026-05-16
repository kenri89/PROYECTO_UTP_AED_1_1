package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Util.ConexionSQL;

public class CursoDAO {

    public boolean insertar(String codigo, String nombre, int semestre) {
        String sql = "INSERT INTO Cursos (codigo, nombre, semestre) VALUES (?, ?, ?)";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setInt(3, semestre);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar curso: " + e.getMessage());
            return false;
        }
    }

    public List<String[]> listar() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre, semestre FROM Cursos";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String[] curso = new String[3];
                curso[0] = rs.getString("codigo");
                curso[1] = rs.getString("nombre");
                curso[2] = String.valueOf(rs.getInt("semestre"));
                lista.add(curso);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar cursos: " + e.getMessage());
        }
        return lista;
    }
}