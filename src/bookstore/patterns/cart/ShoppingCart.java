package bookstore.patterns.cart;

import bookstore.model.Book;
import bookstore.patterns.mediator.BookstoreMediator;
import bookstore.patterns.memento.CartMemento;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private final List<Book> books = new ArrayList<>();
    private BookstoreMediator mediator;
    private double discountAmount;

    public ShoppingCart(BookstoreMediator mediator) {
        this.mediator = mediator;
    }

    public void setMediator(BookstoreMediator mediator) {
        this.mediator = mediator;
    }

    public void addBook(Book book) {
        books.add(book.clone());
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void removeAt(int index) {
        if (index >= 0 && index < books.size()) {
            books.remove(index);
        }
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public void clearCart() {
        books.clear();
        discountAmount = 0;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = Math.max(0, discountAmount);
    }

    public double getSubtotal() {
        return books.stream().mapToDouble(Book::getPrice).sum();
    }

    public double getTotalAmount() {
        return Math.max(0, getSubtotal() - discountAmount);
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public CartMemento save() {
        return new CartMemento(books);
    }

    public void restore(CartMemento memento) {
        books.clear();
        for (Book book : memento.getSavedBooks()) {
            books.add(book.clone());
        }
    }
}
