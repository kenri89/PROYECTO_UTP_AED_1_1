package gui;

import estructuras.ArbolEstudiantes;
import estructuras.ListaMatricula;
import modelo.Estudiante;
import modelo.Matricula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de solo lectura: cursos en los que está matriculado el estudiante actual.
 */
public class PanelMisMatriculas extends JPanel {

    private final ListaMatricula listaMatricula;
    private final String carnet;
    private final ArbolEstudiantes arbolEstudiantes;
    private final DefaultTableModel modelo;

    public PanelMisMatriculas(ListaMatricula listaMatricula, String carnet, ArbolEstudiantes arbolEstudiantes) {
        this.listaMatricula = listaMatricula;
        this.carnet = carnet == null ? "" : carnet.trim();
        this.arbolEstudiantes = arbolEstudiantes;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 240, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String tituloCarnet = this.carnet.isEmpty() ? "(sin carnet asignado)" : this.carnet;
        Estudiante est = this.carnet.isEmpty() ? null : arbolEstudiantes.buscar(this.carnet);
        String nombre = est != null ? est.getNombre() : null;
        String subtitulo = nombre != null
                ? "Carnet: " + tituloCarnet + " — " + nombre
                : "Carnet: " + tituloCarnet + (this.carnet.isEmpty() ? "" : " (registra este estudiante en Gestión de Estudiantes si no aparece el nombre)");

        JLabel lblEncabezado = new JLabel("<html><div style='text-align:center;'>"
                + "<b>Mis cursos matriculados</b><br/>"
                + subtitulo + "</div></html>", JLabel.CENTER);
        lblEncabezado.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEncabezado.setForeground(new Color(0, 70, 140));
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

        JButton btnActualizar = new JButton("Actualizar lista");
        btnActualizar.setBackground(new Color(0, 120, 215));
        btnActualizar.setForeground(Color.WHITE);
        JPanel sur = new JPanel();
        sur.setOpaque(false);
        sur.add(btnActualizar);
        add(sur, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cargarTabla());
        cargarTabla();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        if (carnet.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tu usuario no tiene carnet vinculado. Contacta a la secretaría o al administrador.",
                    "Mis matrículas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        List<Matricula> mis = listaMatricula.buscarPorCarnet(carnet);
        if (mis.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay matrículas registradas para el carnet " + carnet + ".\n"
                            + "La secretaría debe registrarte y matricularte en \"Matrícula\".",
                    "Mis matrículas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (Matricula m : mis) {
            modelo.addRow(new Object[]{
                    m.getCurso().getCodigo(),
                    m.getCurso().getNombre(),
                    m.getCurso().getCreditos(),
                    m.getCurso().getSemestre()
            });
        }
    }
}
