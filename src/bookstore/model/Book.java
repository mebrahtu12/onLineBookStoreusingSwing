package bookstore.model;

import java.io.Serializable;

public class Book implements BookComponent, Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String title;
    private double price;
    private String categoryName;
    private int stock;

    public Book(String title, double price, String categoryName) {
        this(title, price, categoryName, 5);
    }

    public Book(String title, double price, String categoryName, int stock) {
        this.title = title;
        this.price = price;
        this.categoryName = categoryName;
        this.stock = Math.max(0, stock);
    }

    private Book(Book other) {
        this.title = other.title;
        this.price = other.price;
        this.categoryName = other.categoryName;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getStock() {
        return stock;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    public void decrementStock() {
        if (stock > 0) {
            stock--;
        }
    }

    public void incrementStock() {
        stock++;
    }

    public String getCatalogLabel() {
        return title + "  •  $" + String.format("%.2f", price)
                + "  (" + categoryName + ")  •  In stock: " + stock;
    }

    public String getCartLabel() {
        return title + "  •  $" + String.format("%.2f", price) + "  (" + categoryName + ")";
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public Book clone() {
        return new Book(this);
    }

    @Override
    public String toString() {
        return getCatalogLabel();
    }
}
