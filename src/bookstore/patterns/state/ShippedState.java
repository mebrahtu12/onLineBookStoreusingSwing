package bookstore.patterns.state;

public class ShippedState implements OrderState {
    @Override
    public String getName() {
        return "Shipped";
    }

    @Override
    public OrderState next() {
        return new DeliveredState();
    }
}
