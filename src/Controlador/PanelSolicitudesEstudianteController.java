package Controlador;

import com.google.common.base.Strings;
import gui.PanelSolicitudesEstudiante;
import modelo.Solicitud;

import javax.swing.*;
import java.util.Queue;

public class PanelSolicitudesEstudianteController {

    private final PanelSolicitudesEstudiante vista;
    private final Queue<Solicitud> colaSolicitudes;

    public PanelSolicitudesEstudianteController(PanelSolicitudesEstudiante vista, Queue<Solicitud> colaSolicitudes) {
        this.vista = vista;
        this.colaSolicitudes = colaSolicitudes;

        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnEnviar().addActionListener(e -> enviarSolicitud());
    }

    private void enviarSolicitud() {
        String carnet = vista.getCarnet();
        String tipo = vista.getTipoSolicitud();
        String descripcion = vista.getDescripcion();

        // Validación de negocio
        if (Strings.isNullOrEmpty(carnet) || Strings.isNullOrEmpty(descripcion)) {
            JOptionPane.showMessageDialog(vista, "Por favor completa todos los campos.");
            return;
        }

        // Crear la entidad de negocio y meterla en la cola (modelo/estructura)
        Solicitud solicitud = new Solicitud(carnet, tipo, descripcion);
        boolean encoladoExitoso = colaSolicitudes.offer(solicitud);

        if (encoladoExitoso) {
            JOptionPane.showMessageDialog(vista,
                    "Solicitud enviada correctamente.\n\n" +
                    "Resumen:\n" +
                    "Carnet: " + carnet + "\n" +
                    "Tipo: " + tipo + "\n" +
                    "Fecha: " + solicitud.getFechaFormateada());

            // Limpiamos los campos visuales a través de la interfaz limpia de la vista
            vista.limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al enviar la solicitud. Inténtalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}