// ================================
// CLASE: PanelHistorial
// Tema: Unidad 4 - GUI con Swing y JTable
//       Unidad 3 - Pila (estructura de control de acciones)
// Uso: Muestra el historial de acciones sobre matrículas (registro, modificación, eliminación).
// Permite filtrar por estudiante usando su carnet.
// ================================

package gui;

import estructuras.PilaAcciones_U3;
import estructuras.AccionMatricula;
import modelo.Matricula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelHistorial extends JPanel {

    private JComboBox<String> comboCarnets;
    private JTable tabla;
    private DefaultTableModel modelo;

    private PilaAcciones_U3 pilaAcciones;
    private String[] carnets; // Reemplazo de List<String>

    public PanelHistorial(PilaAcciones_U3 pilaAcciones, String[] carnets) {
        this.pilaAcciones = pilaAcciones;
        this.carnets = carnets;

        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BG);

        agregarFiltroCarnets();   // Filtro por estudiante
        agregarTablaHistorial(); // Tabla con historial
    }

    private void agregarFiltroCarnets() {
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtrar por Estudiante"));
        panelFiltro.setBackground(UIConstants.PANEL_BG);

        comboCarnets = new JComboBox<>();
        comboCarnets.addItem("Todos"); // Filtro global

        for (String c : carnets) {
            comboCarnets.addItem(c);
        }

        JButton btnFiltrar = UIConstants.crearBoton("Filtrar");
        btnFiltrar.addActionListener(e -> cargarHistorial());

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
        cargarHistorial();
    }

    private void cargarHistorial() {
        modelo.setRowCount(0); // Limpiar tabla
        String filtro = (String) comboCarnets.getSelectedItem();

        PilaAcciones_U3 copia = pilaAcciones.copiar(); // Copiar la pila para no modificarla
        while (!copia.estaVacia()) {
            AccionMatricula accion = copia.desapilar();
            Matricula mat = accion.getMatricula();

            if (filtro.equals("Todos") || mat.getEstudiante().getCarnet().equals(filtro)) {
                modelo.addRow(new Object[]{
                        mat.getEstudiante().getCarnet(),
                        mat.getEstudiante().getNombre(),
                        mat.getCurso().getCodigo(),
                        mat.getCurso().getNombre(),
                        accion.getTipo()
                });
            }
        }
    }
}
