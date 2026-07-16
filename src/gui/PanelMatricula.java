package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelMatricula extends JPanel {

    private JComboBox<String> comboCarnet;
    private JComboBox<String> comboCodigoCurso;
    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnVer;
    private JButton btnDeshacer;
    private JButton btnExportar;

    private JMenuItem itemActualizar;
    private JMenuItem itemEliminar;

    public PanelMatricula() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BG);

        agregarFormulario();
        agregarTabla();
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

        btnRegistrar = UIConstants.crearBoton("Registrar");
        btnActualizar = UIConstants.crearBoton("Actualizar");
        btnVer = UIConstants.crearBoton("Ver Matrículas");
        btnDeshacer = UIConstants.crearBoton("Deshacer");

        panelForm.add(btnRegistrar);
        panelForm.add(btnActualizar);
        panelForm.add(btnVer);
        panelForm.add(btnDeshacer);

        add(panelForm, BorderLayout.WEST);
    }

    private void agregarTabla() {
        modelo = new DefaultTableModel(new Object[]{"Carnet", "Nombre", "Curso", "Nombre Curso"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Matrículas"));

        JPopupMenu menu = new JPopupMenu();
        itemActualizar = new JMenuItem("Actualizar");
        itemEliminar = new JMenuItem("Eliminar");

        menu.add(itemActualizar);
        menu.add(itemEliminar);

        tabla.setComponentPopupMenu(menu);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                if (fila >= 0) {
                    tabla.getSelectionModel().setSelectionInterval(fila, fila);
                }
            }
        });

        JToolBar barraExport = new JToolBar();
        barraExport.setFloatable(false);
        barraExport.setBackground(UIConstants.PANEL_HEADER);
        btnExportar = UIConstants.crearBoton("Exportar a Excel");
        
        barraExport.add(Box.createHorizontalGlue());
        barraExport.add(btnExportar);
        barraExport.add(Box.createHorizontalStrut(10));

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(scroll, BorderLayout.CENTER);
        panelCentral.add(barraExport, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE MANIPULACIÓN DE LA UI ---

    public void limpiarTabla() {
        modelo.setRowCount(0);
    }

    public void agregarFila(Object[] fila) {
        modelo.addRow(fila);
    }

    public void rellenarCombos(String[] carnets, String[] codigosCursos) {
        comboCarnet.removeAllItems();
        for (String carnet : carnets) {
            comboCarnet.addItem(carnet);
        }

        comboCodigoCurso.removeAllItems();
        for (String codigo : codigosCursos) {
            comboCodigoCurso.addItem(codigo);
        }
    }

    public void setSeleccionCombos(String carnet, String codigoCurso) {
        comboCarnet.setSelectedItem(carnet);
        comboCodigoCurso.setSelectedItem(codigoCurso);
    }

    public int getFilaSeleccionada() {
        return tabla.getSelectedRow();
    }

    public Object getValueAt(int fila, int columna) {
        return modelo.getValueAt(fila, columna);
    }

    public String getCarnetSeleccionado() {
        return (String) comboCarnet.getSelectedItem();
    }

    public String getCodigoCursoSeleccionado() {
        return (String) comboCodigoCurso.getSelectedItem();
    }

    // --- GETTERS DE COMPONENTES PARA EL CONTROLADOR ---

    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnVer() { return btnVer; }
    public JButton getBtnDeshacer() { return btnDeshacer; }
    public JButton getBtnExportar() { return btnExportar; }
    public JMenuItem getItemActualizar() { return itemActualizar; }
    public JMenuItem getItemEliminar() { return itemEliminar; }
    public JTable getTabla() { return tabla; }
}