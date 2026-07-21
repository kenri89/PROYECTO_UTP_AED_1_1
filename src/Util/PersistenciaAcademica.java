package util;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import estructuras.ArbolEstudiantes;
import estructuras.ArregloCursos;
import estructuras.ListaCursos;
import estructuras.ListaMatricula;
import estructuras.MatrizSemestres;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import modelo.Solicitud;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public final class PersistenciaAcademica {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenciaAcademica.class);

    private static final Joiner TAB_JOINER = Joiner.on('\t').useForNull("");
    private static final Splitter TAB_SPLITTER = Splitter.on('\t').trimResults();

    public static final String DIR_NAME = ".proyecto_utp_aed_datos";
    private static final String CURSOS = "cursos.tsv";
    private static final String ESTUDIANTES = "estudiantes.tsv";
    private static final String MATRICULAS = "matriculas.tsv";
    private static final String SOLICITUDES = "solicitudes.tsv";

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private PersistenciaAcademica() {
    }

    private static File directorioFile() {
        return new File(System.getProperty("user.home"), DIR_NAME);
    }

    private static String esc(String s) {
        return Strings.nullToEmpty(s).replace("\t", " ").replace("\r", " ").replace("\n", " ");
    }

    @FunctionalInterface
    private interface LineParser {
        void parse(List<String> columns) throws IOException;
    }

    private static void leerTsv(File dir, String nombre, int columnas, LineParser parser) {
        File archivo = new File(dir, nombre);
        if (!archivo.isFile()) {
            return;
        }
        try {
            List<String> lineas = FileUtils.readLines(archivo, StandardCharsets.UTF_8.name());
            for (String linea : lineas) {
                if (linea.isBlank() || linea.startsWith("#")) {
                    continue;
                }
                List<String> p = ImmutableList.copyOf(TAB_SPLITTER.split(linea));
                if (p.size() < columnas) {
                    continue;
                }
                parser.parse(p);
            }
        } catch (IOException e) {
            LOGGER.error("Persistencia: error leyendo {}", nombre, e);
        }
    }

    public static void guardar(ArregloCursos arregloCursos,
                               ArbolEstudiantes arbolEstudiantes,
                               MatrizSemestres matrizSemestres,
                               ListaCursos listaCursos,
                               ListaMatricula listaMatricula,
                               Queue<Solicitud> colaSolicitudes) {
        try {
            FileUtils.forceMkdir(directorioFile());

            List<String> lineasCursos = new ArrayList<>();
            for (Curso c : arregloCursos.obtenerCursos()) {
                if (c == null) continue;
                lineasCursos.add(TAB_JOINER.join(esc(c.getCodigo()), esc(c.getNombre()),
                        c.getCreditos(), c.getSemestre()));
            }
            FileUtils.writeLines(new File(directorioFile(), CURSOS), StandardCharsets.UTF_8.name(), lineasCursos);

            List<String> lineasEstudiantes = new ArrayList<>();
            List<Estudiante> estudiantes = new ArrayList<>();
            arbolEstudiantes.inorden(estudiantes::add);
            for (Estudiante est : estudiantes) {
                lineasEstudiantes.add(TAB_JOINER.join(esc(est.getCarnet()), esc(est.getNombre()), esc(est.getCarrera())));
            }
            FileUtils.writeLines(new File(directorioFile(), ESTUDIANTES), StandardCharsets.UTF_8.name(), lineasEstudiantes);

            List<String> lineasMatriculas = new ArrayList<>();
            List<Matricula> matriculas = new ArrayList<>();
            listaMatricula.recorrer(matriculas::add);
            for (Matricula m : matriculas) {
                lineasMatriculas.add(TAB_JOINER.join(esc(m.getEstudiante().getCarnet()), esc(m.getCurso().getCodigo())));
            }
            FileUtils.writeLines(new File(directorioFile(), MATRICULAS), StandardCharsets.UTF_8.name(), lineasMatriculas);

            List<String> lineasSolicitudes = new ArrayList<>();
            for (Solicitud s : colaSolicitudes) {
                lineasSolicitudes.add(TAB_JOINER.join(
                        esc(s.getCarnet()), esc(s.getTipo()), esc(s.getDescripcion()),
                        s.getFecha() != null ? s.getFecha().format(ISO) : "",
                        s.isAtendida() ? "1" : "0",
                        s.getFechaAtencion() != null ? s.getFechaAtencion().format(ISO) : ""));
            }
            FileUtils.writeLines(new File(directorioFile(), SOLICITUDES), StandardCharsets.UTF_8.name(), lineasSolicitudes);

            LOGGER.info("Datos guardados exitosamente en {}", directorioFile());
        } catch (IOException e) {
            LOGGER.error("Persistencia: no se pudo guardar", e);
        }
    }

    public static void cargar(ArregloCursos arregloCursos,
                              MatrizSemestres matrizSemestres,
                              ListaCursos listaCursos,
                              ArbolEstudiantes arbolEstudiantes,
                              ListaMatricula listaMatricula,
                              Queue<Solicitud> colaSolicitudes) {
        File dir = directorioFile();
        if (!dir.isDirectory()) {
            return;
        }

        arregloCursos.limpiar();
        matrizSemestres.limpiar();
        listaCursos.limpiar();
        arbolEstudiantes.limpiar();
        listaMatricula.limpiar();
        while (!colaSolicitudes.isEmpty()) {
            colaSolicitudes.poll();
        }

        leerTsv(dir, CURSOS, 4, p -> {
            int creditos = Integer.parseInt(checkNotNull(p.get(2)).trim());
            int semestre = Integer.parseInt(checkNotNull(p.get(3)).trim());
            Curso c = new Curso(p.get(0).trim(), p.get(1).trim(), creditos, semestre);
            arregloCursos.insertar(c);
            matrizSemestres.insertarPorSemestre(c);
            listaCursos.insertar(c);
        });

        leerTsv(dir, ESTUDIANTES, 3, p -> {
            arbolEstudiantes.insertar(new Estudiante(p.get(0).trim(), p.get(1).trim(), p.get(2).trim()));
        });

        leerTsv(dir, MATRICULAS, 2, p -> {
            String carnet = p.get(0).trim();
            String codigo = p.get(1).trim();
            Estudiante est = arbolEstudiantes.buscar(carnet);
            Curso cur = listaCursos.buscar(codigo);
            if (est != null && cur != null) {
                listaMatricula.agregar(new Matricula(est, cur));
            }
        });

        listaMatricula.recorrer(m -> CuentasEstudiantes.registrarPorMatricula(m.getEstudiante().getCarnet()));

        leerTsv(dir, SOLICITUDES, 6, p -> {
            LocalDateTime fecha = null;
            LocalDateTime fechaAt = null;
            try {
                String f = p.get(3).trim();
                if (!f.isEmpty()) fecha = LocalDateTime.parse(f, ISO);
            } catch (DateTimeParseException ignored) {}
            try {
                String fa = p.get(5).trim();
                if (!fa.isEmpty()) fechaAt = LocalDateTime.parse(fa, ISO);
            } catch (DateTimeParseException ignored) {}
            boolean atendida = "1".equals(p.get(4).trim()) || "true".equalsIgnoreCase(p.get(4).trim());
            Solicitud sol = new Solicitud(
                    p.get(0).trim(), p.get(1).trim(), p.get(2).trim(),
                    fecha != null ? fecha : LocalDateTime.now(), atendida, fechaAt);
            colaSolicitudes.add(sol);
        });

        LOGGER.info("Datos cargados exitosamente desde {}", dir);
    }
}
