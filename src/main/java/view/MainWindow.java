package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("Sistema de Cotizaciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.setBackground(Color.BLACK);

        // Top: logo + title
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel logoLabel = createLogoLabel();
        top.add(logoLabel, BorderLayout.WEST);

        JLabel title = new JLabel("Sistema de Cotizaciones");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        top.add(title, BorderLayout.CENTER);

        cp.add(top, BorderLayout.NORTH);

        // Center: brief description
        JTextArea descripcion = new JTextArea("Bienvenido al sistema. Seleccione una opción.");
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setForeground(Color.WHITE);
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descripcion.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        cp.add(descripcion, BorderLayout.CENTER);

        // Bottom: buttons
        JPanel botones = new JPanel();
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        botones.setLayout(new GridLayout(1, 4, 10, 0));

        // Colors: bright green (confirm) and red (negative)
        Color confirmGreen = new Color(0x00, 0xFF, 0x66); // aproximación verde encendido
        Color negativeRed = new Color(0xFF, 0x44, 0x44);

        JButton registrarBtn = styledButton("Registrar empresa", confirmGreen);
        JButton loginEmpresaBtn = styledButton("Inicio sesión empresa", new Color(70,70,70));
        JButton loginAdminBtn = styledButton("Inicio sesión admin", new Color(70,70,70));
        JButton salirBtn = styledButton("Salir", negativeRed);

        salirBtn.addActionListener(e -> System.exit(0));

        botones.add(registrarBtn);
        botones.add(loginEmpresaBtn);
        botones.add(loginAdminBtn);
        botones.add(salirBtn);

        cp.add(botones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel createLogoLabel() {
        // Intenta cargar imagen desde resources; si no existe, usar placeholder
        String path = "src/main/resources/assets/logo/logo.png";
        File f = new File(path);
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
            JLabel lbl = new JLabel(new ImageIcon(img));
            lbl.setOpaque(false);
            return lbl;
        } else {
            JLabel placeholder = new JLabel("LOGO");
            placeholder.setPreferredSize(new Dimension(120, 80));
            placeholder.setForeground(Color.WHITE);
            placeholder.setFont(new Font("SansSerif", Font.BOLD, 20));
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
        b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return b;
    }

    public static void main(String[] args) {
        // For quick testing when running this class directly
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
