package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHistorial extends JPanel {

    private JComboBox<String> comboCarnets;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnFiltrar;

    public PanelHistorial(String[] carnets) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BG);

        agregarFiltroCarnets(carnets); // Filtro por estudiante
        agregarTablaHistorial();       // Tabla con historial
    }

    private void agregarFiltroCarnets(String[] carnets) {
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtrar por Estudiante"));
        panelFiltro.setBackground(UIConstants.PANEL_BG);

        comboCarnets = new JComboBox<>();
        comboCarnets.addItem("Todos"); // Filtro global

        if (carnets != null) {
            for (String c : carnets) {
                comboCarnets.addItem(c);
            }
        }

        btnFiltrar = UIConstants.crearBoton("Filtrar");

        panelFiltro.add(new JLabel("Carnet:"));
        panelFiltro.add(comboCarnets);
        panelFiltro.add(btnFiltrar);

        add(panelFiltro, BorderLayout.NORTH);
    }

    private void agregarTablaHistorial() {
        modelo = new DefaultTableModel(new Object[]{"Carnet", "Nombre", "Curso", "Nombre Curso", "Acción"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Historial de Matrículas"));

        add(scroll, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE MANIPULACIÓN DE LA UI ---

    public void limpiarTabla() {
        modelo.setRowCount(0);
    }

    public void agregarFilaTabla(Object[] fila) {
        modelo.addRow(fila);
    }

    public String getFiltroSeleccionado() {
        return (String) comboCarnets.getSelectedItem();
    }

    // --- GETTERS ---

    public JButton getBtnFiltrar() {
        return btnFiltrar;
    }

    public JTable getTabla() {
        return tabla;
    }
}