package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelCursos extends JPanel {

    private DefaultTableModel modeloTabla;
    private JTable tablaCursos;
    private JTextField txtCodigo, txtNombre, txtCreditos;
    private JComboBox<String> comboSemestre;
    private JButton btnAgregar, btnEliminar, btnActualizar, btnExportar;
    private JMenuItem itemActualizar;

    public PanelCursos() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BG);

        agregarFormulario();
        agregarTabla();
    }

    private void agregarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Curso"));
        panelForm.setBackground(UIConstants.PANEL_FORM);

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

        btnAgregar = UIConstants.crearBoton("Agregar Curso");
        btnEliminar = UIConstants.crearBoton("Eliminar por Código");
        btnActualizar = UIConstants.crearBoton("Actualizar Curso");

        panelForm.add(btnAgregar);
        panelForm.add(btnEliminar);
        panelForm.add(btnActualizar);

        add(panelForm, BorderLayout.WEST);
    }

    private void agregarTabla() {
        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Nombre", "Créditos", "Semestre"}, 0);
        tablaCursos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cursos Registrados"));

        // Crear menú contextual pero delegar su comportamiento al controlador
        JPopupMenu menuContextual = new JPopupMenu();
        itemActualizar = new JMenuItem("Actualizar");
        menuContextual.add(itemActualizar);

        tablaCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) mostrarMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) mostrarMenu(e);
            }

            private void mostrarMenu(MouseEvent e) {
                int fila = tablaCursos.rowAtPoint(e.getPoint());
                if (fila >= 0 && fila < tablaCursos.getRowCount()) {
                    tablaCursos.setRowSelectionInterval(fila, fila);
                    menuContextual.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        JToolBar barraExport = new JToolBar();
        barraExport.setFloatable(false);
        barraExport.setBackground(UIConstants.PANEL_BG);
        btnExportar = UIConstants.crearBoton("Exportar a Excel");
        barraExport.add(Box.createHorizontalGlue());
        barraExport.add(btnExportar);
        barraExport.add(Box.createHorizontalStrut(10));

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        panelCentral.add(barraExport, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE ACCESO Y MANIPULACIÓN DE LA UI ---

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCreditos.setText("");
        comboSemestre.setSelectedIndex(0);
        txtCodigo.setEnabled(true);
    }

    public void rellenarCampos(String codigo, String nombre, String creditos, int semestre) {
        txtCodigo.setText(codigo);
        txtNombre.setText(nombre);
        txtCreditos.setText(creditos);
        comboSemestre.setSelectedIndex(semestre - 1);
        txtCodigo.setEnabled(false);
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void agregarFilaTabla(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    // --- GETTERS ---

    public String getCodigoInput() { return txtCodigo.getText().trim(); }
    public String getNombreInput() { return txtNombre.getText().trim(); }
    public String getCreditosInput() { return txtCreditos.getText().trim(); }
    public int getSemestreSeleccionado() { return comboSemestre.getSelectedIndex() + 1; }

    public JTable getTablaCursos() { return tablaCursos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnExportar() { return btnExportar; }
    public JMenuItem getItemActualizar() { return itemActualizar; }
}