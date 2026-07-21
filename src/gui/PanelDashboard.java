package gui;

import modelo.Usuario;
import modelo.Curso;
import estructuras.ListaCursos;
import estructuras.ArregloCursos;
import estructuras.MatrizSemestres;
import estructuras.ArbolEstudiantes;
import estructuras.ListaMatricula;
import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

public class PanelDashboard extends JPanel {

    public PanelDashboard(Usuario usuario, ArbolEstudiantes arbolEstudiantes,
                          ArregloCursos arregloCursos, ListaCursos listaCursos,
                          ListaMatricula listaMatricula, MatrizSemestres matrizSemestres) {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Panel de Control", JLabel.CENTER);
        lblTitulo.setFont(UIConstants.FONT_TITLE);
        lblTitulo.setForeground(UIConstants.TEXT_BLUE);
        add(lblTitulo, BorderLayout.NORTH);

        int totalEstudiantes = contarEstudiantes(arbolEstudiantes);
        int totalCursos = listaCursos.contarCursos();
        int totalMatriculas = listaMatricula.getTamaño();
        int totalSemestres = contarSemestres(arregloCursos);

        JPanel panelCards = new JPanel(new GridLayout(2, 2, 20, 20));
        panelCards.setBackground(UIConstants.PANEL_BG);
        panelCards.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        panelCards.add(crearCard("Estudiantes", String.valueOf(totalEstudiantes), UIConstants.AZUL_OSCURO));
        panelCards.add(crearCard("Cursos", String.valueOf(totalCursos), UIConstants.AZUL_MEDIO));
        panelCards.add(crearCard("Matrículas", String.valueOf(totalMatriculas), UIConstants.AZUL_CLARO));
        panelCards.add(crearCard("Semestres", String.valueOf(totalSemestres), new Color(30, 100, 70)));

        add(panelCards, BorderLayout.CENTER);

        JLabel lblPie = new JLabel(
            "Bienvenido, " + usuario.getUsername() + " | Rol: " + usuario.getRol(),
            JLabel.CENTER);
        lblPie.setFont(UIConstants.FONT_HEADER);
        lblPie.setForeground(UIConstants.TEXT_DARK);
        lblPie.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblPie, BorderLayout.SOUTH);
    }

    private JPanel crearCard(String titulo, String valor, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setPreferredSize(new Dimension(200, 120));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblValor = new JLabel(valor, JLabel.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 48));
        lblValor.setForeground(Color.WHITE);
        card.add(lblValor, BorderLayout.CENTER);

        JLabel lblTit = new JLabel(titulo, JLabel.CENTER);
        lblTit.setFont(UIConstants.FONT_LABEL);
        lblTit.setForeground(new Color(220, 220, 220));
        card.add(lblTit, BorderLayout.SOUTH);

        return card;
    }

    private int contarEstudiantes(ArbolEstudiantes arbol) {
        int[] count = {0};
        try {
            arbol.inorden(e -> count[0]++);
        } catch (Exception ignored) {}
        return count[0];
    }

    private int contarSemestres(ArregloCursos arreglo) {
        java.util.Set<Integer> semestres = new HashSet<>();
        try {
            for (Curso c : arreglo.obtenerCursos()) {
                if (c != null) semestres.add(c.getSemestre());
            }
        } catch (Exception ignored) {}
        return semestres.size();
    }
}
