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

    // Tema: Unidad 1 - Arreglo de Objetos (Mantener por consistencia académica)
    private Usuario[] usuariosRegistrados = {
        new Usuario("admin", "1234", "Administrador"),
        new Usuario("secretaria", "1234", "Secretaría"),
        new Usuario("estudiante", "1234", "Estudiante", "2024001")
    };

    public LoginFrame() {
        setTitle("Login - Sistema de Gestión Académica");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(220, 235, 255));
        setLayout(new BorderLayout());

        // =============== Panel Título ===============
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
            String pass = new String(txtPassword.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese usuario y contraseña.");
                return;
            }

            // 1. Intentar validar como Administrador / Personal en la Base de Datos
            dao.UsuarioDAO usuarioDAO = new dao.UsuarioDAO();
            boolean accesoPermitido = usuarioDAO.validarUsuario(user, pass);
            Usuario usuarioLogueado = null;

            if (accesoPermitido) {
                // Instanciamos el usuario con el rol correspondiente obtenido de la BD (por defecto Administrador)
                usuarioLogueado = new Usuario(user, pass, "Administrador");
            } 
            // 2. Si no está en la BD, verificar si es un Estudiante en el archivo plano txt
            else if (CuentasEstudiantes.autenticar(user, pass)) {
                accesoPermitido = true;
                usuarioLogueado = new Usuario(user, pass, "Estudiante", user);
            }

            // 3. Evaluar el resultado del acceso
            if (accesoPermitido && usuarioLogueado != null) {
                JOptionPane.showMessageDialog(this, "¡Bienvenido al sistema como " + usuarioLogueado.getRol() + "!");
                
                // Abrir la ventana principal pasando el usuario autenticado
                MenuPrincipal ventana = new MenuPrincipal(usuarioLogueado);
                ventana.setVisible(true);
                this.dispose(); // Cierra la ventana de login
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de acceso", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}