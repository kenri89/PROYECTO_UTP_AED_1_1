package gui;

import com.google.common.base.Strings;
import modelo.Usuario;
import util.CuentasEstudiantes;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login - Sistema de Gestión Académica");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(220, 235, 255));
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Inicio de Sesión", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 70, 140));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panelForm.setBackground(new Color(230, 240, 255));

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblPass = new JLabel("Contraseña:");
        JTextField txtUsuario = new JTextField();
        JPasswordField txtPassword = new JPasswordField();

        panelForm.add(lblUsuario);
        panelForm.add(txtUsuario);
        panelForm.add(lblPass);
        panelForm.add(txtPassword);

        add(panelForm, BorderLayout.CENTER);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(220, 235, 255));
        panelBoton.add(btnLogin);
        add(panelBoton, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String pass = new String(txtPassword.getPassword()).trim();

            if (Strings.isNullOrEmpty(user) || Strings.isNullOrEmpty(pass)) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese usuario y contraseña.");
                return;
            }

            dao.UsuarioDAO usuarioDAO = new dao.UsuarioDAO();
            boolean accesoPermitido = usuarioDAO.validarUsuario(user, pass);
            Usuario usuarioLogueado = null;

            if (accesoPermitido) {
                usuarioLogueado = new Usuario(user, pass, "Administrador");
            } else if (CuentasEstudiantes.autenticar(user, pass)) {
                accesoPermitido = true;
                usuarioLogueado = new Usuario(user, pass, "Estudiante", user);
            }

            if (accesoPermitido && usuarioLogueado != null) {
                JOptionPane.showMessageDialog(this, "¡Bienvenido al sistema como " + usuarioLogueado.getRol() + "!");
                MenuPrincipal ventana = new MenuPrincipal(usuarioLogueado);
                ventana.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
