package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una solicitud hecha por un estudiante.
 * Utilizada en el sistema académico para gestionar solicitudes
 * mediante una estructura de datos tipo Cola.
 */
public class Solicitud {
    private String carnet;           // Identificador del estudiante
    private String tipo;             // Tipo de solicitud (ej: "Cambio de curso")
    private String descripcion;      // Detalle de la solicitud
    private LocalDateTime fecha;     // Fecha y hora en que se hizo la solicitud
    private boolean atendida;        // Indica si ya fue atendida o no
    private LocalDateTime fechaAtencion; // Nueva: Fecha y hora en que fue atendida

    /**
     * Constructor que inicializa la solicitud con su información principal.
     */
    public Solicitud(String carnet, String tipo, String descripcion) {
        this(carnet, tipo, descripcion, LocalDateTime.now(), false, null);
    }

    /** Restauración desde persistencia (misma firma que los datos guardados). */
    public Solicitud(String carnet, String tipo, String descripcion,
                     LocalDateTime fecha, boolean atendida, LocalDateTime fechaAtencion) {
        this.carnet = carnet;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
        this.atendida = atendida;
        this.fechaAtencion = fechaAtencion;
    }

    // Getters básicos
    public String getCarnet() {
        return carnet;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    /**
     * Devuelve la fecha y hora en que fue creada la solicitud, como cadena formateada.
     */
    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * Devuelve la fecha y hora en que fue atendida la solicitud, como cadena formateada.
     * Si no ha sido atendida aún, devuelve una cadena vacía.
     */
    public String getFechaAtencionFormateada() {
        return (fechaAtencion != null) ? fechaAtencion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
    }

    /**
     * Alias para obtener la descripción, útil para compatibilidad con otras vistas.
     */
    public String getDetalle() {
        return descripcion;
    }
}
