package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public final class UIConstants {

    public static final Color PANEL_BG = new Color(230, 240, 255);
    public static final Color PANEL_LIGHT = new Color(220, 235, 255);
    public static final Color PANEL_HEADER = new Color(200, 225, 255);
    public static final Color BUTTON_BLUE = new Color(0, 120, 215);
    public static final Color TEXT_BLUE = new Color(0, 70, 140);
    public static final Color TEXT_DARK_BLUE = new Color(0, 80, 160);

    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Arial", Font.BOLD, 18);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);

    public static final Dimension BUTTON_SIZE = new Dimension(200, 50);

    private UIConstants() {
    }
}
