package modelo;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Solicitud {
    private String carnet;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
    private boolean atendida;
    private LocalDateTime fechaAtencion;

    public Solicitud(String carnet, String tipo, String descripcion) {
        this(carnet, tipo, descripcion, LocalDateTime.now(), false, null);
    }

    public Solicitud(String carnet, String tipo, String descripcion,
                     LocalDateTime fecha, boolean atendida, LocalDateTime fechaAtencion) {
        this.carnet = checkNotNull(carnet, "El carnet no puede ser nulo");
        this.tipo = checkNotNull(tipo, "El tipo no puede ser nulo");
        this.descripcion = Strings.nullToEmpty(descripcion);
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
        this.atendida = atendida;
        this.fechaAtencion = fechaAtencion;
    }

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

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getFechaAtencionFormateada() {
        return (fechaAtencion != null) ? fechaAtencion.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
    }

    public String getDetalle() {
        return descripcion;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("carnet", carnet)
                .add("tipo", tipo)
                .add("atendida", atendida)
                .toString();
    }
}
