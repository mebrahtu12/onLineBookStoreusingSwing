package bookstore.ui;

import bookstore.model.Category;

import javax.swing.*;
import java.awt.*;

public class GenreListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        label.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        label.setIconTextGap(12);
        label.setVerticalAlignment(SwingConstants.CENTER);

        if (value instanceof Category category) {
            label.setText(category.getName());
            label.setIcon(BookCoverProvider.getCoverForGenre(category.getName()));
            label.setForeground(isSelected ? Theme.PRIMARY_DARK : Theme.PRIMARY);
        }

        label.setBackground(isSelected ? Theme.LIST_SELECTION
                : (index % 2 == 0 ? Color.WHITE : new Color(250, 248, 244)));
        return label;
    }
}
