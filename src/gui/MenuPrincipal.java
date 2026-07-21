package gui;

import modelo.Usuario;
import estructuras.ListaCursos;
import estructuras.ArregloCursos;
import estructuras.MatrizSemestres;
import estructuras.ArbolEstudiantes;
import estructuras.ListaMatricula;
import estructuras.PilaAcciones_U3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame {

    private JPanel panelContenido;
    private Usuario usuarioActual;

    private ListaCursos listaCursos;
    private ArregloCursos arregloCursos;
    private MatrizSemestres matrizSemestres;
    private ArbolEstudiantes arbolEstudiantes;
    private ListaMatricula listaMatricula;
    private final PilaAcciones_U3 pilaHistorial = new PilaAcciones_U3();

    public MenuPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;

        arregloCursos = new ArregloCursos();
        matrizSemestres = new MatrizSemestres();
        listaCursos = new ListaCursos();
        arbolEstudiantes = new ArbolEstudiantes();
        listaMatricula = new ListaMatricula();

        setTitle("Sistema Académico - Rol: " + usuarioActual.getRol());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new LoginFrame().setVisible(true);
            }
        });
        setLayout(new BorderLayout());

        getContentPane().setBackground(UIConstants.PANEL_BG);

        JLabel lblBienvenida = new JLabel("Usuario: " + usuarioActual.getUsername() + " | Rol: " + usuarioActual.getRol(), JLabel.CENTER);
        lblBienvenida.setFont(UIConstants.FONT_HEADER);
        lblBienvenida.setForeground(UIConstants.TEXT_BLUE);
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(9, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelBotones.setBackground(UIConstants.PANEL_HEADER);

        JButton btnCursos = UIConstants.crearBoton("Gestión de Cursos");
        JButton btnEstudiantes = UIConstants.crearBoton("Gestión de Estudiantes");
        JButton btnMatricula = UIConstants.crearBoton("Matrícula");
        JButton btnHistorial = UIConstants.crearBoton("Historial Académico");
        JButton btnSolicitudes = UIConstants.crearBoton("Solicitudes");
        JButton btnDashboard = UIConstants.crearBoton("Dashboard");
        JButton btnMisMatriculas = UIConstants.crearBoton("Mis cursos matriculados");
        JButton btnAtenderSolicitudes = UIConstants.crearBoton("Atender Solicitudes (Admin)");
        JButton btnSalir = UIConstants.crearBoton("Salir");

        String rol = usuarioActual.getRol() == null ? "" : usuarioActual.getRol().trim().toLowerCase();
        boolean esAdmin = rol.equals("admin") || rol.equals("administrador");
        boolean esSecretaria = rol.equals("secretaría") || rol.equals("secretaria");
        boolean esEstudiante = rol.equals("estudiante");
        boolean esDocente = rol.equals("docente");

        btnCursos.setVisible(esAdmin || esSecretaria);
        btnEstudiantes.setVisible(esAdmin || esSecretaria);
        btnMatricula.setVisible(esAdmin || esSecretaria || esEstudiante);
        btnHistorial.setVisible(esAdmin || esEstudiante || esDocente);
        btnSolicitudes.setVisible(esAdmin || esEstudiante || esSecretaria);
        btnDashboard.setVisible(true);
        btnMisMatriculas.setVisible(esEstudiante);
        btnAtenderSolicitudes.setVisible(esAdmin);

        JButton[] botones = {
            btnCursos, btnEstudiantes, btnMatricula, btnHistorial,
            btnSolicitudes, btnDashboard, btnMisMatriculas, btnAtenderSolicitudes, btnSalir
        };

        for (JButton btn : botones) {
            btn.setPreferredSize(UIConstants.BUTTON_SIZE);
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.WEST);

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(UIConstants.PANEL_BG);
        panelContenido.add(new PanelDashboard(usuarioActual, arbolEstudiantes, arregloCursos, listaCursos, listaMatricula, matrizSemestres), BorderLayout.CENTER);
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
            String carnetEst = null;
            if (usuarioActual.getRol().trim().equalsIgnoreCase("estudiante")) {
                carnetEst = usuarioActual.getCarnet() != null ? usuarioActual.getCarnet() : usuarioActual.getUsername();
            }
            panelContenido.add(new PanelMatricula(listaCursos, arbolEstudiantes, listaMatricula, pilaHistorial, carnetEst), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnHistorial.addActionListener(e -> {
            java.util.List<String> carnets = new ArrayList<>();
            arbolEstudiantes.inorden(est -> carnets.add(est.getCarnet()));

            // ✅ Conversión de List<String> a String[]
            String[] arregloCarnets = carnets.toArray(new String[0]);

            // ✅ PanelHistorial espera un arreglo, no una lista
            PanelHistorial panel = new PanelHistorial(arregloCarnets);
            panelContenido.removeAll();
            panelContenido.add(panel, BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnSolicitudes.addActionListener(e -> {
            panelContenido.removeAll();
            panelContenido.add(new PanelSolicitudesEstudiante(), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnDashboard.addActionListener(e -> {
            panelContenido.removeAll();
            panelContenido.add(new PanelDashboard(usuarioActual, arbolEstudiantes, arregloCursos, listaCursos, listaMatricula, matrizSemestres), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnMisMatriculas.addActionListener(e -> {
            panelContenido.removeAll();
            String carnet = usuarioActual.getCarnet() != null ? usuarioActual.getCarnet() : usuarioActual.getUsername();
            panelContenido.add(new PanelMisMatriculas(listaMatricula, carnet, arbolEstudiantes), BorderLayout.CENTER);
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
            panelContenido.add(new PanelAdministradorSolicitudes(), BorderLayout.CENTER);
            panelContenido.revalidate();
            panelContenido.repaint();
        });

        btnSalir.addActionListener(e -> {
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }

}
