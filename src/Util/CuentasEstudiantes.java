package util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CuentasEstudiantes {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuentasEstudiantes.class);

    public static final String PASSWORD_DEFECTO = "1234";
    private static final String ARCHIVO = "cuentas_estudiantes.txt";

    private CuentasEstudiantes() {
    }

    private static Path archivo() {
        return Paths.get(System.getProperty("user.home"), PersistenciaAcademica.DIR_NAME, ARCHIVO);
    }

    public static void registrarPorMatricula(String carnetRaw) {
        String carnet = carnetRaw == null ? "" : carnetRaw.trim();
        if (carnet.isEmpty()) {
            return;
        }
        Path f = archivo();
        try {
            Files.createDirectories(f.getParent());
            Set<String> carnets = leerCarnets(f);
            if (carnets.contains(carnet)) {
                return;
            }
            carnets.add(carnet);
            escribirCarnets(f, carnets);
        } catch (IOException e) {
            LOGGER.error("CuentasEstudiantes: no se pudo guardar cuenta", e);
        }
    }

    private static Set<String> leerCarnets(Path f) throws IOException {
        Set<String> set = new LinkedHashSet<>();
        if (Files.isRegularFile(f)) {
            for (String linea : Files.readAllLines(f, StandardCharsets.UTF_8)) {
                String c = linea.trim();
                if (!c.isEmpty() && !c.startsWith("#")) {
                    set.add(c);
                }
            }
        }
        return set;
    }

    private static void escribirCarnets(Path f, Set<String> carnets) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(f, StandardCharsets.UTF_8)) {
            for (String c : carnets) {
                w.write(c);
                w.newLine();
            }
        }
    }

    public static boolean autenticar(String usuario, String password) {
        if (usuario == null || password == null || !PASSWORD_DEFECTO.equals(password)) {
            return false;
        }
        String u = usuario.trim();
        if (u.isEmpty()) {
            return false;
        }
        Path f = archivo();
        if (!Files.isRegularFile(f)) {
            return false;
        }
        try {
            for (String linea : Files.readAllLines(f, StandardCharsets.UTF_8)) {
                String c = linea.trim();
                if (!c.isEmpty() && c.equals(u)) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.error("CuentasEstudiantes: error al autenticar", e);
        }
        return false;
    }
}
