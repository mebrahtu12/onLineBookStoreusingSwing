package bookstore.ui;

import bookstore.service.UserRegistry;

import javax.swing.*;
import java.awt.*;

public final class AuthDialog {
    private AuthDialog() {
    }

    public static boolean showRegister(Component parent, UserRegistry registry) {
        JTextField username = field();
        JPasswordField password = passwordField();
        JPasswordField confirm = passwordField();

        int result = JOptionPane.showConfirmDialog(parent,
                form("Create your account",
                        new Row("Username", username),
                        new Row("Password", password),
                        new Row("Confirm password", confirm)),
                "Register — Online Book Store",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        String user = username.getText().trim();
        String pass = new String(password.getPassword());
        String pass2 = new String(confirm.getPassword());

        if (user.isEmpty()) {
            warn(parent, "Username is required.");
            return false;
        }
        if (pass.length() < 4) {
            warn(parent, "Password must be at least 4 characters.");
            return false;
        }
        if (!pass.equals(pass2)) {
            warn(parent, "Passwords do not match.");
            return false;
        }
        if (registry.isRegistered(user)) {
            warn(parent, "Username already exists. Please log in instead.");
            return false;
        }
        if (!registry.register(user, pass)) {
            warn(parent, "Registration failed. Try again.");
            return false;
        }
        info(parent, "Account created for \"" + user + "\".\nYou can now log in.");
        return true;
    }

    public static String showLogin(Component parent, UserRegistry registry) {
        JTextField username = field();
        JPasswordField password = passwordField();

        int result = JOptionPane.showConfirmDialog(parent,
                form("Log in to your account",
                        new Row("Username", username),
                        new Row("Password", password)),
                "Log In — Online Book Store",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String user = username.getText().trim();
        String pass = new String(password.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            warn(parent, "Enter username and password.");
            return null;
        }
        if (!registry.isRegistered(user)) {
            warn(parent, "No account found. Please register first.");
            return null;
        }
        if (!registry.authenticate(user, pass)) {
            warn(parent, "Incorrect password.");
            return null;
        }
        return user;
    }

    private record Row(String label, JComponent field) {
    }

    private static JPanel form(String title, Row... rows) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 12, 0);

        JLabel heading = new JLabel(title);
        heading.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD));
        heading.setForeground(Theme.PRIMARY_DARK);
        panel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 0, 4, 8);
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
