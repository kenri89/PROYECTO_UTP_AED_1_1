package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.ConexionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EstudianteDAO implements IEstudianteDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstudianteDAO.class);

    public boolean insertar(String carnet, String nombre, String carrera) {
        String sql = "INSERT INTO Estudiantes (carnet, nombre, carrera) VALUES (?, ?, ?)";
        LOGGER.debug("Insertando estudiante: {} - {} ({})", carnet, nombre, carrera);
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, carnet);
            ps.setString(2, nombre);
            ps.setString(3, carrera);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Estudiante {} insertado en BD", carnet);
            return exito;
        } catch (Exception e) {
            LOGGER.error("Error al insertar estudiante {}: {}", carnet, e.getMessage(), e);
            return false;
        }
    }

    public List<String[]> listar() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT carnet, nombre, carrera FROM Estudiantes";
        LOGGER.debug("Listando estudiantes desde BD");
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
            LOGGER.info("{} estudiantes cargados desde BD", lista.size());
        } catch (Exception e) {
            LOGGER.warn("BD no disponible para listar estudiantes: {}", e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(String carnet, String nombre, String carrera) {
        String sql = "UPDATE Estudiantes SET nombre = ?, carrera = ? WHERE carnet = ?";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, carrera);
            ps.setString(3, carnet);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Estudiante {} actualizado en BD", carnet);
            return exito;
        } catch (Exception e) {
            LOGGER.error("Error al actualizar estudiante {}: {}", carnet, e.getMessage(), e);
            return false;
        }
    }

    public boolean eliminar(String carnet) {
        String sql = "DELETE FROM Estudiantes WHERE carnet = ?";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, carnet);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Estudiante {} eliminado de BD", carnet);
            return exito;
        } catch (Exception e) {
            LOGGER.error("Error al eliminar estudiante {}: {}", carnet, e.getMessage(), e);
            return false;
        }
    }
}
