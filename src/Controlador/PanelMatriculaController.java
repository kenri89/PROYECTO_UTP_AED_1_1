package Controlador;

import gui.PanelMatricula;
import modelo.Matricula;
import modelo.Estudiante;
import modelo.Curso;
import estructuras.ArbolEstudiantes;
import estructuras.ListaCursos;
import estructuras.ListaMatricula;
import estructuras.PilaAcciones_U3;
import estructuras.AccionMatricula;
import dao.MatriculaDAO;
import util.CuentasEstudiantes;
import util.ExportadorExcel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PanelMatriculaController {

    private final PanelMatricula vista;
    private final ListaCursos listaCursos;
    private final ArbolEstudiantes arbolEstudiantes;
    private final ListaMatricula listaMatricula;
    private final PilaAcciones_U3 pilaHistorial;
    private final MatriculaDAO matriculaDAO;

    private Matricula matriculaSeleccionada = null;

    public PanelMatriculaController(PanelMatricula vista, ListaCursos listaCursos, 
                                    ArbolEstudiantes arbolEstudiantes, ListaMatricula listaMatricula, 
                                    PilaAcciones_U3 pilaHistorial) {
        this.vista = vista;
        this.listaCursos = listaCursos;
        this.arbolEstudiantes = arbolEstudiantes;
        this.listaMatricula = listaMatricula;
        this.pilaHistorial = pilaHistorial;
        this.matriculaDAO = new MatriculaDAO();

        inicializarEventos();
        inicializarDatos();
    }

    private void inicializarEventos() {
        vista.getBtnRegistrar().addActionListener(e -> registrarMatricula());
        vista.getBtnActualizar().addActionListener(e -> actualizarMatricula());
        vista.getBtnVer().addActionListener(e -> cargarTabla());
        vista.getBtnDeshacer().addActionListener(e -> deshacerUltimaAccion());
        vista.getBtnExportar().addActionListener(e -> exportarExcel());

        vista.getItemActualizar().addActionListener(e -> seleccionarMatriculaParaEdicion());
        vista.getItemEliminar().addActionListener(e -> eliminarMatricula());
    }

    private void inicializarDatos() {
        cargarCombos();
        try {
            cargarMatriculasDesdeSQL();
        } catch (Exception e) {
            System.err.println("Error al cargar matriculas desde BD: " + e.getMessage());
        }
    }

    private void cargarCombos() {
        List<String> carnets = new ArrayList<>();
        arbolEstudiantes.inorden(est -> carnets.add(est.getCarnet()));

        List<String> codigos = new ArrayList<>();
        listaCursos.recorrer(curso -> codigos.add(curso.getCodigo()));

        vista.rellenarCombos(
            carnets.toArray(new String[0]), 
            codigos.toArray(new String[0])
        );
    }

    private void cargarMatriculasDesdeSQL() {
        List<String[]> matriculasSQL = matriculaDAO.listar();
        listaMatricula.limpiar();

        for (String[] datos : matriculasSQL) {
            String carnet = datos[0];
            String codigoCurso = datos[1];

            Estudiante est = arbolEstudiantes.buscar(carnet);
            Curso curso = listaCursos.buscar(codigoCurso);

            if (est != null && curso != null) {
                listaMatricula.agregar(new Matricula(est, curso));
            }
        }
        cargarTabla();
    }

    private void cargarTabla() {
        vista.limpiarTabla();
        listaMatricula.recorrer(m -> vista.agregarFila(new Object[]{
                m.getEstudiante().getCarnet(),
                m.getEstudiante().getNombre(),
                m.getCurso().getCodigo(),
                m.getCurso().getNombre()
        }));
    }

    private void registrarMatricula() {
        String carnet = vista.getCarnetSeleccionado();
        String codigo = vista.getCodigoCursoSeleccionado();

        if (carnet == null || codigo == null) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un estudiante y un curso.");
            return;
        }

        Estudiante est = arbolEstudiantes.buscar(carnet);
        Curso curso = listaCursos.buscar(codigo);

        if (est == null || curso == null) {
            JOptionPane.showMessageDialog(vista, "Estudiante o curso no encontrado.");
            return;
        }

        boolean exitoSQL = matriculaDAO.insertar(carnet, codigo);

        if (exitoSQL) {
            CuentasEstudiantes.registrarPorMatricula(carnet);
            // Registrar acción en la pila de historial si es necesario
            pilaHistorial.apilar(new AccionMatricula("REGISTRO", new Matricula(est, curso)));
            
            JOptionPane.showMessageDialog(vista, "Matrícula registrada con éxito en la base de datos.");
            cargarMatriculasDesdeSQL();
        } else {
            JOptionPane.showMessageDialog(vista, "Error: no se pudo guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarMatriculaParaEdicion() {
        int fila = vista.getFilaSeleccionada();
        if (fila >= 0) {
            String carnet = (String) vista.getValueAt(fila, 0);
            String codCurso = (String) vista.getValueAt(fila, 2);
            matriculaSeleccionada = listaMatricula.buscar(carnet, codCurso);
            if (matriculaSeleccionada != null) {
                vista.setSeleccionCombos(carnet, codCurso);
            }
        }
    }

    private void actualizarMatricula() {
        if (matriculaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar una matrícula con clic derecho.");
            return;
        }

        String nuevoCarnet = vista.getCarnetSeleccionado();
        String nuevoCurso = vista.getCodigoCursoSeleccionado();

        Estudiante nuevoEst = arbolEstudiantes.buscar(nuevoCarnet);
        Curso nuevoCur = listaCursos.buscar(nuevoCurso);

        if (nuevoEst == null || nuevoCur == null) {
            JOptionPane.showMessageDialog(vista, "Datos inválidos.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista, "¿Desea actualizar esta matrícula?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String carnetViejo = matriculaSeleccionada.getEstudiante().getCarnet();
            String cursoViejo = matriculaSeleccionada.getCurso().getCodigo();

            // Guardamos el estado anterior para la opción Deshacer
            pilaHistorial.apilar(new AccionMatricula("ACTUALIZACIÓN", matriculaSeleccionada));

            matriculaDAO.eliminar(carnetViejo, cursoViejo);
            matriculaDAO.insertar(nuevoCarnet, nuevoCurso);

            cargarMatriculasDesdeSQL();
            matriculaSeleccionada = null;
        }
    }

    private void eliminarMatricula() {
        int fila = vista.getFilaSeleccionada();
        if (fila >= 0) {
            String carnet = (String) vista.getValueAt(fila, 0);
            String codigo = (String) vista.getValueAt(fila, 2);

            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Desea eliminar la matrícula de " + carnet + " en curso " + codigo + "?",
                    "Confirmación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Matricula aEliminar = listaMatricula.buscar(carnet, codigo);
                if (aEliminar != null) {
                    pilaHistorial.apilar(new AccionMatricula("ELIMINACIÓN", aEliminar));
                }
                
                matriculaDAO.eliminar(carnet, codigo);
                cargarMatriculasDesdeSQL();
            }
        }
    }

    private void deshacerUltimaAccion() {
        if (pilaHistorial.estaVacia()) {
            JOptionPane.showMessageDialog(vista, "No hay acciones para deshacer.");
            return;
        }

        AccionMatricula ultima = pilaHistorial.desapilar();

        switch (ultima.getTipo()) {
            case "REGISTRO":
                Matricula reg = ultima.getMatricula();
                matriculaDAO.eliminar(reg.getEstudiante().getCarnet(), reg.getCurso().getCodigo());
                break;
            case "ELIMINACIÓN":
                Matricula elim = ultima.getMatricula();
                matriculaDAO.insertar(elim.getEstudiante().getCarnet(), elim.getCurso().getCodigo());
                break;
            case "ACTUALIZACIÓN":
                Matricula anterior = ultima.getMatricula();
                Matricula actual = listaMatricula.buscar(anterior.getEstudiante().getCarnet(), anterior.getCurso().getCodigo());
                if (actual != null) {
                    String carnetActual = actual.getEstudiante().getCarnet();
                    String cursoActual = actual.getCurso().getCodigo();
                    matriculaDAO.eliminar(carnetActual, cursoActual);
                    matriculaDAO.insertar(anterior.getEstudiante().getCarnet(), anterior.getCurso().getCodigo());
                }
                break;
        }

        cargarMatriculasDesdeSQL();
    }

    private void exportarExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("matriculas.xls"));
        if (fc.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            try {
                ExportadorExcel.exportarMatriculas(listaMatricula, fc.getSelectedFile());
                JOptionPane.showMessageDialog(vista, "Matrículas exportadas a Excel exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}