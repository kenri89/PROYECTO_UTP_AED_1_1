package gui;

import dao.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> comboSelRol;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));

        JPanel panelIzquierdo = new JPanel(null);
        panelIzquierdo.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(UIConstants.TEXT_DARK);
        lblTitulo.setBounds(105, 45, 250, 40);
        panelIzquierdo.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setBounds(50, 110, 280, 20);
        panelIzquierdo.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBounds(50, 135, 280, 30);
        txtUsuario.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        panelIzquierdo.add(txtUsuario);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setBounds(50, 190, 280, 20);
        panelIzquierdo.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(50, 215, 280, 30);
        txtPassword.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
        panelIzquierdo.add(txtPassword);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRol.setBounds(50, 270, 280, 20);
        panelIzquierdo.add(lblRol);

        comboSelRol = new JComboBox<>(new String[]{"Administrador", "Estudiante", "Secretaría"});
        comboSelRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboSelRol.setBounds(50, 295, 280, 30);
        comboSelRol.setBackground(Color.WHITE);
        panelIzquierdo.add(comboSelRol);

        btnLogin = new JButton("Ingresar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(UIConstants.AZUL_MEDIO);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setBounds(50, 360, 130, 40);
        panelIzquierdo.add(btnLogin);

        JPanel panelDerecho = UIConstants.crearPanelGradiente();
        panelDerecho.setLayout(new GridBagLayout());

        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(getClass().getResource("/gui/logo.png"))
                    .getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH));
            JLabel lblLogo = new JLabel(icon);
            panelDerecho.add(lblLogo);
        } catch (Exception e) {
            JLabel lblLogoTexto = new JLabel("I.E.S Perú - Japón", JLabel.CENTER);
            lblLogoTexto.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblLogoTexto.setForeground(Color.WHITE);
            panelDerecho.add(lblLogoTexto);
        }

        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);
        add(panelPrincipal);

        btnLogin.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuario = getUsuario();
        String password = getPassword();
        String rol = getRol();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Usuario u = new UsuarioDAO().autenticar(usuario, password, rol);
            if (u != null) {
                dispose();
                new MenuPrincipal(u).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getRol() {
        return (String) comboSelRol.getSelectedItem();
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }
}