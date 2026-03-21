package view.dialog;

import controller.AppController;
import model.Cliente;
import view.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CotizacionDialog extends JDialog {
    private final AppController controller;

    private JComboBox<Cliente> clienteCombo;
    private DefaultTableModel model;
    private JTextField anchoF;
    private JTextField altoF;
    private JTextField materialF;
    private JTextField tipoF;

    public CotizacionDialog(Window owner, AppController controller) {
        super(owner, "Generar cotización", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        List<Cliente> clientes = controller.listarClientes();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(getOwner(), "No hay clientes registrados. Registra un cliente primero.", "Información", JOptionPane.PLAIN_MESSAGE);
            dispose();
            return;
        }

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel p = new JPanel(new GridLayout(0, 1, 0, 6));

        clienteCombo = new JComboBox<>(clientes.toArray(new Cliente[0]));
        clienteCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    lbl.setText(((Cliente) value).getNombre());
                }
                return lbl;
            }
        });

        String[] columns = {"Documento", "Correo", "Teléfono", "Dirección"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable clienteTable = new JTable(model);
        clienteTable.setRowHeight(22);
        JScrollPane tableScroll = new JScrollPane(clienteTable);
        tableScroll.setPreferredSize(new Dimension(420, 70));

        Runnable refreshTable = () -> {
            model.setRowCount(0);
            Cliente c = (Cliente) clienteCombo.getSelectedItem();
            if (c != null) {
                model.addRow(new Object[]{c.getDocumento(), c.getCorreo(), c.getTelefono(), c.getDireccion()});
            }
        };
        clienteCombo.addActionListener(e -> refreshTable.run());
        refreshTable.run();

        JTextField anchoF = new JTextField();
        JTextField altoF = new JTextField();
        JTextField materialF = new JTextField();
        JTextField tipoF = new JTextField();

        this.anchoF = anchoF;
        this.altoF = altoF;
        this.materialF = materialF;
        this.tipoF = tipoF;

        p.add(new JLabel("Cliente:")); p.add(clienteCombo);
        p.add(new JLabel("Datos del cliente:")); p.add(tableScroll);
        p.add(new JLabel("Ancho:")); p.add(anchoF);
        p.add(new JLabel("Alto:")); p.add(altoF);
        p.add(new JLabel("Material:")); p.add(materialF);
        p.add(new JLabel("Tipo de impresión:")); p.add(tipoF);
        root.add(p, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generarBtn = UIStyle.createMainButton("Generar");
        generarBtn.setPreferredSize(new Dimension(120, 36));
        JButton cancelarBtn = UIStyle.createMainButton("Cancelar");
        cancelarBtn.setPreferredSize(new Dimension(120, 36));

        generarBtn.addActionListener(e -> generarCotizacion());
        cancelarBtn.addActionListener(e -> dispose());

        actions.add(generarBtn);
        actions.add(cancelarBtn);
        root.add(actions, BorderLayout.SOUTH);

        setContentPane(root);
        setPreferredSize(new Dimension(500, 450));
        pack();
        setLocationRelativeTo(getOwner());
        getRootPane().setDefaultButton(generarBtn);
    }

    private void generarCotizacion() {
        try {
            Cliente seleccionado = (Cliente) clienteCombo.getSelectedItem();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(getOwner(), "Debes seleccionar un cliente", "Validación", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            double ancho = Double.parseDouble(anchoF.getText().trim());
            double alto = Double.parseDouble(altoF.getText().trim());
            String material = materialF.getText().trim();
            String tipo = tipoF.getText().trim();

            double costo = controller.cotizar(ancho, alto, material, tipo);
            JOptionPane.showMessageDialog(getOwner(), "Cotización para " + seleccionado.getNombre() + ": " + costo, "Resultado", JOptionPane.PLAIN_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(getOwner(), "Ancho y alto deben ser números", "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
