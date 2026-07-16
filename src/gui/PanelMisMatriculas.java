package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelMisMatriculas extends JPanel {

    private final DefaultTableModel modelo;
    private final JLabel lblEncabezado;
    private final JButton btnActualizar;
    private final JButton btnExportar;

    public PanelMisMatriculas() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Inicializamos el encabezado con un texto temporal o vacío
        lblEncabezado = new JLabel("", JLabel.CENTER);
        lblEncabezado.setFont(UIConstants.FONT_LABEL);
        lblEncabezado.setForeground(UIConstants.TEXT_BLUE);
        add(lblEncabezado, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"Código", "Curso", "Créditos", "Semestre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnActualizar = UIConstants.crearBoton("Actualizar lista");
        btnExportar = UIConstants.crearBoton("Descargar reporte");

        JPanel sur = new JPanel();
        sur.setOpaque(false);
        sur.add(btnActualizar);
        sur.add(Box.createHorizontalStrut(10));
        sur.add(btnExportar);
        add(sur, BorderLayout.SOUTH);
    }

    // --- MÉTODOS DE MANIPULACIÓN DE LA UI ---

    public void actualizarEncabezado(String subtitulo) {
        lblEncabezado.setText("<html><div style='text-align:center;'>"
                + "<b>Mis cursos matriculados</b><br/>"
                + subtitulo + "</div></html>");
    }

    public void limpiarTabla() {
        modelo.setRowCount(0);
    }

    public void agregarFila(Object[] fila) {
        modelo.addRow(fila);
    }

    // --- GETTERS DE COMPONENTES PARA EL CONTROLADOR ---

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnExportar() {
        return btnExportar;
    }
}