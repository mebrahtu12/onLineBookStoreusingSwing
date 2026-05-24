package bookstore.patterns.state;

public class DeliveredState implements OrderState {
    @Override
    public String getName() {
        return "Delivered";
    }

    @Override
    public OrderState next() {
        return this;
    }
}
