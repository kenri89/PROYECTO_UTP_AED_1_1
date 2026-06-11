package gui;

import com.google.common.base.Strings;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(750, 500); // Más ancho para el diseño dividido
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));
        
        JPanel panelIzquierdo = new JPanel(null); 
        panelIzquierdo.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(30, 30, 30));
        lblTitulo.setBounds(105, 45, 250, 40);
        panelIzquierdo.add(lblTitulo);

        // Campo: Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setBounds(50, 110, 280, 20);
        panelIzquierdo.add(lblUsuario);

        JTextField txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBounds(50, 135, 280, 30);
        // Borde inferior estilo línea (como en la imagen)
        txtUsuario.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK)); 
        panelIzquierdo.add(txtUsuario);

        // Campo: Contraseña
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setBounds(50, 190, 280, 20);
        panelIzquierdo.add(lblPass);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(50, 215, 280, 30);
        txtPassword.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        panelIzquierdo.add(txtPassword);

        // Campo: Rol (Mantenemos la lógica de tus compañeros pero estilizado)
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRol.setBounds(50, 270, 280, 20);
        panelIzquierdo.add(lblRol);

        JComboBox<String> comboSelRol = new JComboBox<>(new String[]{"Administrador", "Estudiante", "Secretaría"});
        comboSelRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboSelRol.setBounds(50, 295, 280, 30);
        comboSelRol.setBackground(Color.WHITE);
        panelIzquierdo.add(comboSelRol);

        // Botón Ingresar
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(25, 70, 140));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setBounds(50, 360, 130, 40);
        panelIzquierdo.add(btnLogin);

        JPanel panelDerecho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 32, 67), 0, getHeight(), new Color(40, 85, 150));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelDerecho.setLayout(new GridBagLayout());
        
        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource("/gui/logo.png"))
                    .getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
            JLabel lblLogo = new JLabel(icon);
            panelDerecho.add(lblLogo);
        } catch (Exception e) {
            // Respaldo elegante si no encuentra la imagen del logo
            JLabel lblLogoTexto = new JLabel("I.E.S Perú - Japón", JLabel.CENTER);
            lblLogoTexto.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblLogoTexto.setForeground(Color.WHITE);
            panelDerecho.add(lblLogoTexto);
        }

        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);
        add(panelPrincipal);

        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String pass = new String(txtPassword.getPassword()).trim();
            String rolSeleccionado = (String) comboSelRol.getSelectedItem();

            if (Strings.isNullOrEmpty(user) || Strings.isNullOrEmpty(pass)) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese usuario y contraseña.");
                return;
            }

            String rol = rolSeleccionado != null ? rolSeleccionado : "Administrador";
            dao.UsuarioDAO usuarioDAO = new dao.UsuarioDAO();
            Usuario usuarioLogueado = usuarioDAO.autenticar(user, pass, rol);

            if (usuarioLogueado != null) {
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