package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.ConexionSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatriculaDAO implements IMatriculaDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatriculaDAO.class);

    public boolean insertar(String carnet, String codigoCurso) {
        String sql = "INSERT INTO Matriculas (carnet, codigo_curso) VALUES (?, ?)";
        LOGGER.debug("Insertando matricula: {} -> {}", carnet, codigoCurso);
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, carnet);
            ps.setString(2, codigoCurso);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Matricula {} -> {} insertada en BD", carnet, codigoCurso);
            return exito;
        } catch (Exception e) {
            LOGGER.warn("BD no disponible para insertar matricula: {}", e.getMessage());
            return false;
        }
    }

    public List<String[]> listar() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT carnet, codigo_curso FROM Matriculas";
        LOGGER.debug("Listando matriculas desde BD");
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String[] row = new String[2];
                row[0] = rs.getString("carnet");
                row[1] = rs.getString("codigo_curso");
                lista.add(row);
            }
            LOGGER.info("{} matriculas cargadas desde BD", lista.size());
        } catch (Exception e) {
            LOGGER.warn("BD no disponible para listar matriculas: {}", e.getMessage());
        }
        return lista;
    }

    public boolean eliminar(String carnet, String codigoCurso) {
        String sql = "DELETE FROM Matriculas WHERE carnet = ? AND codigo_curso = ?";
        try (Connection cn = ConexionSQL.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, carnet);
            ps.setString(2, codigoCurso);
            boolean exito = ps.executeUpdate() > 0;
            if (exito) LOGGER.info("Matricula {} -> {} eliminada de BD", carnet, codigoCurso);
            return exito;
        } catch (Exception e) {
            LOGGER.warn("BD no disponible para eliminar matricula: {}", e.getMessage());
            return false;
        }
    }
}
