package bookstore.patterns.state;

public interface OrderState {
    String getName();
    OrderState next();
}
