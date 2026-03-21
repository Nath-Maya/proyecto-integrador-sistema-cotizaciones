package view.dialog;

import controller.AppController;
import model.Cliente;
import model.MaterialItem;
import view.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CotizacionDialog extends JDialog {
    private final AppController controller;
    private final Map<String, MaterialItem> materialMap = new HashMap<>();

    private JComboBox<Cliente> clienteCombo;
    private JTextField anchoF;
    private JTextField altoF;
    private JComboBox<String> materialCombo;
    private JComboBox<String> productoCombo;
    private JComboBox<String> tipoImpresionCombo;
    private JComboBox<String> colorCombo;
    private DefaultTableModel resumenModel;
    private JLabel precioUnidadLabel;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
    private final NumberFormat integerFormat = NumberFormat.getIntegerInstance(Locale.of("es", "CO"));

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

        DefaultTableModel clienteModel = new DefaultTableModel(new String[]{"Documento", "Correo", "Teléfono", "Dirección"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable clienteTable = new JTable(clienteModel);
        clienteTable.setRowHeight(22);
        JScrollPane tableScroll = new JScrollPane(clienteTable);
        tableScroll.setPreferredSize(new Dimension(420, 70));

        Runnable refreshClienteTable = () -> {
            clienteModel.setRowCount(0);
            Cliente c = (Cliente) clienteCombo.getSelectedItem();
            if (c != null) {
                clienteModel.addRow(new Object[]{c.getDocumento(), c.getCorreo(), c.getTelefono(), c.getDireccion()});
            }
        };
        clienteCombo.addActionListener(e -> refreshClienteTable.run());
        refreshClienteTable.run();

        anchoF = new JTextField();
        altoF = new JTextField();

        productoCombo = new JComboBox<>(new String[]{
                "Flyers",
                "Brochures",
                "Tarjetas de presentacion",
                "Postales",
                "Posters"
        });

        materialCombo = new JComboBox<>();
        for (MaterialItem m : controller.listarMateriales()) {
            materialCombo.addItem(m.getMaterial());
            materialMap.put(m.getMaterial(), m);
        }

        tipoImpresionCombo = new JComboBox<>(new String[]{"Offset", "Digital"});
        colorCombo = new JComboBox<>(new String[]{"4/0 CMYK", "4/4 CMYK"});

        p.add(new JLabel("Cliente:")); p.add(clienteCombo);
        p.add(new JLabel("Datos del cliente:")); p.add(tableScroll);

        JPanel productoDimensionPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        JPanel productoPanel = new JPanel(new GridLayout(0, 1, 0, 4));
        JPanel dimensionPanel = new JPanel(new GridLayout(2, 2, 6, 4));

        productoPanel.add(new JLabel("Producto:"));
        productoPanel.add(productoCombo);

        dimensionPanel.add(new JLabel("Ancho:"));
        dimensionPanel.add(anchoF);
        dimensionPanel.add(new JLabel("Largo:"));
        dimensionPanel.add(altoF);

        productoDimensionPanel.add(productoPanel);
        productoDimensionPanel.add(dimensionPanel);

        p.add(new JLabel("Producto y dimensiones:")); p.add(productoDimensionPanel);

        JPanel filaTresPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        JPanel materialPanel = new JPanel(new GridLayout(0, 1, 0, 4));
        JPanel impresionPanel = new JPanel(new GridLayout(0, 1, 0, 4));
        JPanel colorPanel = new JPanel(new GridLayout(0, 1, 0, 4));

        materialPanel.add(new JLabel("Material:"));
        materialPanel.add(materialCombo);

        impresionPanel.add(new JLabel("Tipo de impresión:"));
        impresionPanel.add(tipoImpresionCombo);

        colorPanel.add(new JLabel("Color:"));
        colorPanel.add(colorCombo);

        filaTresPanel.add(materialPanel);
        filaTresPanel.add(impresionPanel);
        filaTresPanel.add(colorPanel);

        p.add(new JLabel("Configuración de impresión:")); p.add(filaTresPanel);

        resumenModel = new DefaultTableModel(new String[]{
            "Cantidad",
            "Imposiciones",
            "Cantidad impresiones",
            "Cantidad hojas material",
            "Costo impresión",
            "Costo material",
            "Subtotal",
            "Utilidad (30%)",
            "Total"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
            return row == 0 && column == 0;
            }
        };
        resumenModel.addRow(new Object[]{"1", "-", "-", "-", "-", "-", "-", "-", "-"});

        JTable resumenTable = new JTable(resumenModel);
        resumenTable.setRowHeight(22);
        JScrollPane resumenScroll = new JScrollPane(resumenTable);
        resumenScroll.setPreferredSize(new Dimension(880, 90));

        p.add(new JLabel("Resumen de cálculo:"));
        p.add(resumenScroll);

        JPanel precioUnidadPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        precioUnidadPanel.setOpaque(false);
        JLabel precioUnidadTextLabel = new JLabel("Precio por unidad: ");
        precioUnidadTextLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        precioUnidadTextLabel.setForeground(new Color(0, 153, 255));
        precioUnidadLabel = new JLabel("-");
        precioUnidadLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        precioUnidadLabel.setForeground(new Color(0, 255, 102));
        precioUnidadPanel.add(precioUnidadTextLabel);
        precioUnidadPanel.add(precioUnidadLabel);

        p.add(precioUnidadPanel);

        root.add(p, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton calcularBtn = UIStyle.createMainButton("Calcular");
        calcularBtn.setPreferredSize(new Dimension(120, 36));
        JButton generarBtn = UIStyle.createMainButton("Generar");
        generarBtn.setPreferredSize(new Dimension(120, 36));
        JButton cancelarBtn = UIStyle.createMainButton("Cancelar");
        cancelarBtn.setPreferredSize(new Dimension(120, 36));

        calcularBtn.addActionListener(e -> actualizarResumen(false));
        generarBtn.addActionListener(e -> generarCotizacion());
        cancelarBtn.addActionListener(e -> dispose());

        actions.add(calcularBtn);
        actions.add(generarBtn);
        actions.add(cancelarBtn);
        root.add(actions, BorderLayout.SOUTH);

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { actualizarResumen(true); }
            @Override
            public void removeUpdate(DocumentEvent e) { actualizarResumen(true); }
            @Override
            public void changedUpdate(DocumentEvent e) { actualizarResumen(true); }
        };
        anchoF.getDocument().addDocumentListener(dl);
        altoF.getDocument().addDocumentListener(dl);

        materialCombo.addActionListener(e -> actualizarResumen(true));
        tipoImpresionCombo.addActionListener(e -> actualizarResumen(true));
        colorCombo.addActionListener(e -> actualizarResumen(true));

        setContentPane(root);
        setPreferredSize(new Dimension(1040, 640));
        pack();
        setLocationRelativeTo(getOwner());
        getRootPane().setDefaultButton(calcularBtn);

        actualizarResumen(true);
    }

    private void generarCotizacion() {
        ResultadoCalculo r = calcular(false);
        if (r == null) {
            return;
        }

        Cliente seleccionado = (Cliente) clienteCombo.getSelectedItem();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(getOwner(), "Debes seleccionar un cliente", "Validación", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        String material = (String) materialCombo.getSelectedItem();
        String producto = (String) productoCombo.getSelectedItem();
        String tipoImpresion = (String) tipoImpresionCombo.getSelectedItem();
        String color = (String) colorCombo.getSelectedItem();
        String tipo = producto + " | " + tipoImpresion + " | " + color;

        controller.cotizar(r.anchoProducto, r.largoProducto, material, tipo);

        JOptionPane.showMessageDialog(
                getOwner(),
                "Cotización para " + seleccionado.getNombre() + "\n" +
                        "Total: " + currencyFormat.format(r.total) + "\n" +
                        "Precio por unidad: " + currencyFormat.format(r.precioUnidad),
                "Resultado",
                JOptionPane.PLAIN_MESSAGE
        );
        dispose();
    }

    private void actualizarResumen(boolean silencioso) {
        ResultadoCalculo r = calcular(silencioso);
        if (r == null) {
            setResumenVacio();
            return;
        }

        resumenModel.setValueAt(integerFormat.format(r.cantidad), 0, 0);
        resumenModel.setValueAt(integerFormat.format(r.imposiciones), 0, 1);
        resumenModel.setValueAt(integerFormat.format(r.cantidadImpresiones), 0, 2);
        resumenModel.setValueAt(integerFormat.format(r.cantidadHojas), 0, 3);
        resumenModel.setValueAt(currencyFormat.format(r.costoImpresion), 0, 4);
        resumenModel.setValueAt(currencyFormat.format(r.costoMaterial), 0, 5);
        resumenModel.setValueAt(currencyFormat.format(r.subtotal), 0, 6);
        resumenModel.setValueAt(currencyFormat.format(r.utilidad), 0, 7);
        resumenModel.setValueAt(currencyFormat.format(r.total), 0, 8);
        precioUnidadLabel.setText(currencyFormat.format(r.precioUnidad));
    }

    private void setResumenVacio() {
        resumenModel.setValueAt("-", 0, 1);
        resumenModel.setValueAt("-", 0, 2);
        resumenModel.setValueAt("-", 0, 3);
        resumenModel.setValueAt("-", 0, 4);
        resumenModel.setValueAt("-", 0, 5);
        resumenModel.setValueAt("-", 0, 6);
        resumenModel.setValueAt("-", 0, 7);
        resumenModel.setValueAt("-", 0, 8);
        precioUnidadLabel.setText("-");
    }

    private ResultadoCalculo calcular(boolean silencioso) {
        int cantidad;
        try {
            cantidad = parseCantidad();
        } catch (IllegalArgumentException ex) {
            if (!silencioso) {
                JOptionPane.showMessageDialog(getOwner(), ex.getMessage(), "Validación", JOptionPane.PLAIN_MESSAGE);
            }
            return null;
        }

        double anchoProducto;
        double largoProducto;
        try {
            anchoProducto = parseDoublePositivo(anchoF.getText().trim(), "Ancho");
            largoProducto = parseDoublePositivo(altoF.getText().trim(), "Largo");
        } catch (IllegalArgumentException ex) {
            if (!silencioso) {
                JOptionPane.showMessageDialog(getOwner(), ex.getMessage(), "Validación", JOptionPane.PLAIN_MESSAGE);
            }
            return null;
        }

        String materialNombre = (String) materialCombo.getSelectedItem();
        if (materialNombre == null || materialNombre.isBlank()) {
            if (!silencioso) {
                JOptionPane.showMessageDialog(getOwner(), "Debes seleccionar un material", "Validación", JOptionPane.PLAIN_MESSAGE);
            }
            return null;
        }

        MaterialItem material = materialMap.get(materialNombre);
        if (material == null) {
            if (!silencioso) {
                JOptionPane.showMessageDialog(getOwner(), "No se encontró el material seleccionado", "Validación", JOptionPane.PLAIN_MESSAGE);
            }
            return null;
        }

        int imposicionesAncho = (int) Math.floor(material.getAncho() / anchoProducto);
        int imposicionesLargo = (int) Math.floor(material.getLargo() / largoProducto);
        int imposiciones = imposicionesAncho * imposicionesLargo;

        if (imposiciones <= 0) {
            if (!silencioso) {
                JOptionPane.showMessageDialog(getOwner(), "Las dimensiones del producto no permiten imposición en el pliego", "Validación", JOptionPane.PLAIN_MESSAGE);
            }
            return null;
        }

        int cantidadHojas = ceilDiv(cantidad, imposiciones);
        int factorColor = "4/4 CMYK".equals(colorCombo.getSelectedItem()) ? 2 : 1;
        int cantidadImpresiones = cantidadHojas * factorColor;

        String tipoImpresion = (String) tipoImpresionCombo.getSelectedItem();
        double costoImpresion;
        if ("Offset".equals(tipoImpresion)) {
            costoImpresion = (cantidadImpresiones / 3000.0) * 45000.0;
        } else {
            costoImpresion = cantidadImpresiones * 250.0;
        }

        double costoMaterial = cantidadHojas * material.getPrecioPorHoja();
        double subtotal = costoImpresion + costoMaterial;
        double utilidad = subtotal * 0.30;
        double total = subtotal + utilidad;
        double precioUnidad = total / cantidad;

        ResultadoCalculo r = new ResultadoCalculo();
        r.cantidad = cantidad;
        r.anchoProducto = anchoProducto;
        r.largoProducto = largoProducto;
        r.imposiciones = imposiciones;
        r.cantidadImpresiones = cantidadImpresiones;
        r.cantidadHojas = cantidadHojas;
        r.costoImpresion = costoImpresion;
        r.costoMaterial = costoMaterial;
        r.subtotal = subtotal;
        r.utilidad = utilidad;
        r.total = total;
        r.precioUnidad = precioUnidad;
        return r;
    }

    private int parseCantidad() {
        Object raw = resumenModel.getValueAt(0, 0);
        String texto = raw == null ? "" : raw.toString().trim();
        if (!texto.matches("\\d+")) {
            throw new IllegalArgumentException("La cantidad debe ser un número entero positivo");
        }
        int cantidad = Integer.parseInt(texto);
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        return cantidad;
    }

    private double parseDoublePositivo(String txt, String campo) {
        if (txt == null || txt.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        double val;
        try {
            val = Double.parseDouble(txt);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(campo + " debe ser numérico");
        }
        if (val <= 0) {
            throw new IllegalArgumentException(campo + " debe ser mayor que cero");
        }
        return val;
    }

    private int ceilDiv(int a, int b) {
        return (a + b - 1) / b;
    }

    private static class ResultadoCalculo {
        int cantidad;
        double anchoProducto;
        double largoProducto;
        int imposiciones;
        int cantidadImpresiones;
        int cantidadHojas;
        double costoImpresion;
        double costoMaterial;
        double subtotal;
        double utilidad;
        double total;
        double precioUnidad;
    }
}
