package gui;

import estructuras.ArbolEstudiantes;
import modelo.Estudiante;
import util.ExportadorExcel;
import util.ConexionSQL;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
        setBackground(UIConstants.PANEL_BG);

        agregarFormulario();
        agregarTabla();
        try {
            cargarDatosDesdeSQL();
        } catch (Exception e) {
            System.err.println("Error al cargar datos desde BD (usando modo local): " + e.getMessage());
        } 
    }

    private void agregarFormulario() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Estudiante"));
        panelForm.setBackground(UIConstants.PANEL_FORM);

        txtCarnet = new JTextField();
        txtNombre = new JTextField();
        comboCarrera = new JComboBox<>(new String[]{"SECRETARIADO", "AGROPECUARIA", "MECANICA AUTOMOTRIZ", "ENFERMERIA TECNICA", "LABORATORIO CLINICO", "INFORMATICA"});

        panelForm.add(new JLabel("Carnet:"));
        panelForm.add(txtCarnet);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Carrera:"));
        panelForm.add(comboCarrera);

        JButton btnAgregar = UIConstants.crearBoton("Registrar");
        JButton btnActualizar = UIConstants.crearBoton("Actualizar");
        JButton btnBuscar = UIConstants.crearBoton("Buscar por carnet");

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
                tabla.getSelectionModel().setSelectionInterval(fila, fila);
            }
        });

        itemActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                carnetSeleccionado = (String) modelo.getValueAt(fila, 0);
                txtCarnet.setText((String) modelo.getValueAt(fila, 0));
                txtNombre.setText((String) modelo.getValueAt(fila, 1));
                comboCarrera.setSelectedItem((String) modelo.getValueAt(fila, 2));
            }
        });

        itemEliminar.addActionListener(e -> eliminarEstudiante());

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        JToolBar barraExport = new JToolBar();
        barraExport.setFloatable(false);
        barraExport.setBackground(UIConstants.PANEL_BG);

        JButton btnExportar = UIConstants.crearBoton("Exportar a Excel");
        btnExportar.addActionListener(e -> exportarExcel());
        barraExport.add(Box.createHorizontalGlue());
        barraExport.add(btnExportar);
        barraExport.add(Box.createHorizontalStrut(10));

        panelTabla.add(barraExport, BorderLayout.SOUTH);
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

        dao.EstudianteDAO estudianteDAO = new dao.EstudianteDAO();
        boolean exitoSQL = estudianteDAO.insertar(carnet, nombre, carrera);

        if (exitoSQL) {
            String hash = BCrypt.hashpw("1234", BCrypt.gensalt());
            String sql = "INSERT INTO usuarios (username, password, rol) VALUES (?, ?, 'Estudiante')";
            try (java.sql.Connection cn = ConexionSQL.getConexion();
                 java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, carnet);
                ps.setString(2, hash);
                ps.executeUpdate();
            } catch (Exception e) {
                System.err.println("No se creó cuenta de usuario: " + e.getMessage());
            }
            JOptionPane.showMessageDialog(this, "Estudiante registrado con éxito en la base de datos.");
            cargarDatosDesdeSQL();
        } else {
            JOptionPane.showMessageDialog(this, "Error: no se pudo guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        limpiarCampos();
    }

    private void actualizarEstudiante() {
        if (carnetSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un estudiante desde el menú contextual.");
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
            dao.EstudianteDAO estudianteDAO = new dao.EstudianteDAO();
            estudianteDAO.actualizar(carnetSeleccionado, nuevoNombre, nuevaCarrera);

            cargarDatosDesdeSQL();
            limpiarCampos();
            carnetSeleccionado = null;
        }
    }

    private void eliminarEstudiante() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String carnet = (String) modelo.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar al estudiante con carnet: " + carnet + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.EstudianteDAO estudianteDAO = new dao.EstudianteDAO();
                estudianteDAO.eliminar(carnet);

                cargarDatosDesdeSQL();
            }
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

    private void cargarTabla() {
        modelo.setRowCount(0);
        arbol.inorden(est -> modelo.addRow(new Object[]{
                est.getCarnet(), est.getNombre(), est.getCarrera()
        }));
    }

    private void limpiarCampos() {
        txtCarnet.setText("");
        txtNombre.setText("");
        comboCarrera.setSelectedIndex(0);
    }

    private void cargarDatosDesdeSQL() {
        dao.EstudianteDAO estudianteDAO = new dao.EstudianteDAO();
        java.util.List<String[]> estudiantesSQL = estudianteDAO.listar();

        arbol.limpiar();

        for (String[] datos : estudiantesSQL) {
            Estudiante estudiante = new Estudiante(datos[0], datos[1], datos[2]);
            arbol.insertar(estudiante);
        }

        cargarTabla();
    }

    private void exportarExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("estudiantes.xlsx"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            try {
                ExportadorExcel.exportarEstudiantes(arbol, archivo);
                JOptionPane.showMessageDialog(this, "Estudiantes exportados a Excel exitosamente.");
            } catch (Throwable ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
