package gui;

import com.google.common.base.Strings;
import estructuras.ArregloCursos;
import estructuras.MatrizSemestres;
import estructuras.ListaCursos;
import modelo.Curso;
import util.ExportadorExcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class PanelCursos extends JPanel {

    private ArregloCursos arregloCursos;
    private MatrizSemestres matrizSemestres;
    private ListaCursos listaCursos;

    private JTable tablaCursos;
    private DefaultTableModel modeloTabla;

    private JTextField txtCodigo, txtNombre, txtCreditos;
    private JComboBox<String> comboSemestre;

    public PanelCursos(ArregloCursos arregloCursos, MatrizSemestres matrizSemestres, ListaCursos listaCursos) {
        this.arregloCursos = arregloCursos;
        this.matrizSemestres = matrizSemestres;
        this.listaCursos = listaCursos;

        setLayout(new BorderLayout());
        setBackground(new Color(230, 240, 255));

        agregarFormulario();
        agregarTabla();
        try {
            cargarDatosDesdeSQL();
        } catch (Exception e) {
            System.err.println("Error al cargar datos desde BD (usando modo local): " + e.getMessage());
        }
    }
    private void agregarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Curso"));
        panelForm.setBackground(new Color(220, 230, 255));

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtCreditos = new JTextField();
        comboSemestre = new JComboBox<>();

        for (int i = 1; i <= 10; i++) {
            comboSemestre.addItem("Semestre " + i);
        }

        panelForm.add(new JLabel("Código:"));
        panelForm.add(txtCodigo);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Créditos:"));
        panelForm.add(txtCreditos);
        panelForm.add(new JLabel("Semestre:"));
        panelForm.add(comboSemestre);

        JButton btnAgregar = new JButton("Agregar Curso");
        JButton btnEliminar = new JButton("Eliminar por Código");
        JButton btnActualizar = new JButton("Actualizar Curso");

        btnAgregar.setBackground(new Color(30, 120, 200));
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setBorder(BorderFactory.createRaisedBevelBorder());

        btnEliminar.setBackground(new Color(200, 60, 60));
        btnEliminar.setForeground(Color.BLACK);
        btnEliminar.setBorder(BorderFactory.createRaisedBevelBorder());

        btnActualizar.setBackground(new Color(255, 165, 0));
        btnActualizar.setForeground(Color.BLACK);
        btnActualizar.setBorder(BorderFactory.createRaisedBevelBorder());

        panelForm.add(btnAgregar);
        panelForm.add(btnEliminar);
        panelForm.add(btnActualizar);

        add(panelForm, BorderLayout.WEST);

        btnAgregar.addActionListener(e -> insertarCurso());
        btnEliminar.addActionListener(e -> eliminarCurso());
        btnActualizar.addActionListener(e -> confirmarYActualizarCurso());
    }

    private void agregarTabla() {
        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Nombre", "Créditos", "Semestre"}, 0);
        tablaCursos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cursos Registrados"));

        tablaCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) mostrarMenuContextual(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) mostrarMenuContextual(e);
            }
        });

        JToolBar barraExport = new JToolBar();
        barraExport.setFloatable(false);
        barraExport.setBackground(new Color(220, 235, 255));
        JButton btnExportar = new JButton("Exportar a Excel");
        btnExportar.setBackground(new Color(0, 150, 50));
        btnExportar.setForeground(Color.BLACK);
        btnExportar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnExportar.setFont(new Font("Arial", Font.BOLD, 14));
        btnExportar.addActionListener(e -> exportarExcel());
        barraExport.add(Box.createHorizontalGlue());
        barraExport.add(btnExportar);
        barraExport.add(Box.createHorizontalStrut(10));

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        panelCentral.add(barraExport, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);
    }

    private void mostrarMenuContextual(MouseEvent e) {
        int fila = tablaCursos.rowAtPoint(e.getPoint());
        if (fila >= 0 && fila < tablaCursos.getRowCount()) {
            tablaCursos.setRowSelectionInterval(fila, fila);

            JPopupMenu menu = new JPopupMenu();
            JMenuItem itemActualizar = new JMenuItem("Actualizar");

            itemActualizar.addActionListener(ev -> cargarCursoSeleccionado());

            menu.add(itemActualizar);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void cargarCursoSeleccionado() {
        int fila = tablaCursos.getSelectedRow();
        if (fila == -1) return;

        txtCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtCreditos.setText(modeloTabla.getValueAt(fila, 2).toString());
        comboSemestre.setSelectedIndex(Integer.parseInt(modeloTabla.getValueAt(fila, 3).toString()) - 1);

        txtCodigo.setEnabled(false);
    }

    private void insertarCurso() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String creditosStr = txtCreditos.getText().trim();
        int semestre = comboSemestre.getSelectedIndex() + 1;

        if (codigo.isEmpty() || nombre.isEmpty() || creditosStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        int creditos;
        try {
            creditos = Integer.parseInt(creditosStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los créditos deben ser un número.");
            return;
        }

        dao.CursoDAO cursoDAO = new dao.CursoDAO();
        boolean exitoSQL = cursoDAO.insertar(codigo, nombre, creditos, semestre);

        if (exitoSQL) {
            JOptionPane.showMessageDialog(this, "Curso registrado con éxito en la base de datos.");
            cargarDatosDesdeSQL();
        } else {
            JOptionPane.showMessageDialog(this, "Error: no se pudo guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarCampos();
    }

    private void eliminarCurso() {
        int fila = tablaCursos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un curso para eliminar.");
            return;
        }

        String codigo = modeloTabla.getValueAt(fila, 0).toString();
        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar el curso con código " + codigo + "?",
                "Confirmación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        new dao.CursoDAO().eliminar(codigo);
        JOptionPane.showMessageDialog(this, "Curso eliminado correctamente.");
        cargarDatosDesdeSQL();
    }

    private void confirmarYActualizarCurso() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String creditosStr = txtCreditos.getText().trim();
        int semestre = comboSemestre.getSelectedIndex() + 1;

        if (codigo.isEmpty() || nombre.isEmpty() || creditosStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos para actualizar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Deseas actualizar este curso?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        try {
            int creditos = Integer.parseInt(creditosStr);

            dao.CursoDAO cursoDAO = new dao.CursoDAO();
            cursoDAO.actualizar(codigo, nombre, creditos, semestre);

            JOptionPane.showMessageDialog(this, "Curso actualizado correctamente.");
            cargarDatosDesdeSQL();
            limpiarCampos();
            txtCodigo.setEnabled(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Créditos inválidos.");
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Curso curso : arregloCursos.obtenerCursos()) {
            modeloTabla.addRow(new Object[]{
                    curso.getCodigo(), curso.getNombre(),
                    curso.getCreditos(), curso.getSemestre()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCreditos.setText("");
        comboSemestre.setSelectedIndex(0);
        txtCodigo.setEnabled(true);
    }
    private void cargarDatosDesdeSQL() {
        dao.CursoDAO cursoDAO = new dao.CursoDAO();
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

        cargarTabla();
    }

    private void exportarExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("cursos.xls"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            try {
                ExportadorExcel.exportarCursos(arregloCursos, archivo);
                JOptionPane.showMessageDialog(this, "Cursos exportados a Excel exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
