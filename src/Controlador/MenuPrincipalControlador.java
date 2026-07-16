package Controlador;

import modelo.Usuario;
import modelo.Solicitud;
import estructuras.*;
import gui.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MenuPrincipalControlador {

    private MenuPrincipal vista;
    private Usuario usuarioActual;

    // El controlador ahora es el dueño y administrador de las estructuras de datos (Modelo)
    private ListaCursos listaCursos;
    private ArregloCursos arregloCursos;
    private MatrizSemestres matrizSemestres;
    private ArbolEstudiantes arbolEstudiantes;
    private ListaMatricula listaMatricula;
    private final PilaAcciones_U3 pilaHistorial;
    private Queue<Solicitud> colaSolicitudes;

    public MenuPrincipalControlador(MenuPrincipal vista) {
        this.vista = vista;
        this.usuarioActual = vista.getUsuarioActual();

        // Inicializamos las estructuras de datos aquí
        this.arregloCursos = new ArregloCursos();
        this.matrizSemestres = new MatrizSemestres();
        this.listaCursos = new ListaCursos();
        this.arbolEstudiantes = new ArbolEstudiantes();
        this.listaMatricula = new ListaMatricula();
        this.pilaHistorial = new PilaAcciones_U3();
        this.colaSolicitudes = new LinkedList<>();

        // Enlazamos las acciones de los botones
        conectarEventos();
    }

    private void conectarEventos() {
        vista.getBtnCursos().addActionListener(e -> {
            PanelCursos vistaCursos = new PanelCursos();
            PanelCursosController controller = new PanelCursosController(
                    vistaCursos,
                    this.arregloCursos,
                    this.matrizSemestres,
                    this.listaCursos
            );
            vista.cambiarPanelCentro(vistaCursos);
        });

        vista.getBtnEstudiantes().addActionListener(e -> {
            PanelEstudiantes vistaEstudiantes = new PanelEstudiantes();

            // 2. Instancias el controlador pasándole la vista y la estructura del árbol
            PanelEstudiantesController controller = new PanelEstudiantesController(vistaEstudiantes, this.arbolEstudiantes);

            // 3. Reemplazas el panel en la UI principal
            vista.cambiarPanelCentro(vistaEstudiantes);
        });

        vista.getBtnMatricula().addActionListener(e -> {
            PanelMatricula panelMatricula = new PanelMatricula();

            new PanelMatriculaController(panelMatricula, listaCursos, arbolEstudiantes, listaMatricula, pilaHistorial);

            vista.cambiarPanelCentro(panelMatricula);
        });

        vista.getBtnHistorial().addActionListener(e -> {
            java.util.List<String> carnets = new java.util.ArrayList<>();
            arbolEstudiantes.inorden(est -> carnets.add(est.getCarnet()));
            String[] arregloCarnets = carnets.toArray(new String[0]);

            PanelHistorial panelHistorial = new PanelHistorial(arregloCarnets);

            new PanelHistorialController(panelHistorial, pilaHistorial);

            vista.cambiarPanelCentro(panelHistorial);
        });

        vista.getBtnSolicitudes().addActionListener(e -> {
            PanelSolicitudesEstudiante panelSolicitudes = new PanelSolicitudesEstudiante();
            new PanelSolicitudesEstudianteController(panelSolicitudes, colaSolicitudes);
            vista.cambiarPanelCentro(panelSolicitudes);
        });

        vista.getBtnMisMatriculas().addActionListener(e -> {
            PanelMisMatriculas panelMisMatriculas = new PanelMisMatriculas();
            new PanelMisMatriculasController(panelMisMatriculas, listaMatricula, usuarioActual.getCarnet(), arbolEstudiantes);

            vista.cambiarPanelCentro(panelMisMatriculas);
        });

        vista.getBtnAtenderSolicitudes().addActionListener(e -> {
            String rolClick = usuarioActual.getRol() == null ? "" : usuarioActual.getRol().trim().toLowerCase();
            if (!(rolClick.equals("admin") || rolClick.equals("administrador"))) {
                JOptionPane.showMessageDialog(vista, "Acceso restringido solo para administradores.");
                return;
            }

            PanelAdministradorSolicitudes panelVista = new PanelAdministradorSolicitudes();

            PanelAdministradorSolicitudesController panelController
                    = new PanelAdministradorSolicitudesController(panelVista, colaSolicitudes);

            vista.cambiarPanelCentro(panelVista);
        });

        vista.getBtnSalir().addActionListener(e -> {
            int confirmar = JOptionPane.showConfirmDialog(vista, "¿Deseas salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
}
