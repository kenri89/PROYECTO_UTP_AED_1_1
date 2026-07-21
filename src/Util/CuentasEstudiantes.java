package util;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class CuentasEstudiantes {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuentasEstudiantes.class);

    public static final String PASSWORD_DEFECTO = "1234";
    private static final String ARCHIVO = "cuentas_estudiantes.txt";

    private CuentasEstudiantes() {
    }

    private static File archivo() {
        return new File(new File(System.getProperty("user.home"), PersistenciaAcademica.DIR_NAME), ARCHIVO);
    }

    public static void registrarPorMatricula(String carnetRaw) {
        String carnet = Strings.nullToEmpty(carnetRaw).trim();
        if (carnet.isEmpty()) {
            return;
        }
        File f = archivo();
        try {
            FileUtils.forceMkdir(f.getParentFile());
            Set<String> carnets = leerCarnets(f);
            if (carnets.contains(carnet)) {
                return;
            }
            carnets.add(carnet);
            FileUtils.writeLines(f, StandardCharsets.UTF_8.name(), carnets);
        } catch (IOException e) {
            LOGGER.error("CuentasEstudiantes: no se pudo guardar cuenta", e);
        }
    }

    private static Set<String> leerCarnets(File f) throws IOException {
        Set<String> set = new LinkedHashSet<>();
        if (f.isFile()) {
            List<String> lineas = FileUtils.readLines(f, StandardCharsets.UTF_8.name());
            for (String linea : lineas) {
                String c = linea.trim();
                if (!c.isEmpty() && !c.startsWith("#")) {
                    set.add(c);
                }
            }
        }
        return set;
    }

    public static boolean autenticar(String usuario, String password) {
        if (usuario == null || password == null || !PASSWORD_DEFECTO.equals(password)) {
            return false;
        }
        String u = usuario.trim();
        if (u.isEmpty()) {
            return false;
        }
        File f = archivo();
        if (!f.isFile()) {
            return false;
        }
        try {
            List<String> lineas = FileUtils.readLines(f, StandardCharsets.UTF_8.name());
            for (String linea : lineas) {
                if (linea.trim().equals(u)) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.error("CuentasEstudiantes: error al autenticar", e);
        }
        return false;
    }
}
