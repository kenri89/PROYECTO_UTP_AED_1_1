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

    // SQL Queries
    private static final String INSERT_SQL = "INSERT INTO Estudiantes (carnet, nombre, carrera) VALUES (?, ?, ?)";
    private static final String SELECT_SQL = "SELECT carnet, nombre, carrera FROM Estudiantes";
    private static final String UPDATE_SQL = "UPDATE Estudiantes SET nombre = ?, carrera = ? WHERE carnet = ?";
    private static final String DELETE_SQL = "DELETE FROM Estudiantes WHERE carnet = ?";

    @Override
    public boolean insertar(String carnet, String nombre, String carrera) {
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, carnet);
            ps.setString(2, nombre);
            ps.setString(3, carrera);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error al insertar estudiante: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<String[]> listar() {
        List<String[]> lista = new ArrayList<>();
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(SELECT_SQL);
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

    @Override
    public boolean actualizar(String carnet, String nuevoNombre, String nuevaCarrera) {
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevaCarrera);
            ps.setString(3, carnet);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error al actualizar estudiante con carnet {}: {}", carnet, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean eliminar(String carnet) {
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(DELETE_SQL)) {
            ps.setString(1, carnet);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error al eliminar estudiante con carnet {}: {}", carnet, e.getMessage(), e);
            return false;
        }
    }
}