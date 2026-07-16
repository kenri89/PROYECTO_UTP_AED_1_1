
package Controlador;

import com.google.common.base.Strings;
import Controlador.MenuPrincipalControlador;
import dao.IUsuarioDAO;
import gui.LoginFrame;
import gui.MenuPrincipal;
import javax.swing.JOptionPane;
import modelo.Usuario;

public class LoginController {
    // Instancias del Modelo/DAO y de la Vista
    private LoginFrame vista;
    private IUsuarioDAO usuarioDAO;

    public LoginController(LoginFrame vista, IUsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;

        // El controlador le asigna el evento al botón de la vista
        this.vista.getBtnLogin().addActionListener(e -> procesarLogin());
    }

    private void procesarLogin() {
        String user = vista.getUsuario();
        String pass = vista.getPassword();
        String rolSeleccionado = vista.getRol();

        if (Strings.isNullOrEmpty(user) || Strings.isNullOrEmpty(pass)) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese usuario y contraseña.");
            return;
        }

        String rol = rolSeleccionado != null ? rolSeleccionado : "Administrador";
        Usuario usuarioLogueado = usuarioDAO.autenticar(user, pass, rol);

        if (usuarioLogueado != null) {
            JOptionPane.showMessageDialog(vista, "¡Bienvenido al sistema como " + usuarioLogueado.getRol() + "!");
            new MenuPrincipalControlador(new MenuPrincipal(usuarioLogueado));
        
            vista.dispose(); // Cierra la pantalla de login
        } else {
            JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
