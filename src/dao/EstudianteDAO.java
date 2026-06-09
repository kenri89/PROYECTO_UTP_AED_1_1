package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.ConexionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EstudianteDAO implements IEstudianteDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstudianteDAO.class);

    public boolean insertar(String carnet, String nombre, String carrera) {
        String sql = "INSERT INTO Estudiantes (carnet, nombre, carrera) VALUES (?, ?, ?)";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, carnet);
            ps.setString(2, nombre);
            ps.setString(3, carrera);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error al insertar estudiante: {}", e.getMessage(), e);
            return false;
        }
    }

    public List<String[]> listar() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT carnet, nombre, carrera FROM Estudiantes";
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
            LOGGER.error("Error al listar estudiantes: {}", e.getMessage(), e);
        }
        return lista;
    }
}
