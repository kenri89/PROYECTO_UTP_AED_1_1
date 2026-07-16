package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelEstudiantes extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtCarnet, txtNombre;
    private JComboBox<String> comboCarrera;
    
    private JButton btnRegistrar, btnActualizar, btnBuscar, btnExportar;
    private JMenuItem itemActualizar, itemEliminar;

    public PanelEstudiantes() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BG);

        agregarFormulario();
        agregarTabla();
    }

    private void agregarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Estudiante"));
        panelForm.setBackground(UIConstants.PANEL_FORM);

        txtCarnet = new JTextField();
        txtNombre = new JTextField();
        comboCarrera = new JComboBox<>(new String[]{"DESARROLLO DE SISTEMAS", "CONTABILIDAD"});

        panelForm.add(new JLabel("Carnet:"));
        panelForm.add(txtCarnet);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Carrera:"));
        panelForm.add(comboCarrera);

        btnRegistrar = UIConstants.crearBoton("Registrar");
        btnActualizar = UIConstants.crearBoton("Actualizar");
        btnBuscar = UIConstants.crearBoton("Buscar por carnet");

        panelForm.add(btnRegistrar);
        panelForm.add(btnActualizar);
        panelForm.add(btnBuscar);

        add(panelForm, BorderLayout.WEST);
    }

    private void agregarTabla() {
        modelo = new DefaultTableModel(new Object[]{"Carnet", "Nombre", "Carrera"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estudiantes"));

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
                if (fila >= 0 && fila < tabla.getRowCount()) {
                    tabla.getSelectionModel().setSelectionInterval(fila, fila);
                }
            }
        });

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        JToolBar barraExport = new JToolBar();
        barraExport.setFloatable(false);
        barraExport.setBackground(UIConstants.PANEL_BG);

        btnExportar = UIConstants.crearBoton("Exportar a Excel");
        barraExport.add(Box.createHorizontalGlue());
        barraExport.add(btnExportar);
        barraExport.add(Box.createHorizontalStrut(10));

        panelTabla.add(barraExport, BorderLayout.SOUTH);
        add(panelTabla, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE MANIPULACIÓN DE LA UI ---

    public void limpiarCampos() {
        txtCarnet.setText("");
        txtNombre.setText("");
        comboCarrera.setSelectedIndex(0);
        txtCarnet.setEnabled(true);
    }

    public void rellenarCampos(String carnet, String nombre, String carrera) {
        txtCarnet.setText(carnet);
        txtNombre.setText(nombre);
        comboCarrera.setSelectedItem(carrera);
        txtCarnet.setEnabled(false); // Evitar modificar la clave primaria al actualizar
    }

    public void limpiarTabla() {
        modelo.setRowCount(0);
    }

    public void agregarFilaTabla(Object[] fila) {
        modelo.addRow(fila);
    }

    // --- GETTERS ---

    public String getCarnetInput() { return txtCarnet.getText().trim(); }
    public String getNombreInput() { return txtNombre.getText().trim(); }
    public String getCarreraSeleccionada() { return (String) comboCarrera.getSelectedItem(); }

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModelo() { return modelo; }
    
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnExportar() { return btnExportar; }
    
    public JMenuItem getItemActualizar() { return itemActualizar; }
    public JMenuItem getItemEliminar() { return itemEliminar; }
}