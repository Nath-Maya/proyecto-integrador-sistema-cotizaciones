package view;

import controller.AppController;
import view.dialog.ClienteFormDialog;
import view.dialog.CotizacionDialog;
import view.dialog.InventoryControlDialog;
import view.dialog.LoginDialog;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {

    private AppController controller = new AppController();
    private JPanel buttonsContainer;
    private JLabel screenTitleLabel;

    public MainWindow() {
        setTitle("Sistema de Cotizaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.setBackground(UIStyle.COLOR_BLACK);

        // Top bar: small logo (5x5) + screen title, grey background
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        topBar.setBackground(UIStyle.COLOR_TOP_BAR);
        topBar.setPreferredSize(new Dimension(getWidth(), 28));
        JLabel smallLogo = createSmallLogoLabel();
        topBar.add(smallLogo);
        JLabel screenTitle = new JLabel("Inicio");
        screenTitle.setFont(UIStyle.FONT_TITLE_SMALL);
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
        rebuildButtons();

        setVisible(true);
    }

    private void rebuildButtons() {
        buttonsContainer.removeAll();

        if (!controller.isAppLoggedIn()) {
            JButton iniciarBtn = UIStyle.createMainButton("✅ Iniciar sesión");
            iniciarBtn.addActionListener(e -> openLoginDialog());
            JButton salirBtn = UIStyle.createMainButton("⛔ Salir");
            salirBtn.addActionListener(e -> System.exit(0));

            buttonsContainer.add(iniciarBtn);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(salirBtn);
            screenTitleLabel.setText("Inicio");
        } else {
            JButton registrarCliente = UIStyle.createMainButton("📝 Registrar cliente");
            registrarCliente.addActionListener(e -> openClienteDialog());

            JButton generarCot = UIStyle.createMainButton("💲 Generar cotización");
            generarCot.addActionListener(e -> openCotizacionDialog());

            JButton inventarioBtn = UIStyle.createMainButton("📦 Control inventario");
            inventarioBtn.addActionListener(e -> openInventoryDialog());

            JButton salirBtn = UIStyle.createMainButton("⛔ Salir");
            salirBtn.addActionListener(e -> System.exit(0));

            buttonsContainer.add(registrarCliente);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(generarCot);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(inventarioBtn);
            buttonsContainer.add(Box.createVerticalStrut(10));
            buttonsContainer.add(salirBtn);
            screenTitleLabel.setText("Principal");
        }

        buttonsContainer.revalidate();
        buttonsContainer.repaint();
    }

    private void openClienteDialog() {
        ClienteFormDialog dialog = new ClienteFormDialog(this, controller);
        dialog.setVisible(true);
    }

    private void openLoginDialog() {
        LoginDialog dialog = new LoginDialog(this, controller);
        dialog.setVisible(true);
        if (dialog.isAuthenticated()) {
            JOptionPane.showMessageDialog(this, "Bienvenido, " + controller.getAppUser(), "Éxito", JOptionPane.PLAIN_MESSAGE);
            rebuildButtons();
        }
    }

    private void openCotizacionDialog() {
        CotizacionDialog dialog = new CotizacionDialog(this, controller);
        dialog.setVisible(true);
    }

    private void openInventoryDialog() {
        InventoryControlDialog dialog = new InventoryControlDialog(this, controller);
        dialog.setVisible(true);
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
            placeholder.setForeground(UIStyle.COLOR_WHITE);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
