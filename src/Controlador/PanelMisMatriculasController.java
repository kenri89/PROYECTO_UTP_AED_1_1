package Controlador;

import gui.PanelMisMatriculas;
import modelo.Matricula;
import modelo.Estudiante;
import modelo.Curso;
import estructuras.ArbolEstudiantes;
import estructuras.ListaMatricula;
import dao.EstudianteDAO;
import dao.MatriculaDAO;
import dao.CursoDAO;
import util.ExportadorExcel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PanelMisMatriculasController {

    private final PanelMisMatriculas vista;
    private final ListaMatricula listaMatricula;
    private final String carnet;
    private final ArbolEstudiantes arbolEstudiantes;

    private final EstudianteDAO estudianteDAO;
    private final MatriculaDAO matriculaDAO;
    private final CursoDAO cursoDAO;

    public PanelMisMatriculasController(PanelMisMatriculas vista, ListaMatricula listaMatricula, 
                                        String carnet, ArbolEstudiantes arbolEstudiantes) {
        this.vista = vista;
        this.listaMatricula = listaMatricula;
        this.carnet = carnet == null ? "" : carnet.trim();
        this.arbolEstudiantes = arbolEstudiantes;

        this.estudianteDAO = new EstudianteDAO();
        this.matriculaDAO = new MatriculaDAO();
        this.cursoDAO = new CursoDAO();

        inicializarEncabezado();
        inicializarEventos();
        actualizarDatosYTabla();
    }

    private void inicializarEncabezado() {
        String tituloCarnet = this.carnet.isEmpty() ? "(sin carnet asignado)" : this.carnet;
        
        // Buscamos al estudiante localmente para construir el subtítulo
        Estudiante est = this.carnet.isEmpty() ? null : arbolEstudiantes.buscar(this.carnet);
        String nombre = est != null ? est.getNombre() : null;
        
        String subtitulo = nombre != null
                ? "Carnet: " + tituloCarnet + " — " + nombre
                : "Carnet: " + tituloCarnet + (this.carnet.isEmpty() ? "" : " (registra este estudiante en Gestión de Estudiantes si no aparece el nombre)");

        vista.actualizarEncabezado(subtitulo);
    }

    private void inicializarEventos() {
        vista.getBtnActualizar().addActionListener(e -> actualizarDatosYTabla());
        vista.getBtnExportar().addActionListener(e -> exportarReporte());
    }

    private void actualizarDatosYTabla() {
        cargarDatosDesdeSQL();
        inicializarEncabezado(); // Volvemos a inicializar por si el nombre ya se registró en la BD
        cargarTabla();
    }

    private void cargarDatosDesdeSQL() {
        List<String[]> estudiantesSQL = estudianteDAO.listar();
        List<String[]> cursosSQL = cursoDAO.listar();
        List<String[]> matriculasSQL = matriculaDAO.listar();

        arbolEstudiantes.limpiar();
        for (String[] datos : estudiantesSQL) {
            arbolEstudiantes.insertar(new Estudiante(datos[0], datos[1], datos[2]));
        }

        listaMatricula.limpiar();
        for (String[] datos : matriculasSQL) {
            String carnetMat = datos[0];
            String codigoCurso = datos[1];

            Estudiante est = arbolEstudiantes.buscar(carnetMat);
            Curso curso = null;
            for (String[] cd : cursosSQL) {
                if (cd[0].equals(codigoCurso)) {
                    curso = new Curso(cd[0], cd[1], Integer.parseInt(cd[2]), Integer.parseInt(cd[3]));
                    break;
                }
            }

            if (est != null && curso != null) {
                listaMatricula.agregar(new Matricula(est, curso));
            }
        }
    }

    private void cargarTabla() {
        vista.limpiarTabla();
        
        if (carnet.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Tu usuario no tiene carnet vinculado. Contacta a la secretaría o al administrador.",
                    "Mis matrículas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Matricula> mis = listaMatricula.buscarPorCarnet(carnet);
        if (mis.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "No hay matrículas registradas para el carnet " + carnet + ".\n"
                            + "La secretaría debe registrarte y matricularte en \"Matrícula\".",
                    "Mis matrículas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Matricula m : mis) {
            vista.agregarFila(new Object[]{
                    m.getCurso().getCodigo(),
                    m.getCurso().getNombre(),
                    m.getCurso().getCreditos(),
                    m.getCurso().getSemestre()
            });
        }
    }

    private void exportarReporte() {
        List<Matricula> mis = listaMatricula.buscarPorCarnet(carnet);
        if (mis.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No tienes matrículas que exportar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("reporte_matriculas.xls"));
        if (fc.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            try {
                ListaMatricula filtrada = new ListaMatricula();
                for (Matricula m : mis) {
                    filtrada.agregar(m);
                }
                ExportadorExcel.exportarMatriculas(filtrada, fc.getSelectedFile());
                JOptionPane.showMessageDialog(vista, "Reporte descargado exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista, "Error al descargar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}