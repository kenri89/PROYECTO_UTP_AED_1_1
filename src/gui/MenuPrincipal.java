package gui;

import modelo.Usuario;
import modelo.Solicitud;
import estructuras.ListaCursos;
import estructuras.ArregloCursos;
import estructuras.MatrizSemestres;
import estructuras.ArbolEstudiantes;
import estructuras.ListaMatricula;
import estructuras.PilaAcciones_U3;
import util.PersistenciaAcademica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MenuPrincipal extends JFrame {

    private JPanel panelContenido;
    private Usuario usuarioActual;

    private ListaCursos listaCursos;
    private ArregloCursos arregloCursos;
    private MatrizSemestres matrizSemestres;
    private ArbolEstudiantes arbolEstudiantes;
    private ListaMatricula listaMatricula;
    private final PilaAcciones_U3 pilaHistorial = new PilaAcciones_U3(); // única pila

    private Queue<Solicitud> colaSolicitudes = new LinkedList<>(); // COLA para solicitudes

    public MenuPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;

        // Estructuras de datos
        arregloCursos = new ArregloCursos();
        matrizSemestres = new MatrizSemestres();
        listaCursos = new ListaCursos();
        arbolEstudiantes = new ArbolEstudiantes();
        listaMatricula = new ListaMatricula();

        PersistenciaAcademica.cargar(arregloCursos, matrizSemestres, listaCursos,
                arbolEstudiantes, listaMatricula, colaSolicitudes);

        setTitle("Sistema Académico - Rol: " + usuarioActual.getRol());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarDatos();
            }
        });
        setLayout(new BorderLayout());

        Color azulFondo = new Color(220, 240, 255);
        Color azulBoton = new Color(0, 120, 215);
        Color azulTexto = new Color(0, 80, 160);
        getContentPane().setBackground(azulFondo);

        JLabel lblBienvenida = new JLabel("Usuario: " + usuarioActual.getUsername() + " | Rol: " + usuarioActual.getRol(), JLabel.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setForeground(azulTexto);
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(8, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelBotones.setBackground(new Color(200, 225, 255));

        JButton btnCursos = new JButton("Gestión de Cursos");
        JButton btnEstudiantes = new JButton("Gestión de Estudiantes");
        JButton btnMatricula = new JButton("Matrícula");
        JButton btnHistorial = new JButton("Historial Académico");
        JButton btnSolicitudes = new JButton("Solicitudes");
        JButton btnMisMatriculas = new JButton("Mis cursos matriculados");
        JButton btnAtenderSolicitudes = new JButton("Atender Solicitudes (Admin)");
        JButton btnSalir = new JButton("Salir");

        // Restricciones por rol (visibilidad de opciones)
        // Nota: se normaliza para soportar "Secretaría"/"Secretaria" y "Administrador"/"Admin"
        String rol = usuarioActual.getRol() == null ? "" : usuarioActual.getRol().trim().toLowerCase();
        boolean esAdmin = rol.equals("admin") || rol.equals("administrador");
        boolean esSecretaria = rol.equals("secretaría") || rol.equals("secretaria");
        boolean esEstudiante = rol.equals("estudiante");
        boolean esDocente = rol.equals("docente");

        // Política actual (ajústala si tu proyecto lo requiere):
        // - Admin: todo
        // - Secretaría: cursos, estudiantes, matrícula, solicitudes (ver)
        // - Estudiante: historial, solicitudes, mis matrículas (crear/ver)
        // - Docente: historial (ver) (por ahora)
        btnCursos.setVisible(esAdmin || esSecretaria);
        btnEstudiantes.setVisible(esAdmin || esSecretaria);
        btnMatricula.setVisible(esAdmin || esSecretaria);
        btnHistorial.setVisible(esAdmin || esEstudiante || esDocente);
        btnSolicitudes.setVisible(esAdmin || esEstudiante || esSecretaria);
        btnMisMatriculas.setVisible(esEstudiante);
        btnAtenderSolicitudes.setVisible(esAdmin);

        JButton[] botones = {
            btnCursos, btnEstudiantes, btnMatricula, btnHistorial,
            btnSolicitudes, btnMisMatriculas, btnAtenderSolicitudes, btnSalir
        };

        for (JButton btn : botones) {
            btn.setBackground(azulBoton);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setPreferredSize(new Dimension(200, 50));
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.WEST);

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(azulFondo);
        panelContenido.add(new JLabel("Bienvenido al sistema académico", JLabel.CENTER), BorderLayout.CENTER);
        add(panelContenido, BorderLayout.CENTER);

        // Navegación
        btnCursos.addActionListener(e -> {
            panelContenido.removeAll();
            panelContenido.add(new PanelCursos(arregloCursos, matrizSemestres, listaCursos), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnEstudiantes.addActionListener(e -> {
            panelContenido.removeAll();
            panelContenido.add(new PanelEstudiantes(arbolEstudiantes), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnMatricula.addActionListener(e -> {
            panelContenido.removeAll();
            panelContenido.add(new PanelMatricula(listaCursos, arbolEstudiantes, listaMatricula, pilaHistorial), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnHistorial.addActionListener(e -> {
            java.util.List<String> carnets = new ArrayList<>();
            arbolEstudiantes.inorden(est -> carnets.add(est.getCarnet()));

            // ✅ Conversión de List<String> a String[]
            String[] arregloCarnets = carnets.toArray(new String[0]);

            // ✅ PanelHistorial espera un arreglo, no una lista
            PanelHistorial panel = new PanelHistorial(pilaHistorial, arregloCarnets);
            panelContenido.removeAll();
            panelContenido.add(panel, BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnSolicitudes.addActionListener(e -> {
            panelContenido.removeAll();
            panelContenido.add(new PanelSolicitudesEstudiante(colaSolicitudes), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnMisMatriculas.addActionListener(e -> {
            panelContenido.removeAll();
            panelContenido.add(new PanelMisMatriculas(listaMatricula, usuarioActual.getCarnet(), arbolEstudiantes), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnAtenderSolicitudes.addActionListener(e -> {
            String rolClick = usuarioActual.getRol() == null ? "" : usuarioActual.getRol().trim().toLowerCase();
            if (!(rolClick.equals("admin") || rolClick.equals("administrador"))) {
                JOptionPane.showMessageDialog(this, "Acceso restringido solo para administradores.");
                return;
            }

            panelContenido.removeAll();
            panelContenido.add(new PanelAdministradorSolicitudes(colaSolicitudes), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnSalir.addActionListener(e -> {
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Deseas salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                guardarDatos();
                System.exit(0);
            }
        });
    }

    private void guardarDatos() {
        PersistenciaAcademica.guardar(arregloCursos, arbolEstudiantes, matrizSemestres,
                listaCursos, listaMatricula, colaSolicitudes);
    }
}
