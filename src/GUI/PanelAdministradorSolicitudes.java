package gui;

import modelo.Solicitud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class PanelAdministradorSolicitudes extends JPanel {

    private Queue<Solicitud> colaSolicitudes;
    private java.util.List<Solicitud> solicitudesAtendidas;
    private DefaultTableModel modeloPendientes;
    private DefaultTableModel modeloAtendidas;

    public PanelAdministradorSolicitudes(Queue<Solicitud> colaSolicitudes) {
        this.colaSolicitudes = colaSolicitudes;
        this.solicitudesAtendidas = new LinkedList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(240, 250, 255));

        JLabel lblTitulo = new JLabel("Gestión de Solicitudes", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        // --- Tabla de solicitudes pendientes ---
        modeloPendientes = new DefaultTableModel(new String[]{
                "Carnet", "Tipo", "Descripción", "Fecha de Envío"
        }, 0);
        JTable tablaPendientes = new JTable(modeloPendientes);

        JPanel panelPendientes = new JPanel(new BorderLayout());
        panelPendientes.setBorder(BorderFactory.createTitledBorder("Solicitudes Pendientes"));
        panelPendientes.add(new JScrollPane(tablaPendientes), BorderLayout.CENTER);

        JButton btnAtender = new JButton("Atender Siguiente");
        btnAtender.setBackground(new Color(0, 150, 100));
        btnAtender.setForeground(Color.WHITE);
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

        // Botón de atención
        btnAtender.addActionListener(e -> atenderSiguiente());

        // Mostrar datos al cargar
        actualizarTablas();
    }

    private void atenderSiguiente() {
        if (colaSolicitudes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay solicitudes pendientes.");
            return;
        }

        Solicitud s = colaSolicitudes.poll(); // Extraer solicitud
        s.setAtendida(true);
        s.setFechaAtencion(LocalDateTime.now());
        solicitudesAtendidas.add(s);

        actualizarTablas();
    }

    private void actualizarTablas() {
        modeloPendientes.setRowCount(0);
        for (Solicitud s : colaSolicitudes) {
            modeloPendientes.addRow(new Object[]{
                    s.getCarnet(),
                    s.getTipo(),
                    s.getDescripcion(),
                    s.getFechaFormateada()
            });
        }

        modeloAtendidas.setRowCount(0);
        for (Solicitud s : solicitudesAtendidas) {
            modeloAtendidas.addRow(new Object[]{
                    s.getCarnet(),
                    s.getTipo(),
                    s.getDescripcion(),
                    s.getFechaFormateada(),
                    s.getFechaAtencionFormateada()
            });
        }
    }
}
