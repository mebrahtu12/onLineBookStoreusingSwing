package bookstore.patterns.memento;

import bookstore.model.Book;

import java.util.ArrayList;
import java.util.List;

public class CartMemento {
    private final List<Book> savedBooks;

    public CartMemento(List<Book> books) {
        savedBooks = new ArrayList<>();
        for (Book book : books) {
            savedBooks.add(book.clone());
        }
    }

    public List<Book> getSavedBooks() {
        return savedBooks;
    }
}
