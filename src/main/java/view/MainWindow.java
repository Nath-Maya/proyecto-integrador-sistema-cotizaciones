package view;

import controller.AppController;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {

    private AppController controller = new AppController();
    private JPanel buttonsContainer;

    public MainWindow() {
        setTitle("Sistema de Cotizaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.setBackground(Color.BLACK);

        // Center panel: logo centered and buttons in column
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        // Spacer top
        center.add(Box.createVerticalStrut(20));

        JLabel logoLabel = createLogoLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(logoLabel);

        // Title under logo
        JLabel title = new JLabel("Sistema de Cotizaciones");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        center.add(title);

        // Buttons container (will be rebuilt according to login state)
        buttonsContainer = new JPanel();
        buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.Y_AXIS));
        buttonsContainer.setOpaque(false);
        buttonsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(buttonsContainer);

        // Build initial buttons
        rebuildButtons();

        cp.add(center, BorderLayout.CENTER);

        setVisible(true);
    }

    private void rebuildButtons() {
        buttonsContainer.removeAll();

        Color confirmGreen = new Color(0x00, 0xFF, 0x66);
        Color neutral = new Color(70,70,70);
        Color negativeRed = new Color(0xFF, 0x44, 0x44);

        if (!controller.isAppLoggedIn()) {
            JButton iniciarBtn = styledButton("Iniciar sesión", neutral);
            iniciarBtn.addActionListener(e -> onAppLogin());
            JButton salirBtn = styledButton("Salir", negativeRed);
            salirBtn.addActionListener(e -> System.exit(0));

            buttonsContainer.add(iniciarBtn);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(salirBtn);
        } else {
            JButton registrarCliente = styledButton("Registrar cliente", confirmGreen);
            registrarCliente.addActionListener(e -> onRegistrar());

            JButton generarCot = styledButton("Generar cotización", neutral);
            generarCot.addActionListener(e -> onGenerarCotizacion());

            JButton cerrarSesion = styledButton("Cerrar sesión", neutral);
            cerrarSesion.addActionListener(e -> {
                controller.appLogout();
                rebuildButtons();
                JOptionPane.showMessageDialog(this, "Sesión cerrada", "Info", JOptionPane.INFORMATION_MESSAGE);
            });

            JButton salirBtn = styledButton("Salir", negativeRed);
            salirBtn.addActionListener(e -> System.exit(0));

            buttonsContainer.add(registrarCliente);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(generarCot);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(cerrarSesion);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(salirBtn);
        }

        buttonsContainer.revalidate();
        buttonsContainer.repaint();
    }

    private void onRegistrar() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JTextField nitF = new JTextField();
        JTextField nombreF = new JTextField();
        JTextField direccionF = new JTextField();
        JTextField correoF = new JTextField();
        JTextField telefonoF = new JTextField();
        JPasswordField passF = new JPasswordField();

        p.add(new JLabel("Nit:")); p.add(nitF);
        p.add(new JLabel("Nombre empresa:")); p.add(nombreF);
        p.add(new JLabel("Direccion:")); p.add(direccionF);
        p.add(new JLabel("Correo:")); p.add(correoF);
        p.add(new JLabel("Telefono:")); p.add(telefonoF);
        p.add(new JLabel("Contraseña:")); p.add(passF);

        int res = JOptionPane.showConfirmDialog(this, p, "Registrar empresa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            boolean ok = controller.registrarEmpresa(nitF.getText().trim(), nombreF.getText().trim(), direccionF.getText().trim(), correoF.getText().trim(), telefonoF.getText().trim(), new String(passF.getPassword()));
            if (ok) JOptionPane.showMessageDialog(this, "Empresa registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(this, "Ya existe una empresa con ese NIT", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAppLogin() {
        JPanel p = new JPanel(new GridLayout(0,1));
        JTextField userF = new JTextField();
        JPasswordField passF = new JPasswordField();
        p.add(new JLabel("Usuario:")); p.add(userF);
        p.add(new JLabel("Contraseña:")); p.add(passF);

        int res = JOptionPane.showConfirmDialog(this, p, "Iniciar sesión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            boolean ok = controller.appLogin(userF.getText().trim(), new String(passF.getPassword()));
            if (ok) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + controller.getAppUser(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                rebuildButtons();
            }
            else JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
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

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.PLAIN, 14));
        b.setOpaque(true);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(300, 40));
        Border line = new LineBorder(Color.DARK_GRAY, 2, true);
        Border empty = new EmptyBorder(8,12,8,12);
        b.setBorder(new CompoundBorder(line, empty));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
