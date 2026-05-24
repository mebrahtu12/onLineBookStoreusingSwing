package bookstore.ui;

import bookstore.model.Book;
import bookstore.model.BookComponent;
import bookstore.model.Category;

import javax.swing.*;
import java.awt.*;

public class BookListCellRenderer extends DefaultListCellRenderer {
    public static final int ROW_HEIGHT = 58;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        label.setFont(Theme.BODY_FONT);
        label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        label.setIconTextGap(12);
        label.setVerticalAlignment(SwingConstants.CENTER);

        if (value instanceof Category category) {
            label.setText(category.getName());
            label.setIcon(BookCoverProvider.getCoverForGenre(category.getName()));
            label.setForeground(Theme.PRIMARY_DARK);
            label.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD));
        } else if (value instanceof Book book) {
            boolean cartView = Boolean.TRUE.equals(list.getClientProperty("cartView"));
            label.setText(cartView ? book.getCartLabel() : book.getCatalogLabel());
            label.setIcon(BookCoverProvider.getCoverForBook(book));
            if (!cartView && !book.isInStock()) {
                label.setForeground(new Color(160, 90, 90));
            } else {
                label.setForeground(Theme.TEXT);
            }
        } else {
            label.setIcon(null);
            label.setForeground(Theme.TEXT_MUTED);
        }

        if (isSelected) {
            label.setBackground(Theme.LIST_SELECTION);
            label.setForeground(Theme.PRIMARY_DARK);
        } else {
            label.setBackground(index % 2 == 0 ? Color.WHITE : new Color(250, 248, 244));
        }
        return label;
    }
}
