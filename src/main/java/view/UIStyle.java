package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public final class UIStyle {
    public static final Color COLOR_BLACK = Color.BLACK;
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Color COLOR_TOP_BAR = new Color(200, 200, 200);
    public static final Color COLOR_INPUT_BG = new Color(30, 30, 30);

    public static final Font FONT_TITLE_SMALL = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.PLAIN, 14);

    private UIStyle() {}

    public static JButton createMainButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(COLOR_BLACK);
        b.setForeground(COLOR_WHITE);
        b.setFocusPainted(false);
        b.setFont(FONT_BUTTON);
        b.setOpaque(true);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(300, 40));

        Border line = new LineBorder(COLOR_WHITE, 2, true);
        Border empty = new EmptyBorder(8, 12, 8, 12);
        b.setBorder(new CompoundBorder(line, empty));
        return b;
    }

    public static void styleDarkInput(JTextField field) {
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_WHITE);
        field.setCaretColor(COLOR_WHITE);
    }
}
