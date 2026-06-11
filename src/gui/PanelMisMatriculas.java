package gui;

import estructuras.ArbolEstudiantes;
import estructuras.ListaMatricula;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import util.ExportadorExcel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
        btnActualizar.setForeground(Color.BLACK);
        btnActualizar.setBorder(BorderFactory.createRaisedBevelBorder());

        JButton btnExportar = new JButton("Descargar reporte");
        btnExportar.setBackground(new Color(0, 150, 50));
        btnExportar.setForeground(Color.BLACK);
        btnExportar.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel sur = new JPanel();
        sur.setOpaque(false);
        sur.add(btnActualizar);
        sur.add(Box.createHorizontalStrut(10));
        sur.add(btnExportar);
        add(sur, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> {
            cargarDatosDesdeSQL();
            cargarTabla();
        });
        btnExportar.addActionListener(e -> exportarReporte());
        cargarDatosDesdeSQL();
        cargarTabla();
    }

    private void cargarDatosDesdeSQL() {
        dao.EstudianteDAO estudianteDAO = new dao.EstudianteDAO();
        dao.MatriculaDAO matriculaDAO = new dao.MatriculaDAO();
        dao.CursoDAO cursoDAO = new dao.CursoDAO();
        List<String[]> estudiantesSQL = estudianteDAO.listar();
        List<String[]> cursosSQL = cursoDAO.listar();
        List<String[]> matriculasSQL = matriculaDAO.listar();

        arbolEstudiantes.limpiar();
        for (String[] datos : estudiantesSQL) {
            arbolEstudiantes.insertar(new Estudiante(datos[0], datos[1], datos[2]));
        }

        listaMatricula.limpiar();
        for (String[] datos : matriculasSQL) {
            String carnetMat = datos[0];
            String codigoCurso = datos[1];

            Estudiante est = arbolEstudiantes.buscar(carnetMat);
            Curso curso = null;
            for (String[] cd : cursosSQL) {
                if (cd[0].equals(codigoCurso)) {
                    curso = new Curso(cd[0], cd[1], Integer.parseInt(cd[2]), Integer.parseInt(cd[3]));
                    break;
                }
            }

            if (est != null && curso != null) {
                listaMatricula.agregar(new Matricula(est, curso));
            }
        }
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

    private void exportarReporte() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("reporte_matriculas.xls"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                List<Matricula> mis = listaMatricula.buscarPorCarnet(carnet);
                ListaMatricula filtrada = new ListaMatricula();
                for (Matricula m : mis) {
                    filtrada.agregar(m);
                }
                ExportadorExcel.exportarMatriculas(filtrada, fc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Reporte descargado exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al descargar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
