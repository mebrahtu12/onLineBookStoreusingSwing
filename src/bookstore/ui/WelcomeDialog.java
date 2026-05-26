package bookstore.ui;

import javax.swing.*;
import java.awt.*;

public final class WelcomeDialog {
    private WelcomeDialog() {
    }

    public static void show(JFrame owner, Runnable onRegister, Runnable onLogin) {
        JDialog dialog = new JDialog(owner, "Online Book Store", true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds(0, 0, screen.width, screen.height);

        WelcomePanel panel = new WelcomePanel(onRegister, onLogin, dialog);
        dialog.setContentPane(panel);
        panel.start();

        // ESC to close
        dialog.getRootPane().registerKeyboardAction(e -> panel.disposeDialog(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialog.setVisible(true);
    }

    private static final class WelcomePanel extends JPanel {
        private final Runnable onRegister;
        private final Runnable onLogin;
        private final JDialog dialog;

        private String tip = "";
        private int tipIndex = 0;

        private final Timer tipTimer;

        private final JLabel tipLabel;

        private final String[] tips = new String[] {
                "Tip: Select a genre to see related books.",
                "Tip: Add books to your cart, then checkout.",
                "Tip: Stock is limited per title — try adding the same book multiple times.",
                "Tip: Remove from cart returns the copy back to stock."
        };

        WelcomePanel(Runnable onRegister, Runnable onLogin, JDialog dialog) {
            this.onRegister = onRegister;
            this.onLogin = onLogin;
            this.dialog = dialog;

            setLayout(new BorderLayout());
            setBackground(Theme.BACKGROUND);

            JPanel content = new JPanel();
            content.setOpaque(false);
            content.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 18, 0);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("Online Book Store");
            title.setFont(new Font("Segoe UI", Font.BOLD, 44));
            title.setForeground(Theme.PRIMARY_DARK);
            title.setHorizontalAlignment(SwingConstants.CENTER);
            content.add(title, gbc);

            gbc.gridy++;
            JLabel sub = new JLabel("Explore our collection and discover your next favorite book.");
            sub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            sub.setForeground(Theme.TEXT);
            sub.setHorizontalAlignment(SwingConstants.CENTER);
            content.add(sub, gbc);

            gbc.gridy++;
            tipLabel = new JLabel();
            tipLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            tipLabel.setForeground(Theme.TEXT_MUTED);
            tipLabel.setHorizontalAlignment(SwingConstants.CENTER);
            content.add(tipLabel, gbc);

            gbc.gridy++;
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
            buttons.setOpaque(false);

            StyledButton btnGetStarted = Theme.primaryButton("Get Started");

            btnGetStarted.addActionListener(e -> {
                disposeDialog();
                if (onRegister != null)
                    onRegister.run();
            });

            buttons.add(btnGetStarted);
            content.add(buttons, gbc);

            add(content, BorderLayout.CENTER);

            // small close button (top-right)
            JPanel closeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 12));
            closeWrap.setOpaque(false);
            StyledButton btnClose = Theme.secondaryButton("X");
            btnClose.setFont(Theme.BUTTON_FONT.deriveFont(Font.BOLD, 12f));
            btnClose.addActionListener(e -> disposeDialog());
            closeWrap.add(btnClose);
            add(closeWrap, BorderLayout.NORTH);

            tipTimer = new Timer(3200, e -> {
                tipIndex = (tipIndex + 1) % tips.length;
                tip = tips[tipIndex];
                tipLabel.setText(tip);
            });
            tipIndex = 0;
            tip = tips[tipIndex];
        }

        void start() {
            tipLabel.setText(tip);
            tipTimer.start();
        }

        void disposeDialog() {
            tipTimer.stop();
            dialog.dispose();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Theme.BACKGROUND);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
