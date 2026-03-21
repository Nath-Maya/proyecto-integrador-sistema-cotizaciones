package view.dialog;

import controller.AppController;
import view.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClienteFormDialog extends JDialog {
    private final AppController controller;
    private boolean saved;

    private JTextField documentoF;
    private JTextField nombreF;
    private JTextField correoF;
    private JTextField telefonoF;
    private JTextField direccionF;

    public ClienteFormDialog(Window owner, AppController controller) {
        super(owner, "Registrar cliente", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel p = new JPanel(new GridLayout(0, 1, 0, 6));

        documentoF = new JTextField();
        nombreF = new JTextField();
        correoF = new JTextField();
        telefonoF = new JTextField();
        direccionF = new JTextField();

        p.add(new JLabel("Documento:")); p.add(documentoF);
        p.add(new JLabel("Nombre cliente:")); p.add(nombreF);
        p.add(new JLabel("Correo:")); p.add(correoF);
        p.add(new JLabel("Telefono:")); p.add(telefonoF);
        p.add(new JLabel("Direccion:")); p.add(direccionF);
        root.add(p, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = UIStyle.createMainButton("Guardar");
        guardarBtn.setPreferredSize(new Dimension(120, 36));
        JButton cancelarBtn = UIStyle.createMainButton("Cancelar");
        cancelarBtn.setPreferredSize(new Dimension(120, 36));

        guardarBtn.addActionListener(e -> guardarCliente());
        cancelarBtn.addActionListener(e -> dispose());

        actions.add(guardarBtn);
        actions.add(cancelarBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
        setPreferredSize(new Dimension(420, 360));
        pack();
        setLocationRelativeTo(getOwner());
        getRootPane().setDefaultButton(guardarBtn);
    }

    public boolean isSaved() {
        return saved;
    }

    private void guardarCliente() {
        try {
            boolean ok = controller.registrarCliente(
                    documentoF.getText().trim(),
                    nombreF.getText().trim(),
                    correoF.getText().trim(),
                    telefonoF.getText().trim(),
                    direccionF.getText().trim()
            );

            if (ok) {
                JOptionPane.showMessageDialog(getOwner(), "Cliente registrado correctamente", "Éxito", JOptionPane.PLAIN_MESSAGE);
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(getOwner(), "Ya existe un cliente con ese documento", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(getOwner(), ex.getMessage(), "Validación", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
