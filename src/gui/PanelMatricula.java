package gui;

import estructuras.ArbolEstudiantes;
import estructuras.ListaCursos;
import estructuras.ListaMatricula;
import estructuras.PilaAcciones_U3;
import estructuras.AccionMatricula;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import util.CuentasEstudiantes;
import util.ExportadorExcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class PanelMatricula extends JPanel {

    private ListaCursos listaCursos;
    private ArbolEstudiantes arbolEstudiantes;
    private ListaMatricula listaMatricula;
    private PilaAcciones_U3 pilaHistorial;

    private JComboBox<String> comboCarnet;
    private JComboBox<String> comboCodigoCurso;
    private JTable tabla;
    private DefaultTableModel modelo;

    private Matricula matriculaSeleccionada = null;

    public PanelMatricula(ListaCursos listaCursos, ArbolEstudiantes arbolEstudiantes, ListaMatricula listaMatricula, PilaAcciones_U3 pilaHistorial) {
        this.listaCursos = listaCursos;
        this.arbolEstudiantes = arbolEstudiantes;
        this.listaMatricula = listaMatricula;
        this.pilaHistorial = pilaHistorial;

        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BG);

        agregarFormulario();
        agregarTabla();
        cargarCombos();
        try {
            cargarMatriculasDesdeSQL();
        } catch (Exception e) {
            System.err.println("Error al cargar matriculas desde BD: " + e.getMessage());
        }
    }

    private void agregarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de Matrícula"));
        panelForm.setBackground(UIConstants.PANEL_LIGHT);

        comboCarnet = new JComboBox<>();
        comboCodigoCurso = new JComboBox<>();

        panelForm.add(new JLabel("Carnet Estudiante:"));
        panelForm.add(comboCarnet);
        panelForm.add(new JLabel("Código Curso:"));
        panelForm.add(comboCodigoCurso);

        JButton btnRegistrar = UIConstants.crearBoton("Registrar");
        JButton btnActualizar = UIConstants.crearBoton("Actualizar");
        JButton btnVer = UIConstants.crearBoton("Ver Matrículas");
        JButton btnDeshacer = UIConstants.crearBoton("Deshacer");

        panelForm.add(btnRegistrar);
        panelForm.add(btnActualizar);
        panelForm.add(btnVer);
        panelForm.add(btnDeshacer);

        add(panelForm, BorderLayout.WEST);

        btnRegistrar.addActionListener(e -> registrarMatricula());
        btnActualizar.addActionListener(e -> actualizarMatricula());
        btnVer.addActionListener(e -> cargarTabla());
        btnDeshacer.addActionListener(e -> deshacerUltimaAccion());
    }

    private void agregarTabla() {
        modelo = new DefaultTableModel(new Object[]{"Carnet", "Nombre", "Curso", "Nombre Curso"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Matrículas"));

        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemActualizar = new JMenuItem("Actualizar");
        JMenuItem itemEliminar = new JMenuItem("Eliminar");

        menu.add(itemActualizar);
        menu.add(itemEliminar);

        tabla.setComponentPopupMenu(menu);
        tabla.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                tabla.getSelectionModel().setSelectionInterval(fila, fila);
            }
        });

        itemActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String carnet = (String) modelo.getValueAt(fila, 0);
                String codCurso = (String) modelo.getValueAt(fila, 2);
                matriculaSeleccionada = listaMatricula.buscar(carnet, codCurso);
                if (matriculaSeleccionada != null) {
                    comboCarnet.setSelectedItem(carnet);
                    comboCodigoCurso.setSelectedItem(codCurso);
                }
            }
        });

        itemEliminar.addActionListener(e -> eliminarMatricula());

        JToolBar barraExport = new JToolBar();
        barraExport.setFloatable(false);
        barraExport.setBackground(UIConstants.PANEL_HEADER);
        JButton btnExportar = UIConstants.crearBoton("Exportar a Excel");
        btnExportar.addActionListener(e -> exportarExcel());
        barraExport.add(Box.createHorizontalGlue());
        barraExport.add(btnExportar);
        barraExport.add(Box.createHorizontalStrut(10));

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(scroll, BorderLayout.CENTER);
        panelCentral.add(barraExport, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);
    }

    private void registrarMatricula() {
        String carnet = (String) comboCarnet.getSelectedItem();
        String codigo = (String) comboCodigoCurso.getSelectedItem();

        if (carnet == null || codigo == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante y un curso.");
            return;
        }

        Estudiante est = arbolEstudiantes.buscar(carnet);
        Curso curso = listaCursos.buscar(codigo);

        if (est == null || curso == null) {
            JOptionPane.showMessageDialog(this, "Estudiante o curso no encontrado.");
            return;
        }

        dao.MatriculaDAO matriculaDAO = new dao.MatriculaDAO();
        boolean exitoSQL = matriculaDAO.insertar(carnet, codigo);

        if (exitoSQL) {
            CuentasEstudiantes.registrarPorMatricula(carnet);
            JOptionPane.showMessageDialog(this, "Matrícula registrada con éxito en la base de datos.");
            cargarMatriculasDesdeSQL();
        } else {
            JOptionPane.showMessageDialog(this, "Error: no se pudo guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarMatricula() {
        if (matriculaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una matrícula con clic derecho.");
            return;
        }

        String nuevoCarnet = (String) comboCarnet.getSelectedItem();
        String nuevoCurso = (String) comboCodigoCurso.getSelectedItem();

        Estudiante nuevoEst = arbolEstudiantes.buscar(nuevoCarnet);
        Curso nuevoCur = listaCursos.buscar(nuevoCurso);

        if (nuevoEst == null || nuevoCur == null) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea actualizar esta matrícula?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String carnetViejo = matriculaSeleccionada.getEstudiante().getCarnet();
            String cursoViejo = matriculaSeleccionada.getCurso().getCodigo();

            dao.MatriculaDAO matriculaDAO = new dao.MatriculaDAO();
            matriculaDAO.eliminar(carnetViejo, cursoViejo);
            matriculaDAO.insertar(nuevoCarnet, nuevoCurso);

            cargarMatriculasDesdeSQL();
            matriculaSeleccionada = null;
        }
    }

    private void eliminarMatricula() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String carnet = (String) modelo.getValueAt(fila, 0);
            String codigo = (String) modelo.getValueAt(fila, 2);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Desea eliminar la matrícula de " + carnet + " en curso " + codigo + "?",
                    "Confirmación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dao.MatriculaDAO matriculaDAO = new dao.MatriculaDAO();
                matriculaDAO.eliminar(carnet, codigo);
                cargarMatriculasDesdeSQL();
            }
        }
    }

    private void deshacerUltimaAccion() {
        if (pilaHistorial.estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay acciones para deshacer.");
            return;
        }

        AccionMatricula ultima = pilaHistorial.desapilar();
        dao.MatriculaDAO matriculaDAO = new dao.MatriculaDAO();

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

    private void cargarCombos() {
        comboCarnet.removeAllItems();
        arbolEstudiantes.inorden(est -> comboCarnet.addItem(est.getCarnet()));

        comboCodigoCurso.removeAllItems();
        listaCursos.recorrer(curso -> comboCodigoCurso.addItem(curso.getCodigo()));
    }

    private void cargarMatriculasDesdeSQL() {
        dao.MatriculaDAO matriculaDAO = new dao.MatriculaDAO();
        java.util.List<String[]> matriculasSQL = matriculaDAO.listar();

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
        modelo.setRowCount(0);
        listaMatricula.recorrer(m -> modelo.addRow(new Object[]{
                m.getEstudiante().getCarnet(),
                m.getEstudiante().getNombre(),
                m.getCurso().getCodigo(),
                m.getCurso().getNombre()
        }));
    }

    private void exportarExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("matriculas.xlsx"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ExportadorExcel.exportarMatriculas(listaMatricula, fc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Matrículas exportadas a Excel exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
