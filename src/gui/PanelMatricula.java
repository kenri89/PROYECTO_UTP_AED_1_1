// ================================
// CLASE: PanelMatricula
// Tema: Unidad 2 - Lista enlazada y validación
//       Unidad 3 - Árbol binario de búsqueda
//       Unidad 4 - GUI con Swing y eventos
// Uso: Registro, actualización y eliminación de matrículas.
// Registra las acciones realizadas en una pila para historial.
// ================================

package gui;


import estructuras.ArbolEstudiantes;
import estructuras.ListaCursos;
import estructuras.ListaMatricula;
import estructuras.PilaAcciones_U3;
import estructuras.AccionMatricula;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import util.CuentasEstudiantes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class PanelMatricula extends JPanel {

    private ListaCursos listaCursos;
    private ArbolEstudiantes arbolEstudiantes;
    private ListaMatricula listaMatricula;
    private PilaAcciones_U3 pilaHistorial;

    private JComboBox<String> comboCarnet;
    private JComboBox<String> comboCodigoCurso;
    private JTable tabla;
    private DefaultTableModel modelo;

    private Matricula matriculaSeleccionada = null;

    public PanelMatricula(ListaCursos listaCursos, ArbolEstudiantes arbolEstudiantes, ListaMatricula listaMatricula, PilaAcciones_U3 pilaHistorial) {
        this.listaCursos = listaCursos;
        this.arbolEstudiantes = arbolEstudiantes;
        this.listaMatricula = listaMatricula;
        this.pilaHistorial = pilaHistorial;

        setLayout(new BorderLayout());
        setBackground(new Color(230, 240, 255));

        agregarFormulario();
        agregarTabla();
        cargarCombos();
    }

    private void agregarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de Matrícula"));
        panelForm.setBackground(new Color(210, 230, 255));

        comboCarnet = new JComboBox<>();
        comboCodigoCurso = new JComboBox<>();

        panelForm.add(new JLabel("Carnet Estudiante:"));
        panelForm.add(comboCarnet);
        panelForm.add(new JLabel("Código Curso:"));
        panelForm.add(comboCodigoCurso);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnVer = new JButton("Ver Matrículas");
        JButton btnDeshacer = new JButton("Deshacer");

        btnRegistrar.setBackground(new Color(0, 140, 200));
        btnRegistrar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(0, 170, 100));
        btnActualizar.setForeground(Color.WHITE);
        btnVer.setBackground(new Color(120, 100, 200));
        btnVer.setForeground(Color.WHITE);
        btnDeshacer.setBackground(new Color(200, 80, 80));
        btnDeshacer.setForeground(Color.WHITE);

        panelForm.add(btnRegistrar);
        panelForm.add(btnActualizar);
        panelForm.add(btnVer);
        panelForm.add(btnDeshacer);

        add(panelForm, BorderLayout.WEST);

        // Acciones
        btnRegistrar.addActionListener(e -> registrarMatricula());
        btnActualizar.addActionListener(e -> actualizarMatricula());
        btnVer.addActionListener(e -> cargarTabla());
        btnDeshacer.addActionListener(e -> deshacerUltimaAccion());
    }

    private void agregarTabla() {
        modelo = new DefaultTableModel(new Object[]{"Carnet", "Nombre", "Curso", "Nombre Curso"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Matrículas"));

        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemActualizar = new JMenuItem("Actualizar");
        JMenuItem itemEliminar = new JMenuItem("Eliminar");

        menu.add(itemActualizar);
        menu.add(itemEliminar);

        tabla.setComponentPopupMenu(menu);
        tabla.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                tabla.getSelectionModel().setSelectionInterval(fila, fila);
            }
        });

        itemActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String carnet = (String) modelo.getValueAt(fila, 0);
                String codCurso = (String) modelo.getValueAt(fila, 2);
                matriculaSeleccionada = listaMatricula.buscar(carnet, codCurso);
                if (matriculaSeleccionada != null) {
                    comboCarnet.setSelectedItem(carnet);
                    comboCodigoCurso.setSelectedItem(codCurso);
                }
            }
        });

        itemEliminar.addActionListener(e -> eliminarMatricula());

        add(scroll, BorderLayout.CENTER);
    }

    private void registrarMatricula() {
        String carnet = (String) comboCarnet.getSelectedItem();
        String codigo = (String) comboCodigoCurso.getSelectedItem();

        if (carnet == null || codigo == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estudiante y un curso.");
            return;
        }

        Estudiante est = arbolEstudiantes.buscar(carnet);
        Curso curso = listaCursos.buscar(codigo);

        if (est == null || curso == null) {
            JOptionPane.showMessageDialog(this, "Estudiante o curso no encontrado.");
            return;
        }

        Matricula nueva = new Matricula(est, curso);
        listaMatricula.agregar(nueva);
        CuentasEstudiantes.registrarPorMatricula(carnet);
        pilaHistorial.apilar(new AccionMatricula("REGISTRO", nueva));
        cargarTabla();
    }

    private void actualizarMatricula() {
        if (matriculaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una matrícula con clic derecho.");
            return;
        }

        String nuevoCarnet = (String) comboCarnet.getSelectedItem();
        String nuevoCurso = (String) comboCodigoCurso.getSelectedItem();

        Estudiante nuevoEst = arbolEstudiantes.buscar(nuevoCarnet);
        Curso nuevoCur = listaCursos.buscar(nuevoCurso);

        if (nuevoEst == null || nuevoCur == null) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea actualizar esta matrícula?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Matricula copiaAnterior = new Matricula(matriculaSeleccionada.getEstudiante(), matriculaSeleccionada.getCurso());
            pilaHistorial.apilar(new AccionMatricula("ACTUALIZACIÓN", copiaAnterior));

            matriculaSeleccionada.setEstudiante(nuevoEst);
            matriculaSeleccionada.setCurso(nuevoCur);
            cargarTabla();
            matriculaSeleccionada = null;
        }
    }

    private void eliminarMatricula() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String carnet = (String) modelo.getValueAt(fila, 0);
            String codigo = (String) modelo.getValueAt(fila, 2);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Desea eliminar la matrícula de " + carnet + " en curso " + codigo + "?",
                    "Confirmación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Matricula eliminada = listaMatricula.buscar(carnet, codigo);
                if (eliminada != null) {
                    pilaHistorial.apilar(new AccionMatricula("ELIMINACIÓN", eliminada));
                }

                listaMatricula.eliminar(carnet, codigo);
                cargarTabla();
            }
        }
    }

    private void deshacerUltimaAccion() {
        if (pilaHistorial.estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay acciones para deshacer.");
            return;
        }

        AccionMatricula ultima = pilaHistorial.desapilar();

        switch (ultima.getTipo()) {
            case "REGISTRO":
                Matricula reg = ultima.getMatricula();
                listaMatricula.eliminar(reg.getEstudiante().getCarnet(), reg.getCurso().getCodigo());
                break;
            case "ELIMINACIÓN":
                Matricula elim = ultima.getMatricula();
                listaMatricula.agregar(elim);
                break;
            case "ACTUALIZACIÓN":
                Matricula anterior = ultima.getMatricula();
                Matricula actual = listaMatricula.buscar(anterior.getEstudiante().getCarnet(), anterior.getCurso().getCodigo());
                if (actual != null) {
                    actual.setEstudiante(anterior.getEstudiante());
                    actual.setCurso(anterior.getCurso());
                }
                break;
        }

        cargarTabla();
    }

    private void cargarCombos() {
        comboCarnet.removeAllItems();
        arbolEstudiantes.inorden(est -> comboCarnet.addItem(est.getCarnet()));

        comboCodigoCurso.removeAllItems();
        listaCursos.recorrer(curso -> comboCodigoCurso.addItem(curso.getCodigo()));
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        listaMatricula.recorrer(m -> modelo.addRow(new Object[]{
                m.getEstudiante().getCarnet(),
                m.getEstudiante().getNombre(),
                m.getCurso().getCodigo(),
                m.getCurso().getNombre()
        }));
    }
}
