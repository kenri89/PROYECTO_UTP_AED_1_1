package Controlador;

import gui.PanelCursos;
import modelo.Curso;
import estructuras.ArregloCursos;
import estructuras.MatrizSemestres;
import estructuras.ListaCursos;
import dao.CursoDAO;
import util.ExportadorExcel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class PanelCursosController {

    private PanelCursos vista;
    private ArregloCursos arregloCursos;
    private MatrizSemestres matrizSemestres;
    private ListaCursos listaCursos;
    private CursoDAO cursoDAO;

    public PanelCursosController(PanelCursos vista, ArregloCursos arregloCursos, 
                                 MatrizSemestres matrizSemestres, ListaCursos listaCursos) {
        this.vista = vista;
        this.arregloCursos = arregloCursos;
        this.matrizSemestres = matrizSemestres;
        this.listaCursos = listaCursos;
        this.cursoDAO = new CursoDAO();

        // Registrar los listeners de los componentes de la vista
        this.vista.getBtnAgregar().addActionListener(e -> insertarCurso());
        this.vista.getBtnEliminar().addActionListener(e -> eliminarCurso());
        this.vista.getBtnActualizar().addActionListener(e -> confirmarYActualizarCurso());
        this.vista.getBtnExportar().addActionListener(e -> exportarExcel());
        this.vista.getItemActualizar().addActionListener(e -> cargarCursoSeleccionado());

        // Cargar datos iniciales
        try {
            cargarDatosDesdeSQL();
        } catch (Exception e) {
            System.err.println("Error al cargar datos desde BD (usando modo local): " + e.getMessage());
        }
    }

    private void cargarDatosDesdeSQL() {
        java.util.List<String[]> cursosSQL = cursoDAO.listar();

        arregloCursos.limpiar();
        matrizSemestres.limpiar();
        listaCursos.limpiar();

        for (String[] datos : cursosSQL) {
            String codigo = datos[0];
            String nombre = datos[1];
            int creditos = Integer.parseInt(datos[2]);
            int semestre = Integer.parseInt(datos[3]);

            Curso curso = new Curso(codigo, nombre, creditos, semestre);
            arregloCursos.insertar(curso);
            matrizSemestres.insertarPorSemestre(curso);
            listaCursos.insertar(curso);
        }

        actualizarTablaVista();
    }

    private void actualizarTablaVista() {
        vista.limpiarTabla();
        for (Curso curso : arregloCursos.obtenerCursos()) {
            vista.agregarFilaTabla(new Object[]{
                    curso.getCodigo(),
                    curso.getNombre(),
                    curso.getCreditos(),
                    curso.getSemestre()
            });
        }
    }

    private void insertarCurso() {
        String codigo = vista.getCodigoInput();
        String nombre = vista.getNombreInput();
        String creditosStr = vista.getCreditosInput();
        int semestre = vista.getSemestreSeleccionado();

        if (codigo.isEmpty() || nombre.isEmpty() || creditosStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, completa todos los campos.");
            return;
        }

        int creditos;
        try {
            creditos = Integer.parseInt(creditosStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Los créditos deben ser un número.");
            return;
        }

        boolean exitoSQL = cursoDAO.insertar(codigo, nombre, creditos, semestre);

        if (exitoSQL) {
            JOptionPane.showMessageDialog(vista, "Curso registrado con éxito en la base de datos.");
            cargarDatosDesdeSQL();
        } else {
            JOptionPane.showMessageDialog(vista, "Error: no se pudo guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        vista.limpiarCampos();
    }

    private void eliminarCurso() {
        int fila = vista.getTablaCursos().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un curso para eliminar.");
            return;
        }

        String codigo = vista.getModeloTabla().getValueAt(fila, 0).toString();
        int opcion = JOptionPane.showConfirmDialog(vista, "¿Deseas eliminar el curso con código " + codigo + "?",
                "Confirmación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        cursoDAO.eliminar(codigo);
        JOptionPane.showMessageDialog(vista, "Curso eliminado correctamente.");
        cargarDatosDesdeSQL();
    }

    private void confirmarYActualizarCurso() {
        String codigo = vista.getCodigoInput();
        String nombre = vista.getNombreInput();
        String creditosStr = vista.getCreditosInput();
        int semestre = vista.getSemestreSeleccionado();

        if (codigo.isEmpty() || nombre.isEmpty() || creditosStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos para actualizar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(vista,
                "¿Deseas actualizar este curso?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        try {
            int creditos = Integer.parseInt(creditosStr);
            cursoDAO.actualizar(codigo, nombre, creditos, semestre);

            JOptionPane.showMessageDialog(vista, "Curso actualizado correctamente.");
            cargarDatosDesdeSQL();
            vista.limpiarCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Créditos inválidos.");
        }
    }

    private void cargarCursoSeleccionado() {
        int fila = vista.getTablaCursos().getSelectedRow();
        if (fila == -1) return;

        String codigo = vista.getModeloTabla().getValueAt(fila, 0).toString();
        String nombre = vista.getModeloTabla().getValueAt(fila, 1).toString();
        String creditos = vista.getModeloTabla().getValueAt(fila, 2).toString();
        int semestre = Integer.parseInt(vista.getModeloTabla().getValueAt(fila, 3).toString());

        vista.rellenarCampos(codigo, nombre, creditos, semestre);
    }

    private void exportarExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("cursos.xls"));
        if (fc.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            try {
                ExportadorExcel.exportarCursos(arregloCursos, archivo);
                JOptionPane.showMessageDialog(vista, "Cursos exportados a Excel exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}