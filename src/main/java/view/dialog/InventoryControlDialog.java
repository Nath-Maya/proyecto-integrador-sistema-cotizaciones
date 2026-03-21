package view.dialog;

import controller.AppController;
import model.MaterialItem;
import view.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;

public class InventoryControlDialog extends JDialog {
    private final AppController controller;
    private DefaultTableModel model;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
    private final NumberFormat integerFormat = NumberFormat.getIntegerInstance(Locale.of("es", "CO"));

    public InventoryControlDialog(Window owner, AppController controller) {
        super(owner, "Control de inventario", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        buildUI();
        cargarMateriales();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Control de inventario");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        root.add(title, BorderLayout.NORTH);

        String[] columns = {"Material", "Ancho", "Largo", "Stock", "Precio por hoja", "Total"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(22);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = UIStyle.createMainButton("Agregar material");
        addBtn.setPreferredSize(new Dimension(180, 36));
        addBtn.addActionListener(e -> abrirAgregarMaterial());
        JButton volverBtn = UIStyle.createMainButton("Volver");
        volverBtn.setPreferredSize(new Dimension(120, 36));
        volverBtn.addActionListener(e -> dispose());
        bottom.add(addBtn);
        bottom.add(volverBtn);

        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);
        setPreferredSize(new Dimension(760, 420));
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void cargarMateriales() {
        model.setRowCount(0);
        List<MaterialItem> materiales = controller.listarMateriales();
        for (MaterialItem m : materiales) {
            model.addRow(new Object[]{
                    m.getMaterial(),
                    m.getAncho(),
                    m.getLargo(),
                    integerFormat.format(m.getStock()),
                    currencyFormat.format(m.getPrecioPorHoja()),
                    currencyFormat.format(m.getTotal())
            });
        }
    }

    private void abrirAgregarMaterial() {
        AddMaterialDialog dialog = new AddMaterialDialog(this, controller);
        dialog.setVisible(true);
        if (dialog.isCreated()) {
            cargarMateriales();
        }
    }
}
