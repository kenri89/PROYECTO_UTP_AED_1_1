package gui;

import estructuras.ArbolEstudiantes;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class PanelArbolRecorridos_U4 extends JPanel {

    private DefaultTableModel modelo;
    private JTable tabla;

    public PanelArbolRecorridos_U4(ArbolEstudiantes arbol) {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 245, 255));

        JLabel lblTitulo = new JLabel("Recorridos del Árbol de Estudiantes", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnInorden = new JButton("Inorden");
        JButton btnPreorden = new JButton("Preorden");
        JButton btnPostorden = new JButton("Postorden");
        panelBotones.add(btnInorden);
        panelBotones.add(btnPreorden);
        panelBotones.add(btnPostorden);

        add(panelBotones, BorderLayout.SOUTH);

        modelo = new DefaultTableModel(new Object[]{"Carnet", "Nombre", "Carrera"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        btnInorden.addActionListener(e -> cargarTabla(arbol, "inorden"));
        btnPreorden.addActionListener(e -> cargarTabla(arbol, "preorden"));
        btnPostorden.addActionListener(e -> cargarTabla(arbol, "postorden"));
    }

    private void cargarTabla(ArbolEstudiantes arbol, String tipo) {
        modelo.setRowCount(0);
        List<Estudiante> lista = new LinkedList<>();

        switch (tipo) {
            case "inorden":
                arbol.inorden(lista::add);
                break;
            case "preorden":
                arbol.preorden(lista::add);
                break;
            case "postorden":
                arbol.postorden(lista::add);
                break;
        }

        for (Estudiante est : lista) {
            modelo.addRow(new Object[]{
                    est.getCarnet(), est.getNombre(), est.getCarrera()
            });
        }
    }
}
