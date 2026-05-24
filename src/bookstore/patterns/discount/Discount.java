package bookstore.patterns.discount;

public interface Discount {
    double apply(double subtotal);
    String getDescription();
}
