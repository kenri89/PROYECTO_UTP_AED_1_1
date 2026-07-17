package gui;

import javax.swing.*;
import java.awt.*;

public class PanelSolicitudesEstudiante extends JPanel {

    private JTextField txtCarnet;
    private JComboBox<String> comboTipo;
    private JTextArea txtDescripcion;
    private JButton btnEnviar;

    public PanelSolicitudesEstudiante() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(UIConstants.PANEL_FORM);
        panelFormulario.setBorder(BorderFactory.createTitledBorder("📄 Nueva Solicitud del Estudiante"));

        JLabel lblCarnet = new JLabel("Carnet del estudiante:");
        lblCarnet.setFont(UIConstants.FONT_BUTTON);
        txtCarnet = new JTextField(20);
        txtCarnet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblTipo = new JLabel("Tipo de solicitud:");
        lblTipo.setFont(UIConstants.FONT_BUTTON);
        comboTipo = new JComboBox<>(new String[]{
                "Certificado de matrícula",
                "Constancia de estudiante activo",
                "Cambio de carrera",
                "Cambio de grupo o curso",
                "Duplicado de carnet",
                "Información adicional"
        });
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(UIConstants.FONT_BUTTON);
        txtDescripcion = new JTextArea(20, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(UIConstants.PANEL_BG);
        btnEnviar = UIConstants.crearBoton("Enviar Solicitud");
        btnEnviar.setPreferredSize(new Dimension(180, 40));
        panelBoton.add(btnEnviar);

        // Añadir componentes al panel formulario
        panelFormulario.add(lblCarnet);
        panelFormulario.add(txtCarnet);
        panelFormulario.add(Box.createVerticalStrut(10));
        panelFormulario.add(lblTipo);
        panelFormulario.add(comboTipo);
        panelFormulario.add(Box.createVerticalStrut(10));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(scrollDescripcion);
        panelFormulario.add(Box.createVerticalStrut(20));
        panelFormulario.add(btnEnviar);

        add(panelFormulario, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE MANIPULACIÓN Y LECTURA DE LA UI ---

    public String getCarnet() {
        return txtCarnet.getText().trim();
    }

    public String getTipoSolicitud() {
        return (String) comboTipo.getSelectedItem();
    }

    public String getDescripcion() {
        return txtDescripcion.getText().trim();
    }

    public void limpiarFormulario() {
        txtCarnet.setText("");
        txtDescripcion.setText("");
        comboTipo.setSelectedIndex(0);
    }

    // --- GETTERS DE COMPONENTES PARA EL CONTROLADOR ---

    public JButton getBtnEnviar() {
        return btnEnviar;
    }
}