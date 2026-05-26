package bookstore.ui;

import bookstore.service.UserRegistry;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class AuthDialog {
    private AuthDialog() {
    }

    public static boolean showRegister(Component parent, UserRegistry registry) {
        JTextField username = field();
        JPasswordField password = passwordField();
        JPasswordField confirm = passwordField();

        AtomicBoolean accepted = new AtomicBoolean(false);
        JDialog dialog = createDialog(parent, "Register — Online Book Store");

        JPanel content = new JPanel(new BorderLayout(0, 24));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(48, 72, 48, 72));
        content.add(form("Create your account",
                new Row("Username", username),
                new Row("Password", password),
                new Row("Confirm password", confirm)), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actions.setOpaque(false);
        StyledButton btnCancel = Theme.secondaryButton("Cancel");
        StyledButton btnOk = Theme.primaryButton("Register");

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            String user = username.getText().trim();
            String pass = new String(password.getPassword());
            String pass2 = new String(confirm.getPassword());

            if (user.isEmpty()) {
                warn(parent, "Username is required.");
                return;
            }
            if (pass.length() < 4) {
                warn(parent, "Password must be at least 4 characters.");
                return;
            }
            if (!pass.equals(pass2)) {
                warn(parent, "Passwords do not match.");
                return;
            }
            if (registry.isRegistered(user)) {
                warn(parent, "Username already exists. Please log in instead.");
                return;
            }
            if (!registry.register(user, pass)) {
                warn(parent, "Registration failed. Try again.");
                return;
            }
            info(parent, "Account created for \"" + user + "\".\nYou can now log in.");
            accepted.set(true);
            dialog.dispose();
        });

        actions.add(btnCancel);
        actions.add(btnOk);
        content.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(createWrapper(content));
        registerEscape(dialog);
        dialog.setVisible(true);
        return accepted.get();
    }

    public static String showLogin(Component parent, UserRegistry registry) {
        JTextField username = field();
        JPasswordField password = passwordField();

        AtomicReference<String> result = new AtomicReference<>();
        JDialog dialog = createDialog(parent, "Log In — Online Book Store");

        JPanel content = new JPanel(new BorderLayout(0, 24));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(48, 72, 48, 72));
        content.add(form("Log in to your account",
                new Row("Username", username),
                new Row("Password", password)), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actions.setOpaque(false);
        StyledButton btnCancel = Theme.secondaryButton("Cancel");
        StyledButton btnOk = Theme.primaryButton("Log In");

        btnCancel.addActionListener(e -> dialog.dispose());
        btnOk.addActionListener(e -> {
            String user = username.getText().trim();
            String pass = new String(password.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                warn(parent, "Enter username and password.");
                return;
            }
            if (!registry.isRegistered(user)) {
                warn(parent, "No account found. Please register first.");
                return;
            }
            if (!registry.authenticate(user, pass)) {
                warn(parent, "Incorrect password.");
                return;
            }
            result.set(user);
            dialog.dispose();
        });

        actions.add(btnCancel);
        actions.add(btnOk);
        content.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(createWrapper(content));
        registerEscape(dialog);
        dialog.setVisible(true);
        return result.get();
    }

    private static JPanel createWrapper(JPanel content) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Theme.BACKGROUND);
        wrapper.setOpaque(true);
        wrapper.add(content, new GridBagConstraints());
        return wrapper;
    }

    private static JDialog createDialog(Component parent, String title) {
        Window window = SwingUtilities.windowForComponent(parent);
        JDialog dialog = new JDialog(window, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds(0, 0, screen.width, screen.height);
        return dialog;
    }

    private static void registerEscape(JDialog dialog) {
        dialog.getRootPane().registerKeyboardAction(e -> dialog.dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private record Row(String label, JComponent field) {
    }

    private static JPanel form(String title, Row... rows) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 14, 0);

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, 22f));
        heading.setForeground(Theme.PRIMARY_DARK);
        panel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 0, 6, 8);
        for (Row row : rows) {
            gbc.gridy++;
            gbc.gridx = 0;
            JLabel label = new JLabel(row.label() + ":");
            label.setFont(Theme.BODY_FONT);
            label.setForeground(Theme.TEXT);
            panel.add(label, gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            row.field().setFont(Theme.BODY_FONT);
            panel.add(row.field(), gbc);
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
        }
        return panel;
    }

    private static JTextField field() {
        return new JTextField(18);
    }

    private static JPasswordField passwordField() {
        return new JPasswordField(18);
    }

    private static void warn(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Online Book Store", JOptionPane.WARNING_MESSAGE);
    }

    private static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Online Book Store", JOptionPane.INFORMATION_MESSAGE);
    }
}
