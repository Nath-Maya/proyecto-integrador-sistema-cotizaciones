package view.dialog;

import controller.AppController;
import view.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddMaterialDialog extends JDialog {
    private final AppController controller;
    private boolean created;

    private JTextField materialF;
    private JTextField anchoF;
    private JTextField largoF;
    private JTextField stockF;
    private JTextField precioF;

    public AddMaterialDialog(Window owner, AppController controller) {
        super(owner, "Agregar material", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
        materialF = new JTextField();
        anchoF = new JTextField();
        largoF = new JTextField();
        stockF = new JTextField();
        precioF = new JTextField();

        form.add(new JLabel("Material:")); form.add(materialF);
        form.add(new JLabel("Ancho:")); form.add(anchoF);
        form.add(new JLabel("Largo:")); form.add(largoF);
        form.add(new JLabel("Stock:")); form.add(stockF);
        form.add(new JLabel("Precio por hoja:")); form.add(precioF);

        root.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarBtn = UIStyle.createMainButton("Guardar");
        guardarBtn.setPreferredSize(new Dimension(120, 36));
        JButton cancelarBtn = UIStyle.createMainButton("Cancelar");
        cancelarBtn.setPreferredSize(new Dimension(120, 36));

        guardarBtn.addActionListener(e -> guardar());
        cancelarBtn.addActionListener(e -> dispose());

        actions.add(guardarBtn);
        actions.add(cancelarBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
        setPreferredSize(new Dimension(420, 380));
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void guardar() {
        try {
            String material = materialF.getText().trim();
            double ancho = Double.parseDouble(anchoF.getText().trim());
            double largo = Double.parseDouble(largoF.getText().trim());
            int stock = Integer.parseInt(stockF.getText().trim());
            double precio = Double.parseDouble(precioF.getText().trim());

            boolean ok = controller.agregarMaterial(material, ancho, largo, stock, precio);
            if (ok) {
                created = true;
                JOptionPane.showMessageDialog(this, "Material agregado correctamente", "Éxito", JOptionPane.PLAIN_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un material con ese nombre", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifica que ancho, largo, stock y precio sean numéricos", "Error", JOptionPane.PLAIN_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public boolean isCreated() {
        return created;
    }
}
