package bookstore.ui;

import bookstore.model.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class BookCoverProvider {
    private static final Map<String, ImageIcon> GENRE_COVERS = new HashMap<>();
    private static final int WIDTH = 36;
    private static final int HEIGHT = 48;

    private BookCoverProvider() {
    }

    public static ImageIcon getCoverForBook(Book book) {
        return getCoverForGenre(book.getCategoryName());
    }

    public static ImageIcon getCoverForGenre(String genre) {
        String key = genre == null ? "General" : genre;
        return GENRE_COVERS.computeIfAbsent(key, BookCoverProvider::createGenreCover);
    }

    private static ImageIcon createGenreCover(String genre) {
        Color base = genreColor(genre);
        Color spine = base.darker();
        Color accent = base.brighter();

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(base);
        g.fillRoundRect(2, 2, WIDTH - 4, HEIGHT - 4, 6, 6);

        g.setColor(spine);
        g.fillRect(4, 4, 8, HEIGHT - 8);

        g.setColor(accent);
        g.fillRect(14, 10, WIDTH - 20, 4);
        g.fillRect(14, 18, WIDTH - 22, 3);
        g.fillRect(14, 25, WIDTH - 18, 3);

        g.setColor(new Color(255, 255, 255, 200));
        g.setFont(new Font("Segoe UI", Font.BOLD, 7));
        String label = abbreviate(genre);
        FontMetrics fm = g.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(label)) / 2;
        g.drawString(label, Math.max(12, x), HEIGHT - 8);

        g.dispose();
        return new ImageIcon(image);
    }

    private static String abbreviate(String genre) {
        if (genre.length() <= 8) {
            return genre.toUpperCase();
        }
        return genre.substring(0, 7).toUpperCase() + "…";
    }

    private static Color genreColor(String genre) {
        return switch (genre) {
            case "Fiction" -> new Color(122, 58, 72);
            case "Sci-Fi" -> new Color(42, 78, 128);
            case "Fantasy" -> new Color(88, 58, 128);
            case "Programming" -> new Color(58, 72, 88);
            case "History" -> new Color(128, 92, 52);
            case "Science" -> new Color(42, 108, 98);
            case "Mystery" -> new Color(52, 52, 72);
            case "Biography" -> new Color(108, 82, 58);
            default -> new Color(72, 88, 98);
        };
    }
}
