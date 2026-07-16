package gui;

import modelo.Solicitud;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Queue;

public class PanelAdministradorSolicitudes extends JPanel {

    private DefaultTableModel modeloPendientes;
    private DefaultTableModel modeloAtendidas;
    private JButton btnAtender;

    public PanelAdministradorSolicitudes() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BG);

        JLabel lblTitulo = new JLabel("Gestión de Solicitudes", JLabel.CENTER);
        lblTitulo.setFont(UIConstants.FONT_TITLE);
        add(lblTitulo, BorderLayout.NORTH);

        // --- Tabla de solicitudes pendientes ---
        modeloPendientes = new DefaultTableModel(new String[]{
                "Carnet", "Tipo", "Descripción", "Fecha de Envío"
        }, 0);
        JTable tablaPendientes = new JTable(modeloPendientes);

        JPanel panelPendientes = new JPanel(new BorderLayout());
        panelPendientes.setBorder(BorderFactory.createTitledBorder("Solicitudes Pendientes"));
        panelPendientes.add(new JScrollPane(tablaPendientes), BorderLayout.CENTER);

        btnAtender = UIConstants.crearBoton("Atender Siguiente");
        panelPendientes.add(btnAtender, BorderLayout.SOUTH);

        // --- Tabla de solicitudes atendidas ---
        modeloAtendidas = new DefaultTableModel(new String[]{
                "Carnet", "Tipo", "Descripción", "Fecha de Envío", "Fecha de Atención"
        }, 0);
        JTable tablaAtendidas = new JTable(modeloAtendidas);

        JPanel panelAtendidas = new JPanel(new BorderLayout());
        panelAtendidas.setBorder(BorderFactory.createTitledBorder("Solicitudes Atendidas"));
        panelAtendidas.add(new JScrollPane(tablaAtendidas), BorderLayout.CENTER);

        // --- Panel central con ambas tablas ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelPendientes, panelAtendidas);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    // --- MÉTODOS PARA ACTUALIZAR LA INTERFAZ ---

    public void limpiarTablas() {
        modeloPendientes.setRowCount(0);
        modeloAtendidas.setRowCount(0);
    }

    public void agregarFilaPendiente(Object[] fila) {
        modeloPendientes.addRow(fila);
    }

    public void agregarFilaAtendida(Object[] fila) {
        modeloAtendidas.addRow(fila);
    }

    // Getter para que el controlador escuche el botón
    public JButton getBtnAtender() {
        return btnAtender;
    }
}