package gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main(String[] args) {
        // Hace que la interfaz se adapte elegantemente al estilo de Windows
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al establecer el Look and Feel", e);
        }

        // Lanza y muestra la pantalla de Login de inmediato
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}