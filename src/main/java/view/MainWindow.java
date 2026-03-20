package view;

import controller.AppController;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {

    private AppController controller = new AppController();
    private JPanel buttonsContainer;
    private JLabel screenTitleLabel;
    private JLabel smallLogoLabel;

    public MainWindow() {
        setTitle("Sistema de Cotizaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.setBackground(Color.BLACK);

        // Top bar: small logo (5x5) + screen title, grey background
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        topBar.setBackground(new Color(200,200,200));
        topBar.setPreferredSize(new Dimension(getWidth(), 28));
        JLabel smallLogo = createSmallLogoLabel();
        topBar.add(smallLogo);
        JLabel screenTitle = new JLabel("Inicio");
        screenTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        topBar.add(screenTitle);
        cp.add(topBar, BorderLayout.NORTH);

        // Center panel: logo centered and buttons in column
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        // Spacer top
        center.add(Box.createVerticalStrut(20));

        JLabel logoLabel = createLogoLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(logoLabel);

        // add space between logo and buttons
        center.add(Box.createVerticalStrut(40));

        // (title removed - window already has title)

        // Buttons container (will be rebuilt according to login state)
        buttonsContainer = new JPanel();
        buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.Y_AXIS));
        buttonsContainer.setOpaque(false);
        buttonsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(buttonsContainer);

        // add center to content pane
        cp.add(center, BorderLayout.CENTER);

        // Build initial buttons and set screen title reference
        this.screenTitleLabel = screenTitle;
        this.smallLogoLabel = smallLogo;
        rebuildButtons();

        setVisible(true);
    }

    private void rebuildButtons() {
        buttonsContainer.removeAll();

        Color confirmGreen = new Color(0x00, 0xFF, 0x66);
        Color neutral = new Color(70,70,70);
        Color negativeRed = new Color(0xFF, 0x44, 0x44);

        if (!controller.isAppLoggedIn()) {
            // When not logged in: show green "Iniciar sesión" and red "Salir"
            JButton iniciarBtn = styledButtonWithBorder("✅ Iniciar sesión", Color.BLACK, Color.WHITE);
            iniciarBtn.addActionListener(e -> onAppLogin());
            JButton salirBtn = styledButtonWithBorder("⛔ Salir", Color.BLACK, Color.WHITE);
            salirBtn.addActionListener(e -> System.exit(0));

            buttonsContainer.add(iniciarBtn);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(salirBtn);
            screenTitleLabel.setText("Inicio");
        } else {
            // After login: show Registrar cliente and Generar cotización in black with white borders
            JButton registrarCliente = styledButtonWithBorder("📝 Registrar cliente", Color.BLACK, Color.WHITE);
            registrarCliente.setForeground(Color.WHITE);
            registrarCliente.addActionListener(e -> onRegistrar());

            JButton generarCot = styledButtonWithBorder("💲 Generar cotización", Color.BLACK, Color.WHITE);
            generarCot.setForeground(Color.WHITE);
            generarCot.addActionListener(e -> onGenerarCotizacion());

            JButton salirBtn = styledButtonWithBorder("⛔ Salir", Color.BLACK, Color.WHITE);
            salirBtn.addActionListener(e -> System.exit(0));

            buttonsContainer.add(registrarCliente);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(generarCot);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(salirBtn);
            screenTitleLabel.setText("Principal");
        }

        buttonsContainer.revalidate();
        buttonsContainer.repaint();
    }

    private void onRegistrar() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JTextField documentoF = new JTextField();
        JTextField nombreF = new JTextField();
        JTextField correoF = new JTextField();
        JTextField telefonoF = new JTextField();
        JTextField direccionF = new JTextField();

        p.add(new JLabel("Documento:")); p.add(documentoF);
        p.add(new JLabel("Nombre cliente:")); p.add(nombreF);
        p.add(new JLabel("Correo:")); p.add(correoF);
        p.add(new JLabel("Telefono:")); p.add(telefonoF);
        p.add(new JLabel("Direccion:")); p.add(direccionF);

        int res = JOptionPane.showConfirmDialog(this, p, "Registrar cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                boolean ok = controller.registrarCliente(
                        documentoF.getText().trim(),
                        nombreF.getText().trim(),
                        correoF.getText().trim(),
                        telefonoF.getText().trim(),
                        direccionF.getText().trim()
                );

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cliente registrado correctamente", "Éxito", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese documento", "Error", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private void onAppLogin() {
        JPanel p = new JPanel(new GridLayout(0,1));
        p.setBackground(Color.BLACK);
        JTextField userF = new JTextField();
        JPasswordField passF = new JPasswordField();

        JLabel userLbl = new JLabel("Usuario:");
        userLbl.setForeground(Color.WHITE);
        JLabel passLbl = new JLabel("Clave:");
        passLbl.setForeground(Color.WHITE);

        userF.setBackground(new Color(30,30,30)); userF.setForeground(Color.WHITE);
        passF.setBackground(new Color(30,30,30)); passF.setForeground(Color.WHITE);

        p.add(userLbl); p.add(userF);
        p.add(passLbl); p.add(passF);

        Object[] options = {"Acceder", "Cancelar"};
        int res = JOptionPane.showOptionDialog(this, p, "Ingreso", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (res == JOptionPane.YES_OPTION) {
            boolean ok = controller.appLogin(userF.getText().trim(), new String(passF.getPassword()));
            if (ok) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + controller.getAppUser(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                rebuildButtons();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onGenerarCotizacion() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JTextField anchoF = new JTextField();
        JTextField altoF = new JTextField();
        JTextField materialF = new JTextField();
        JTextField tipoF = new JTextField();
        JTextField nitF = new JTextField();

        p.add(new JLabel("Ancho:")); p.add(anchoF);
        p.add(new JLabel("Alto:")); p.add(altoF);
        p.add(new JLabel("Material:")); p.add(materialF);
        p.add(new JLabel("Tipo de impresión:")); p.add(tipoF);
        p.add(new JLabel("NIT cliente (opcional):")); p.add(nitF);

        int res = JOptionPane.showConfirmDialog(this, p, "Generar cotización", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                double ancho = Double.parseDouble(anchoF.getText().trim());
                double alto = Double.parseDouble(altoF.getText().trim());
                String material = materialF.getText().trim();
                String tipo = tipoF.getText().trim();
                String nit = nitF.getText().trim();

                if (nit.isEmpty()) nit = null;

                double costo = controller.cotizar(ancho, alto, material, tipo, nit);
                JOptionPane.showMessageDialog(this, "Cotización: " + costo, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ancho y alto deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JLabel createLogoLabel() {
        String path = "src/main/resources/assets/logo/logo.png";
        File f = new File(path);
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
            JLabel lbl = new JLabel(new ImageIcon(img));
            lbl.setOpaque(false);
            return lbl;
        } else {
            JLabel placeholder = new JLabel("LOGO");
            placeholder.setPreferredSize(new Dimension(200, 120));
            placeholder.setForeground(Color.WHITE);
            placeholder.setFont(new Font("SansSerif", Font.BOLD, 30));
            placeholder.setHorizontalAlignment(SwingConstants.CENTER);
            placeholder.setVerticalAlignment(SwingConstants.CENTER);
            placeholder.setOpaque(false);
            return placeholder;
        }
    }

    private JLabel createSmallLogoLabel() {
        String path = "src/main/resources/assets/logo/logo.png";
        File f = new File(path);
        JLabel lbl = new JLabel();
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(5, 5, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(img));
        } else {
            lbl.setPreferredSize(new Dimension(5,5));
            lbl.setOpaque(true);
            lbl.setBackground(Color.DARK_GRAY);
        }
        return lbl;
    }

    private JButton styledButtonWithBorder(String text, Color bg, Color borderColor) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.PLAIN, 14));
        b.setOpaque(true);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(300, 40));
        Border line = new LineBorder(borderColor, 2, true);
        Border empty = new EmptyBorder(8,12,8,12);
        b.setBorder(new CompoundBorder(line, empty));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
