package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.ConexionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CursoDAO implements ICursoDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CursoDAO.class);

    public boolean insertar(String codigo, String nombre, int creditos, int semestre) {
        String sql = "INSERT INTO Cursos (codigo, nombre, creditos, semestre) VALUES (?, ?, ?, ?)";
        LOGGER.debug("Insertando curso: {} - {} ({} creditos, semestre {})", codigo, nombre, creditos, semestre);
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setInt(3, creditos);
            ps.setInt(4, semestre);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Curso {} insertado en BD", codigo);
            return exito;
        } catch (Exception e) {
            LOGGER.error("Error al insertar curso {}: {}", codigo, e.getMessage(), e);
            return false;
        }
    }

    public List<String[]> listar() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre, creditos, semestre FROM Cursos";
        LOGGER.debug("Listando cursos desde BD");
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String[] curso = new String[4];
                curso[0] = rs.getString("codigo");
                curso[1] = rs.getString("nombre");
                curso[2] = String.valueOf(rs.getInt("creditos"));
                curso[3] = String.valueOf(rs.getInt("semestre"));
                lista.add(curso);
            }
            LOGGER.info("{} cursos cargados desde BD", lista.size());
        } catch (Exception e) {
            LOGGER.warn("BD no disponible para listar cursos: {}", e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(String codigo, String nombre, int creditos, int semestre) {
        String sql = "UPDATE Cursos SET nombre = ?, creditos = ?, semestre = ? WHERE codigo = ?";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, creditos);
            ps.setInt(3, semestre);
            ps.setString(4, codigo);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Curso {} actualizado en BD", codigo);
            return exito;
        } catch (Exception e) {
            LOGGER.error("Error al actualizar curso {}: {}", codigo, e.getMessage(), e);
            return false;
        }
    }

    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM Cursos WHERE codigo = ?";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Curso {} eliminado de BD", codigo);
            return exito;
        } catch (Exception e) {
            LOGGER.error("Error al eliminar curso {}: {}", codigo, e.getMessage(), e);
            return false;
        }
    }
}
