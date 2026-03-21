package view.dialog;

import controller.AppController;
import view.UIStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginDialog extends JDialog {
    private final AppController controller;
    private boolean authenticated = false;

    private JTextField userField;
    private JPasswordField passField;

    public LoginDialog(Window owner, AppController controller) {
        super(owner, "Ingreso", ModalityType.APPLICATION_MODAL);
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIStyle.COLOR_BLACK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel fields = new JPanel(new GridLayout(0, 1, 0, 6));
        fields.setOpaque(false);

        JLabel userLbl = new JLabel("Usuario:");
        userLbl.setForeground(UIStyle.COLOR_WHITE);

        JLabel passLbl = new JLabel("Clave:");
        passLbl.setForeground(UIStyle.COLOR_WHITE);

        userField = new JTextField();
        passField = new JPasswordField();

        UIStyle.styleDarkInput(userField);
        UIStyle.styleDarkInput(passField);

        fields.add(userLbl);
        fields.add(userField);
        fields.add(passLbl);
        fields.add(passField);
        panel.add(fields, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        JButton accederBtn = UIStyle.createMainButton("Acceder");
        accederBtn.setPreferredSize(new Dimension(120, 36));
        JButton cancelarBtn = UIStyle.createMainButton("Cancelar");
        cancelarBtn.setPreferredSize(new Dimension(120, 36));

        accederBtn.addActionListener(e -> doLogin());
        cancelarBtn.addActionListener(e -> dispose());

        btnPanel.add(accederBtn);
        btnPanel.add(cancelarBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        setPreferredSize(new Dimension(360, 210));
        pack();
        setLocationRelativeTo(getOwner());

        getRootPane().setDefaultButton(accederBtn);
    }

    private void doLogin() {
        boolean ok = controller.appLogin(userField.getText().trim(), new String(passField.getPassword()));
        if (ok) {
            authenticated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
