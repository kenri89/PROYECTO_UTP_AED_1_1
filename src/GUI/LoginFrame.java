// ================================
// CLASE LoginFrame
// Tema: Unidad 1 - Arreglo de Objetos + GUI con Java Swing
// Uso: Autenticación de usuario, muestra ventana principal según rol
// ================================
package gui;

import modelo.Usuario;
import util.CuentasEstudiantes;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    // Tema: Unidad 1 - Arreglo de Objetos
    private Usuario[] usuariosRegistrados = {
        new Usuario("admin", "1234", "Administrador"),
        new Usuario("secretaria", "1234", "Secretaría"),
        // Mismo carnet debe existir en "Gestión de Estudiantes" para matricularlo.
        new Usuario("estudiante", "1234", "Estudiante", "2024001")
    };

    public LoginFrame() {
        setTitle("Login - Sistema de Gestión Académica");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(220, 235, 255));
        setLayout(new BorderLayout());

        // =============== Panel Título con imagen (si deseas agregar) ===============
        JLabel lblTitulo = new JLabel("Inicio de Sesión", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 70, 140));
        add(lblTitulo, BorderLayout.NORTH);

        // =============== Panel central (formulario) ===============
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

        // =============== Panel inferior (botón login) ===============
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(220, 235, 255));
        panelBoton.add(btnLogin);
        add(panelBoton, BorderLayout.SOUTH);

        // =============== Acción del botón login ===============
        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String pass = new String(txtPassword.getPassword());

            boolean accesoPermitido = false;
            Usuario usuarioLogueado = null;

            // Tema: Unidad 1 - Búsqueda en arreglo de objetos
            for (Usuario u : usuariosRegistrados) {
                if (u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                    accesoPermitido = true;
                    usuarioLogueado = u;
                    break;
                }
            }

            // Estudiantes matriculados: usuario = carnet, contraseña 1234 (archivo cuentas_estudiantes.txt)
            if (!accesoPermitido && CuentasEstudiantes.autenticar(user, pass)) {
                accesoPermitido = true;
                usuarioLogueado = new Usuario(user, pass, "Estudiante", user);
            }

            if (accesoPermitido) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + usuarioLogueado.getRol());
                // Abrir la ventana principal con el usuario y rol
                MenuPrincipal ventana = new MenuPrincipal(usuarioLogueado);
                ventana.setVisible(true);
                this.dispose(); // Cierra login
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }
        });
    }
}
