package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public final class UIConstants {

    public static final Color AZUL_OSCURO = new Color(15, 40, 80);
    public static final Color AZUL_MEDIO = new Color(25, 70, 140);
    public static final Color AZUL_CLARO = new Color(50, 120, 200);

    public static final Color PANEL_BG = new Color(220, 235, 255);
    public static final Color PANEL_LIGHT = new Color(210, 230, 255);
    public static final Color PANEL_FORM = new Color(220, 230, 255);
    public static final Color PANEL_HEADER = new Color(200, 225, 255);

    public static final Color TEXT_DARK = new Color(30, 30, 30);
    public static final Color TEXT_BLUE = new Color(0, 70, 140);

    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Arial", Font.BOLD, 18);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);

    public static final Dimension BUTTON_SIZE = new Dimension(200, 50);

    public static JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(AZUL_MEDIO);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BUTTON);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        return btn;
    }

    public static JPanel crearPanelGradiente() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, AZUL_OSCURO, 0, getHeight(), AZUL_CLARO);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private UIConstants() {
    }
}
