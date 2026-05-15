package gui;

import estructuras.ArbolEstudiantes;
import modelo.Estudiante;

import javax.swing.*;
import java.awt.*;

public class PanelBusquedaEstudiantes_U4 extends JPanel {

    public PanelBusquedaEstudiantes_U4(ArbolEstudiantes arbol) {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 245, 255));

        JLabel lblTitulo = new JLabel("Búsqueda de Estudiantes", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBuscar = new JPanel(new FlowLayout());
        JLabel lblCarnet = new JLabel("Carnet:");
        JTextField txtCarnet = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");

        panelBuscar.add(lblCarnet);
        panelBuscar.add(txtCarnet);
        panelBuscar.add(btnBuscar);

        add(panelBuscar, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            String carnet = txtCarnet.getText().trim();
            if (carnet.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa un carnet.");
                return;
            }
            Estudiante estudiante = arbol.buscar(carnet);
            if (estudiante != null) {
                JOptionPane.showMessageDialog(this, "Estudiante encontrado:\n\n" +
                        "Carnet: " + estudiante.getCarnet() +
                        "\nNombre: " + estudiante.getNombre() +
                        "\nCarrera: " + estudiante.getCarrera());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún estudiante con ese carnet.");
            }
        });
    }
}
