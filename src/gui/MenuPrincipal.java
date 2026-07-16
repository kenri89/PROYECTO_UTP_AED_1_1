package gui;

import modelo.Usuario;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class MenuPrincipal extends JFrame {

    private JPanel panelContenido;
    private Usuario usuarioActual;

    // Declaramos los botones como atributos para poder exponerlos
    private JButton btnCursos;
    private JButton btnEstudiantes;
    private JButton btnMatricula;
    private JButton btnHistorial;
    private JButton btnSolicitudes;
    private JButton btnMisMatriculas;
    private JButton btnAtenderSolicitudes;
    private JButton btnSalir;

    public MenuPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;

        setTitle("Sistema Académico - Rol: " + usuarioActual.getRol());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
        getContentPane().setBackground(UIConstants.PANEL_BG);

        // Cabecera de bienvenida
        JLabel lblBienvenida = new JLabel("Usuario: " + usuarioActual.getUsername() + " | Rol: " + usuarioActual.getRol(), JLabel.CENTER);
        lblBienvenida.setFont(UIConstants.FONT_HEADER);
        lblBienvenida.setForeground(UIConstants.TEXT_BLUE);
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        // Panel de botones (Menú lateral)
        JPanel panelBotones = new JPanel(new GridLayout(8, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelBotones.setBackground(UIConstants.PANEL_HEADER);

        btnCursos = UIConstants.crearBoton("Gestión de Cursos");
        btnEstudiantes = UIConstants.crearBoton("Gestión de Estudiantes");
        btnMatricula = UIConstants.crearBoton("Matrícula");
        btnHistorial = UIConstants.crearBoton("Historial Académico");
        btnSolicitudes = UIConstants.crearBoton("Solicitudes");
        btnMisMatriculas = UIConstants.crearBoton("Mis cursos matriculados");
        btnAtenderSolicitudes = UIConstants.crearBoton("Atender Solicitudes (Admin)");
        btnSalir = UIConstants.crearBoton("Salir");

        // Lógica visual de permisos (Esto se queda en la vista porque es visual)
        String rol = usuarioActual.getRol() == null ? "" : usuarioActual.getRol().trim().toLowerCase();
        boolean esAdmin = rol.equals("admin") || rol.equals("administrador");
        boolean esSecretaria = rol.equals("secretaría") || rol.equals("secretaria");
        boolean esEstudiante = rol.equals("estudiante");
        boolean esDocente = rol.equals("docente");

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
            btn.setPreferredSize(UIConstants.BUTTON_SIZE);
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.WEST);

        // Área central de contenido
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(UIConstants.PANEL_BG);
        panelContenido.add(new JLabel("Bienvenido al sistema académico", JLabel.CENTER), BorderLayout.CENTER);
        add(panelContenido, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE CONTROL PARA EL CONTROLADOR ---

    // Este método permite al controlador cambiar el panel del centro sin saber cómo se redibuja
    public void cambiarPanelCentro(JPanel nuevoPanel) {
        panelContenido.removeAll();
        panelContenido.add(nuevoPanel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    // Getters para que el controlador pueda escuchar los botones
    public JButton getBtnCursos() { return btnCursos; }
    public JButton getBtnEstudiantes() { return btnEstudiantes; }
    public JButton getBtnMatricula() { return btnMatricula; }
    public JButton getBtnHistorial() { return btnHistorial; }
    public JButton getBtnSolicitudes() { return btnSolicitudes; }
    public JButton getBtnMisMatriculas() { return btnMisMatriculas; }
    public JButton getBtnAtenderSolicitudes() { return btnAtenderSolicitudes; }
    public JButton getBtnSalir() { return btnSalir; }
    
    public Usuario getUsuarioActual() { return usuarioActual; }
}