package bookstore.service;

import bookstore.model.Book;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistoryService {
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path ordersFile;

    public OrderHistoryService(Path ordersFile) {
        this.ordersFile = ordersFile;
    }

    public void appendOrder(List<Book> books, double totalPaid, String paymentMethod) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("======================================\n");
        entry.append("Order Date: ").append(LocalDateTime.now().format(FORMAT)).append("\n");
        entry.append("Payment: ").append(paymentMethod).append("\n");
        entry.append("Items Purchased:\n");
        for (Book book : books) {
            entry.append("- ").append(book.getTitle())
                    .append(" (").append(book.getCategoryName()).append(") : $")
                    .append(String.format("%.2f", book.getPrice())).append("\n");
        }
        entry.append("Total Paid: $").append(String.format("%.2f", totalPaid)).append("\n");
        entry.append("======================================\n\n");

        Files.writeString(ordersFile, entry.toString(),
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND);
    }

    public String readHistory() {
        if (!Files.exists(ordersFile)) {
            return "No orders yet.";
        }
        try {
            return Files.readString(ordersFile);
        } catch (IOException e) {
            return "Could not read order history.";
        }
    }
}
