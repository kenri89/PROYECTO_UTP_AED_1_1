package gui;

import com.google.common.base.Strings;
import modelo.Solicitud;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

public class PanelSolicitudesEstudiante extends JPanel {

    private JTextField txtCarnet;
    private JComboBox<String> comboTipo;
    private JTextArea txtDescripcion;
    private Queue<Solicitud> colaSolicitudes;

    public PanelSolicitudesEstudiante(Queue<Solicitud> colaSolicitudes) {
        this.colaSolicitudes = colaSolicitudes;

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(225, 240, 255));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // margen general

        // Panel del formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(new Color(245, 250, 255));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("📄 Nueva Solicitud del Estudiante"));

        // Carnet
        JLabel lblCarnet = new JLabel("Carnet del estudiante:");
        lblCarnet.setFont(new Font("Arial", Font.BOLD, 14));
        txtCarnet = new JTextField(20);
        txtCarnet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Tipo de solicitud
        JLabel lblTipo = new JLabel("Tipo de solicitud:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 14));
        comboTipo = new JComboBox<>(new String[]{
                "Certificado de matrícula",
                "Constancia de estudiante activo",
                "Cambio de carrera",
                "Cambio de grupo o curso",
                "Duplicado de carnet",
                "Información adicional"
        });
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Arial", Font.BOLD, 14));
        txtDescripcion = new JTextArea(20, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Botón enviar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(230, 245, 255)); // mismo fondo que el panel principal
        JButton btnEnviar = new JButton("Enviar Solicitud");
        btnEnviar.setBackground(new Color(40, 160, 100)); // color verde personalizado
        btnEnviar.setForeground(Color.BLACK);
        btnEnviar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnviar.setPreferredSize(new Dimension(180, 40)); // tamaño uniforme
        panelBoton.add(btnEnviar);

        // Añadir componentes al panel
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

        // Acción del botón
        btnEnviar.addActionListener(e -> enviarSolicitud());
    }

    private void enviarSolicitud() {
        String carnet = txtCarnet.getText().trim();
        String tipo = (String) comboTipo.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();

        if (Strings.isNullOrEmpty(carnet) || Strings.isNullOrEmpty(descripcion)) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor completa todos los campos.");
            return;
        }

        Solicitud solicitud = new Solicitud(carnet, tipo, descripcion);
        colaSolicitudes.offer(solicitud);

        JOptionPane.showMessageDialog(this,
                "✅ Solicitud enviada correctamente.\n\n" +
                        "📌 Resumen:\n" +
                        "Carnet: " + carnet + "\n" +
                        "Tipo: " + tipo + "\n" +
                        "Fecha: " + solicitud.getFechaFormateada());

        txtCarnet.setText("");
        txtDescripcion.setText("");
        comboTipo.setSelectedIndex(0);
    }
}
