package gui;

import Controlador.LoginController;
import dao.UsuarioDAO;
import java.awt.Color;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Iniciando Sistema de Gestión Académica - PROYECTO_UTP_AED_1");
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
            LOGGER.debug("Look and Feel configurado: Metal (cross-platform)");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            LOGGER.warn("Error al establecer Metal LAF", e);
        }
        javax.swing.UIManager.put("Button.foreground", Color.WHITE);
        javax.swing.UIManager.put("Button.background", UIConstants.AZUL_MEDIO);
        javax.swing.UIManager.put("Button.select", UIConstants.AZUL_OSCURO);
        java.awt.EventQueue.invokeLater(() -> {
            LOGGER.info("Ventana de login abierta");
            new LoginController(new LoginFrame(), new UsuarioDAO());
        });
    }
}
