package bookstore.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public final class Theme {
    public static final Color BACKGROUND = new Color(245, 240, 232);
    public static final Color PANEL = new Color(255, 252, 247);
    public static final Color PRIMARY = new Color(45, 90, 74);
    public static final Color PRIMARY_DARK = new Color(30, 62, 52);
    public static final Color ACCENT = new Color(196, 120, 74);
    public static final Color ACCENT_HOVER = new Color(175, 98, 58);
    public static final Color TEXT = new Color(35, 40, 38);
    public static final Color TEXT_MUTED = new Color(95, 105, 100);
    public static final Color BORDER = new Color(210, 200, 188);
    public static final Color LIST_SELECTION = new Color(220, 235, 228);
    public static final Color SECONDARY_BTN = new Color(255, 255, 255);
    public static final Color SECONDARY_BTN_HOVER = new Color(236, 244, 240);
    public static final Color HEADER_GRADIENT_START = new Color(45, 90, 74);
    public static final Color HEADER_GRADIENT_END = new Color(68, 120, 100);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font BUTTON_FONT = new Font("Segoe UI Semibold", Font.BOLD, 12);

    private Theme() {
    }

    public static void applyGlobal() {
        UIManager.put("Panel.background", PANEL);
        UIManager.put("OptionPane.background", PANEL);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("List.background", Color.WHITE);
        UIManager.put("List.foreground", TEXT);
        UIManager.put("List.selectionBackground", LIST_SELECTION);
        UIManager.put("List.selectionForeground", PRIMARY_DARK);
    }

    public static Border titled(String title) {
        return BorderFactory.createTitledBorder(
                new LineBorder(BORDER),
                title,
                0, 0,
                BUTTON_FONT,
                PRIMARY);
    }

    public static Border padded(int padding) {
        return new EmptyBorder(padding, padding, padding, padding);
    }

    public static StyledButton primaryButton(String text) {
        return new StyledButton(text, StyledButton.Variant.PRIMARY);
    }

    public static StyledButton secondaryButton(String text) {
        return new StyledButton(text, StyledButton.Variant.SECONDARY);
    }

    public static JPanel createHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, HEADER_GRADIENT_START,
                        getWidth(), getHeight(), HEADER_GRADIENT_END);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(padded(18));
        header.setPreferredSize(new Dimension(0, 88));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(SUBTITLE_FONT);
        subLabel.setForeground(new Color(230, 245, 238));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setOpaque(false);
        text.add(titleLabel);
        text.add(subLabel);
        header.add(text, BorderLayout.WEST);
        return header;
    }
}
