package gui;

import estructuras.ArbolEstudiantes;
import modelo.Estudiante;
import dao.EstudianteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI para gestión de estudiantes.
 * Tema: Unidad 3 – Árbol Binario de Búsqueda (ABB) Sincronizado con SQL Server
 */
public class PanelEstudiantes extends JPanel {

    private ArbolEstudiantes arbol;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtCarnet, txtNombre;
    private JComboBox<String> comboCarrera;
    private String carnetSeleccionado = null;

    public PanelEstudiantes(ArbolEstudiantes arbol) {
        this.arbol = arbol;
        setLayout(new BorderLayout());
        setBackground(new Color(230, 245, 255));

        agregarFormulario();
        agregarTabla();
        cargarDatosDesdeSQL();
    }

    private void agregarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Estudiante"));
        panelForm.setBackground(new Color(210, 235, 255));

        txtCarnet = new JTextField();
        txtNombre = new JTextField();
        comboCarrera = new JComboBox<>(new String[]{"DESARROLLO DE SISTEMAS", "CONTABILIDAD"});

        panelForm.add(new JLabel("Carnet:"));
        panelForm.add(txtCarnet);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Carrera:"));
        panelForm.add(comboCarrera);

        JButton btnAgregar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnBuscar = new JButton("Buscar por carnet");

        btnAgregar.setBackground(new Color(30, 120, 200));
        btnAgregar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(60, 150, 80));
        btnActualizar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(120, 120, 200));
        btnBuscar.setForeground(Color.WHITE);

        panelForm.add(btnAgregar);
        panelForm.add(btnActualizar);
        panelForm.add(btnBuscar);

        add(panelForm, BorderLayout.WEST);

        btnAgregar.addActionListener(e -> registrarEstudiante());
        btnActualizar.addActionListener(e -> actualizarEstudiante());
        btnBuscar.addActionListener(e -> buscarEstudiante());
    }

    private void agregarTabla() {
        modelo = new DefaultTableModel(new Object[]{"Carnet", "Nombre", "Carrera"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Estudiantes"));

        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemActualizar = new JMenuItem("Actualizar");
        JMenuItem itemEliminar = new JMenuItem("Eliminar");

        menu.add(itemActualizar);
        menu.add(itemEliminar);

        tabla.setComponentPopupMenu(menu);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                if (fila >= 0) {
                    tabla.getSelectionModel().setSelectionInterval(fila, fila);
                }
            }
        });

        itemActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                carnetSeleccionado = (String) modelo.getValueAt(fila, 0);
                txtCarnet.setText((String) modelo.getValueAt(fila, 0));
                txtCarnet.setEditable(false);
                txtNombre.setText((String) modelo.getValueAt(fila, 1));
                comboCarrera.setSelectedItem((String) modelo.getValueAt(fila, 2));
            }
        });

        itemEliminar.addActionListener(e -> eliminarEstudiante());

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        JPanel panelRecorridos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelRecorridos.setBackground(new Color(230, 245, 255));
        
        JButton btnInorden = new JButton("Inorden");
        JButton btnPreorden = new JButton("Preorden");
        JButton btnPostorden = new JButton("Postorden");

        JButton[] botones = {btnInorden, btnPreorden, btnPostorden};
        for (JButton b : botones) {
            b.setBackground(new Color(70, 130, 180));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setFont(new Font("Arial", Font.BOLD, 12));
            panelRecorridos.add(b);
        }

        btnInorden.addActionListener(e -> cargarRecorrido("inorden"));
        btnPreorden.addActionListener(e -> cargarRecorrido("preorden"));
        btnPostorden.addActionListener(e -> cargarRecorrido("postorden"));

        panelTabla.add(panelRecorridos, BorderLayout.SOUTH);
        add(panelTabla, BorderLayout.CENTER);
    }

    private void registrarEstudiante() {
        String carnet = txtCarnet.getText().trim();
        String nombre = txtNombre.getText().trim();
        String carrera = (String) comboCarrera.getSelectedItem();

        if (carnet.isEmpty() || nombre.isEmpty() || carrera == null) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        EstudianteDAO estudianteDAO = new EstudianteDAO();
        boolean exitoSQL = estudianteDAO.insertar(carnet, nombre, carrera);

        if (exitoSQL) {
            Estudiante estudiante = new Estudiante(carnet, nombre, carrera);
            arbol.insertar(estudiante);
            JOptionPane.showMessageDialog(this, "Estudiante registrado con éxito.");
            cargarRecorrido("inorden");
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar en BD o carnet duplicado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstudiante() {
        if (carnetSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un estudiante desde la tabla.");
            return;
        }

        String nuevoNombre = txtNombre.getText().trim();
        String nuevaCarrera = (String) comboCarrera.getSelectedItem();

        if (nuevoNombre.isEmpty() || nuevaCarrera == null) {
            JOptionPane.showMessageDialog(this, "Nombre y carrera no deben estar vacíos.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas actualizar este estudiante?", "Confirmar actualización", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            if (estudianteDAO.actualizar(carnetSeleccionado, nuevoNombre, nuevaCarrera)) {
                arbol.actualizar(carnetSeleccionado, nuevoNombre, nuevaCarrera);
                JOptionPane.showMessageDialog(this, "Estudiante actualizado con éxito.");
                cargarRecorrido("inorden");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarEstudiante() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String carnet = (String) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar al estudiante con carnet: " + carnet + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                EstudianteDAO estudianteDAO = new EstudianteDAO();
                if (estudianteDAO.eliminar(carnet)) {
                    arbol.eliminar(carnet);
                    JOptionPane.showMessageDialog(this, "Estudiante eliminado con éxito.");
                    cargarRecorrido("inorden");
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una fila de la tabla para eliminar.");
        }
    }

    private void buscarEstudiante() {
        String carnet = JOptionPane.showInputDialog(this, "Ingrese el carnet a buscar:");
        if (carnet == null || carnet.trim().isEmpty()) return;

        Estudiante encontrado = arbol.buscar(carnet.trim());
        if (encontrado != null) {
            JOptionPane.showMessageDialog(this, "Estudiante encontrado:\nNombre: " + encontrado.getNombre() + "\nCarrera: " + encontrado.getCarrera());
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el estudiante.");
        }
    }

    private void cargarRecorrido(String tipo) {
        modelo.setRowCount(0);
        switch (tipo.toLowerCase()) {
            case "inorden" -> arbol.inorden(est -> modelo.addRow(new Object[]{est.getCarnet(), est.getNombre(), est.getCarrera()}));
            case "preorden" -> arbol.preorden(est -> modelo.addRow(new Object[]{est.getCarnet(), est.getNombre(), est.getCarrera()}));
            case "postorden" -> arbol.postorden(est -> modelo.addRow(new Object[]{est.getCarnet(), est.getNombre(), est.getCarrera()}));
        }
    }

    private void limpiarCampos() {
        txtCarnet.setText("");
        txtCarnet.setEditable(true);
        txtNombre.setText("");
        comboCarrera.setSelectedIndex(0);
        carnetSeleccionado = null;
    }

    private void cargarDatosDesdeSQL() {
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        java.util.List<String[]> estudiantesSQL = estudianteDAO.listar();

        for (String[] datos : estudiantesSQL) {
            Estudiante estudiante = new Estudiante(datos[0], datos[1], datos[2]);
            arbol.insertar(estudiante);
        }
        cargarRecorrido("inorden");
    }
}