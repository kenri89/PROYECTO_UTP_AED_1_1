package Controlador; // Modifica a 'controlador' si tu paquete es en minúscula

import gui.PanelAdministradorSolicitudes;
import modelo.Solicitud;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class PanelAdministradorSolicitudesController {

    private PanelAdministradorSolicitudes vista;
    private Queue<Solicitud> colaSolicitudes;
    private java.util.List<Solicitud> solicitudesAtendidas;

    public PanelAdministradorSolicitudesController(PanelAdministradorSolicitudes vista, Queue<Solicitud> colaSolicitudes) {
        this.vista = vista;
        this.colaSolicitudes = colaSolicitudes;
        this.solicitudesAtendidas = new LinkedList<>();

        // Enlazar el botón de la vista con la lógica de este controlador
        this.vista.getBtnAtender().addActionListener(e -> atenderSiguiente());

        // Cargar las tablas inicialmente
        actualizarTablas();
    }

    private void atenderSiguiente() {
        if (colaSolicitudes.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay solicitudes pendientes.");
            return;
        }

        // Lógica de Negocio / Datos
        Solicitud s = colaSolicitudes.poll(); // Extraer de la cola
        s.setAtendida(true);
        s.setFechaAtencion(LocalDateTime.now());
        solicitudesAtendidas.add(s);

        // Actualizar lo que ve el usuario
        actualizarTablas();
    }

    private void actualizarTablas() {
        // Le ordenamos a la vista que limpie las tablas antiguas
        vista.limpiarTablas();

        // Rellenar pendientes en la vista
        for (Solicitud s : colaSolicitudes) {
            vista.agregarFilaPendiente(new Object[]{
                    s.getCarnet(),
                    s.getTipo(),
                    s.getDescripcion(),
                    s.getFechaFormateada()
            });
        }

        // Rellenar atendidas en la vista
        for (Solicitud s : solicitudesAtendidas) {
            vista.agregarFilaAtendida(new Object[]{
                    s.getCarnet(),
                    s.getTipo(),
                    s.getDescripcion(),
                    s.getFechaFormateada(),
                    s.getFechaAtencionFormateada()
            });
        }
    }
}