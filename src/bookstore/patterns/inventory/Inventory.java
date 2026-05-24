package bookstore.patterns.inventory;

import bookstore.model.Book;
import bookstore.model.BookComponent;
import bookstore.model.Category;
import bookstore.patterns.mediator.BookstoreMediator;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final Category rootCategory = new Category("All Books");
    private BookstoreMediator mediator;

    public Inventory(BookstoreMediator mediator) {
        this.mediator = mediator;
    }

    public void setMediator(BookstoreMediator mediator) {
        this.mediator = mediator;
    }

    public void addCategory(Category category) {
        rootCategory.addComponent(category);
    }

    public Category getRootCategory() {
        return rootCategory;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        collectBooks(rootCategory, books);
        return books;
    }

    public List<Category> getTopLevelGenres() {
        List<Category> genres = new ArrayList<>();
        for (BookComponent component : rootCategory.getComponents()) {
            if (component instanceof Category category) {
                genres.add(category);
            }
        }
        return genres;
    }

    public List<Book> getBooksInCategory(Category category) {
        List<Book> books = new ArrayList<>();
        collectBooks(category, books);
        return books;
    }

    public Book findBook(String title, String categoryName) {
        for (Book book : getAllBooks()) {
            if (book.getTitle().equals(title) && book.getCategoryName().equals(categoryName)) {
                return book;
            }
        }
        return null;
    }

    private void collectBooks(Category category, List<Book> books) {
        for (BookComponent component : category.getComponents()) {
            if (component instanceof Book book) {
                books.add(book);
            } else if (component instanceof Category sub) {
                collectBooks(sub, books);
            }
        }
    }
}
