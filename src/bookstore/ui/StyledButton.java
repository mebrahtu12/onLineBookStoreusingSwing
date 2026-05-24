package bookstore.ui;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {
    public enum Variant { PRIMARY, SECONDARY, ACCENT }

    private final Variant variant;
    private boolean hover;

    public StyledButton(String text, Variant variant) {
        super(text);
        this.variant = variant;
        setFont(Theme.BUTTON_FONT);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(10, 16, 10, 16));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = backgroundColor();
        Color fg = foregroundColor();
        Color border = borderColor();

        if (!isEnabled()) {
            bg = Theme.BORDER;
            fg = Theme.TEXT_MUTED;
            border = Theme.BORDER;
        } else if (getModel().isPressed()) {
            bg = bg.darker();
        } else if (hover) {
            bg = bg.brighter();
        }

        int arc = 10;
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2.setColor(border);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

        g2.setColor(fg);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        String text = getText();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, x, y);
        g2.dispose();
    }

    private Color backgroundColor() {
        return switch (variant) {
            case PRIMARY -> hover ? Theme.PRIMARY.brighter() : Theme.PRIMARY;
            case ACCENT -> hover ? Theme.ACCENT.brighter() : Theme.ACCENT;
            case SECONDARY -> hover ? Theme.SECONDARY_BTN_HOVER : Theme.SECONDARY_BTN;
        };
    }

    private Color foregroundColor() {
        return switch (variant) {
            case PRIMARY, ACCENT -> Color.WHITE;
            case SECONDARY -> Theme.PRIMARY_DARK;
        };
    }

    private Color borderColor() {
        return switch (variant) {
            case PRIMARY -> Theme.PRIMARY_DARK;
            case ACCENT -> Theme.ACCENT_HOVER;
            case SECONDARY -> Theme.PRIMARY;
        };
    }
}
