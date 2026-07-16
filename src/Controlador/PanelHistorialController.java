package Controlador;

import gui.PanelHistorial;
import estructuras.PilaAcciones_U3;
import estructuras.AccionMatricula;
import modelo.Matricula;

public class PanelHistorialController {

    private PanelHistorial vista;
    private PilaAcciones_U3 pilaAcciones;

    public PanelHistorialController(PanelHistorial vista, PilaAcciones_U3 pilaAcciones) {
        this.vista = vista;
        this.pilaAcciones = pilaAcciones;

        // Escuchar el evento de filtrado
        this.vista.getBtnFiltrar().addActionListener(e -> cargarHistorial());

        // Carga inicial al abrir el panel
        cargarHistorial();
    }

    private void cargarHistorial() {
        vista.limpiarTabla();
        String filtro = vista.getFiltroSeleccionado();

        if (filtro == null) {
            filtro = "Todos";
        }

        // Copiar la pila para no modificar ni destruir la estructura original de la app
        PilaAcciones_U3 copia = pilaAcciones.copiar();
        
        while (!copia.estaVacia()) {
            AccionMatricula accion = copia.desapilar();
            Matricula mat = accion.getMatricula();

            // Aplicar el filtro de carnet de estudiante
            if (filtro.equals("Todos") || mat.getEstudiante().getCarnet().equals(filtro)) {
                vista.agregarFilaTabla(new Object[]{
                        mat.getEstudiante().getCarnet(),
                        mat.getEstudiante().getNombre(),
                        mat.getCurso().getCodigo(),
                        mat.getCurso().getNombre(),
                        accion.getTipo()
                });
            }
        }
    }
}