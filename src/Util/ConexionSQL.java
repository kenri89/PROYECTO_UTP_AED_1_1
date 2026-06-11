package util;

import com.google.common.base.Strings;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConexionSQL {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConexionSQL.class);
    private static Properties props = null;

    private static final Properties DEFAULTS = new Properties();
    static {
        DEFAULTS.setProperty("db.server", "localhost");
        DEFAULTS.setProperty("db.port", "1433");
        DEFAULTS.setProperty("db.name", "iestp_peru_japon");
        DEFAULTS.setProperty("db.user", "sa");
        DEFAULTS.setProperty("db.password", "1234");
        DEFAULTS.setProperty("db.encrypt", "true");
        DEFAULTS.setProperty("db.trustCertificate", "true");
    }

    private static Properties cargarProperties() {
        if (props != null) {
            return props;
        }
        props = new Properties(DEFAULTS);
        try (InputStream in = ConexionSQL.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
                LOGGER.info("config.properties cargado correctamente");
            } else {
                LOGGER.warn("No se encontró config.properties, se usarán valores por defecto");
            }
        } catch (IOException e) {
            LOGGER.error("Error cargando config.properties, usando valores por defecto", e);
        }
        return props;
    }

    public static Connection getConexion() {
        Connection cn = null;
        try {
            Properties p = cargarProperties();
            String server = p.getProperty("db.server");
            String port = p.getProperty("db.port");
            String dbName = p.getProperty("db.name");
            String url = "jdbc:sqlserver://" + server + ":" + port
                    + ";databaseName=" + dbName
                    + ";encrypt=" + p.getProperty("db.encrypt")
                    + ";trustServerCertificate=" + p.getProperty("db.trustCertificate") + ";";
            LOGGER.debug("Conectando a {}:{}/{}", server, port, dbName);
            cn = DriverManager.getConnection(url, p.getProperty("db.user"), p.getProperty("db.password"));
            LOGGER.info("Conexión exitosa a {}", dbName);
        } catch (SQLException e) {
            LOGGER.error("Error de conexión a BD: {}",
                    Strings.nullToEmpty(e.getMessage()), e);
        }
        return cn;
    }
}
