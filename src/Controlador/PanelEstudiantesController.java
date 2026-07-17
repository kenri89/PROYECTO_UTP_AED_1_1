package Controlador;

import gui.PanelEstudiantes;
import modelo.Estudiante;
import estructuras.ArbolEstudiantes;
import dao.EstudianteDAO;
import util.ExportadorExcel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class PanelEstudiantesController {

    private PanelEstudiantes vista;
    private ArbolEstudiantes arbol;
    private EstudianteDAO estudianteDAO;
    private String carnetSeleccionado;

    public PanelEstudiantesController(PanelEstudiantes vista, ArbolEstudiantes arbol) {
        this.vista = vista;
        this.arbol = arbol;
        this.estudianteDAO = new EstudianteDAO();
        this.carnetSeleccionado = null;

        // Registrar los listeners de los botones y opciones del menu de la vista
        this.vista.getBtnRegistrar().addActionListener(e -> registrarEstudiante());
        this.vista.getBtnActualizar().addActionListener(e -> actualizarEstudiante());
        this.vista.getBtnBuscar().addActionListener(e -> buscarEstudiante());
        this.vista.getBtnExportar().addActionListener(e -> exportarExcel());
        this.vista.getItemActualizar().addActionListener(e -> cargarEstudianteSeleccionado());
        this.vista.getItemEliminar().addActionListener(e -> eliminarEstudiante());

        // Cargar datos iniciales
        try {
            cargarDatosDesdeSQL();
        } catch (Exception e) {
            System.err.println("Error al cargar datos desde BD (usando modo local): " + e.getMessage());
        }
    }

    private void cargarDatosDesdeSQL() {
        java.util.List<String[]> estudiantesSQL = estudianteDAO.listar();

        arbol.limpiar();

        for (String[] datos : estudiantesSQL) {
            Estudiante estudiante = new Estudiante(datos[0], datos[1], datos[2]);
            arbol.insertar(estudiante);
        }

        actualizarTablaVista();
    }

    private void actualizarTablaVista() {
        vista.limpiarTabla();
        // Recorremos el árbol de manera inorden para llenar la tabla
        arbol.inorden(est -> vista.agregarFilaTabla(new Object[]{
                est.getCarnet(), 
                est.getNombre(), 
                est.getCarrera()
        }));
    }

    private void registrarEstudiante() {
        String carnet = vista.getCarnetInput();
        String nombre = vista.getNombreInput();
        String carrera = vista.getCarreraSeleccionada();

        if (carnet.isEmpty() || nombre.isEmpty() || carrera == null) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos.");
            return;
        }

        boolean exitoSQL = estudianteDAO.insertar(carnet, nombre, carrera);

        if (exitoSQL) {
            JOptionPane.showMessageDialog(vista, "Estudiante registrado con éxito en la base de datos.");
            cargarDatosDesdeSQL();
        } else {
            JOptionPane.showMessageDialog(vista, "Error: no se pudo guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        vista.limpiarCampos();
    }

    private void cargarEstudianteSeleccionado() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila == -1) return;

        carnetSeleccionado = (String) vista.getModelo().getValueAt(fila, 0);
        String nombre = (String) vista.getModelo().getValueAt(fila, 1);
        String carrera = (String) vista.getModelo().getValueAt(fila, 2);

        vista.rellenarCampos(carnetSeleccionado, nombre, carrera);
    }

    private void actualizarEstudiante() {
        if (carnetSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "Debes seleccionar un estudiante desde el menú contextual.");
            return;
        }

        String nuevoNombre = vista.getNombreInput();
        String nuevaCarrera = vista.getCarreraSeleccionada();

        if (nuevoNombre.isEmpty() || nuevaCarrera == null) {
            JOptionPane.showMessageDialog(vista, "Nombre y carrera no deben estar vacíos.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista, "¿Deseas actualizar este estudiante?", "Confirmar actualización", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            estudianteDAO.actualizar(carnetSeleccionado, nuevoNombre, nuevaCarrera);

            cargarDatosDesdeSQL();
            vista.limpiarCampos();
            carnetSeleccionado = null;
        }
    }

    private void eliminarEstudiante() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona un estudiante para eliminar.");
            return;
        }

        String carnet = (String) vista.getModelo().getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Deseas eliminar al estudiante con carnet: " + carnet + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            estudianteDAO.eliminar(carnet);
            cargarDatosDesdeSQL();
            vista.limpiarCampos();
            carnetSeleccionado = null;
        }
    }

    private void buscarEstudiante() {
        String carnet = JOptionPane.showInputDialog(vista, "Ingrese el carnet a buscar:");
        if (carnet == null || carnet.trim().isEmpty()) return;

        Estudiante encontrado = arbol.buscar(carnet.trim());
        if (encontrado != null) {
            JOptionPane.showMessageDialog(vista, "Estudiante encontrado:\nNombre: " + encontrado.getNombre() + "\nCarrera: " + encontrado.getCarrera());
        } else {
            JOptionPane.showMessageDialog(vista, "No se encontró el estudiante.");
        }
    }

    private void exportarExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("estudiantes.xls"));
        if (fc.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            try {
                ExportadorExcel.exportarEstudiantes(arbol, archivo);
                JOptionPane.showMessageDialog(vista, "Estudiantes exportados a Excel exitosamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}