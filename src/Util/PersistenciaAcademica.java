package util;

import estructuras.ArbolEstudiantes;
import estructuras.ArregloCursos;
import estructuras.ListaCursos;
import estructuras.ListaMatricula;
import estructuras.MatrizSemestres;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import modelo.Solicitud;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Guarda y carga datos en disco (UTF-8) para conservarlos entre sesiones.
 * Ubicación: carpeta {@value #DIR_NAME} dentro del directorio de inicio del usuario.
 */
public final class PersistenciaAcademica {

    public static final String DIR_NAME = ".proyecto_utp_aed_datos";
    private static final String CURSOS = "cursos.tsv";
    private static final String ESTUDIANTES = "estudiantes.tsv";
    private static final String MATRICULAS = "matriculas.tsv";
    private static final String SOLICITUDES = "solicitudes.tsv";

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private PersistenciaAcademica() {
    }

    private static Path directorio() {
        return Paths.get(System.getProperty("user.home"), DIR_NAME);
    }

    private static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\t", " ").replace("\r", " ").replace("\n", " ");
    }

    public static void guardar(ArregloCursos arregloCursos,
                               ArbolEstudiantes arbolEstudiantes,
                               MatrizSemestres matrizSemestres,
                               ListaCursos listaCursos,
                               ListaMatricula listaMatricula,
                               Queue<Solicitud> colaSolicitudes) {
        try {
            Path dir = directorio();
            Files.createDirectories(dir);

            try (BufferedWriter w = Files.newBufferedWriter(dir.resolve(CURSOS), StandardCharsets.UTF_8)) {
                for (Curso c : arregloCursos.obtenerCursos()) {
                    if (c == null) {
                        continue;
                    }
                    w.write(esc(c.getCodigo()));
                    w.write('\t');
                    w.write(esc(c.getNombre()));
                    w.write('\t');
                    w.write(Integer.toString(c.getCreditos()));
                    w.write('\t');
                    w.write(Integer.toString(c.getSemestre()));
                    w.newLine();
                }
            }

            List<Estudiante> estudiantes = new ArrayList<>();
            arbolEstudiantes.inorden(estudiantes::add);
            try (BufferedWriter w = Files.newBufferedWriter(dir.resolve(ESTUDIANTES), StandardCharsets.UTF_8)) {
                for (Estudiante est : estudiantes) {
                    w.write(esc(est.getCarnet()));
                    w.write('\t');
                    w.write(esc(est.getNombre()));
                    w.write('\t');
                    w.write(esc(est.getCarrera()));
                    w.newLine();
                }
            }

            List<Matricula> matriculas = new ArrayList<>();
            listaMatricula.recorrer(matriculas::add);
            try (BufferedWriter w = Files.newBufferedWriter(dir.resolve(MATRICULAS), StandardCharsets.UTF_8)) {
                for (Matricula m : matriculas) {
                    w.write(esc(m.getEstudiante().getCarnet()));
                    w.write('\t');
                    w.write(esc(m.getCurso().getCodigo()));
                    w.newLine();
                }
            }

            try (BufferedWriter w = Files.newBufferedWriter(dir.resolve(SOLICITUDES), StandardCharsets.UTF_8)) {
                for (Solicitud s : colaSolicitudes) {
                    w.write(esc(s.getCarnet()));
                    w.write('\t');
                    w.write(esc(s.getTipo()));
                    w.write('\t');
                    w.write(esc(s.getDescripcion()));
                    w.write('\t');
                    w.write(s.getFecha() != null ? s.getFecha().format(ISO) : "");
                    w.write('\t');
                    w.write(s.isAtendida() ? "1" : "0");
                    w.write('\t');
                    w.write(s.getFechaAtencion() != null ? s.getFechaAtencion().format(ISO) : "");
                    w.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Persistencia: no se pudo guardar: " + e.getMessage());
        }
    }

    public static void cargar(ArregloCursos arregloCursos,
                              MatrizSemestres matrizSemestres,
                              ListaCursos listaCursos,
                              ArbolEstudiantes arbolEstudiantes,
                              ListaMatricula listaMatricula,
                              Queue<Solicitud> colaSolicitudes) {
        Path dir = directorio();
        if (!Files.isDirectory(dir)) {
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

        Path pc = dir.resolve(CURSOS);
        if (Files.isRegularFile(pc)) {
            try {
                for (String linea : Files.readAllLines(pc, StandardCharsets.UTF_8)) {
                    if (linea.isBlank() || linea.startsWith("#")) {
                        continue;
                    }
                    String[] p = linea.split("\t", -1);
                    if (p.length < 4) {
                        continue;
                    }
                    try {
                        int creditos = Integer.parseInt(p[2].trim());
                        int semestre = Integer.parseInt(p[3].trim());
                        Curso c = new Curso(p[0].trim(), p[1].trim(), creditos, semestre);
                        arregloCursos.insertar(c);
                        matrizSemestres.insertarPorSemestre(c);
                        listaCursos.insertar(c);
                    } catch (NumberFormatException ignored) {
                        // línea inválida
                    }
                }
            } catch (IOException e) {
                System.err.println("Persistencia: error leyendo cursos: " + e.getMessage());
            }
        }

        Path pe = dir.resolve(ESTUDIANTES);
        if (Files.isRegularFile(pe)) {
            try {
                for (String linea : Files.readAllLines(pe, StandardCharsets.UTF_8)) {
                    if (linea.isBlank() || linea.startsWith("#")) {
                        continue;
                    }
                    String[] p = linea.split("\t", -1);
                    if (p.length < 3) {
                        continue;
                    }
                    arbolEstudiantes.insertar(new Estudiante(p[0].trim(), p[1].trim(), p[2].trim()));
                }
            } catch (IOException e) {
                System.err.println("Persistencia: error leyendo estudiantes: " + e.getMessage());
            }
        }

        Path pm = dir.resolve(MATRICULAS);
        if (Files.isRegularFile(pm)) {
            try {
                for (String linea : Files.readAllLines(pm, StandardCharsets.UTF_8)) {
                    if (linea.isBlank() || linea.startsWith("#")) {
                        continue;
                    }
                    String[] p = linea.split("\t", -1);
                    if (p.length < 2) {
                        continue;
                    }
                    String carnet = p[0].trim();
                    String codigo = p[1].trim();
                    Estudiante est = arbolEstudiantes.buscar(carnet);
                    Curso cur = listaCursos.buscar(codigo);
                    if (est != null && cur != null) {
                        listaMatricula.agregar(new Matricula(est, cur));
                    }
                }
            } catch (IOException e) {
                System.err.println("Persistencia: error leyendo matrículas: " + e.getMessage());
            }
        }

        listaMatricula.recorrer(m -> CuentasEstudiantes.registrarPorMatricula(m.getEstudiante().getCarnet()));

        Path ps = dir.resolve(SOLICITUDES);
        if (Files.isRegularFile(ps)) {
            try {
                for (String linea : Files.readAllLines(ps, StandardCharsets.UTF_8)) {
                    if (linea.isBlank() || linea.startsWith("#")) {
                        continue;
                    }
                    String[] p = linea.split("\t", -1);
                    if (p.length < 6) {
                        continue;
                    }
                    LocalDateTime fecha = null;
                    LocalDateTime fechaAt = null;
                    try {
                        if (!p[3].trim().isEmpty()) {
                            fecha = LocalDateTime.parse(p[3].trim(), ISO);
                        }
                    } catch (DateTimeParseException ignored) {
                    }
                    try {
                        if (!p[5].trim().isEmpty()) {
                            fechaAt = LocalDateTime.parse(p[5].trim(), ISO);
                        }
                    } catch (DateTimeParseException ignored) {
                    }
                    boolean atendida = "1".equals(p[4].trim()) || "true".equalsIgnoreCase(p[4].trim());
                    Solicitud sol = new Solicitud(
                            p[0].trim(),
                            p[1].trim(),
                            p[2].trim(),
                            fecha != null ? fecha : LocalDateTime.now(),
                            atendida,
                            fechaAt
                    );
                    colaSolicitudes.add(sol);
                }
            } catch (IOException e) {
                System.err.println("Persistencia: error leyendo solicitudes: " + e.getMessage());
            }
        }
    }
}
